package org.scraper.main;

import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Pool implements IPool {
	
	private static Integer defaultPoolThreads = 50;
	
	private static Long defaultPoolTimeout = 60L;
	
	
	private Set<Pool> subPools = new HashSet<>();
	
	private Integer threads = 50;
	
	private Long timeout = 60L;
	
	private ThreadPoolExecutor executor;
	
	private Boolean enabled = false;
	
	private List<Runnable> runnables;
	
	public Pool() {
		this(defaultPoolThreads, defaultPoolTimeout);
	}
	
	public Pool(Integer threads) {
		this(threads, defaultPoolTimeout);
	}
	
	public Pool(Integer threads, Long timeout) {
		this.threads = threads;
		this.timeout = timeout;
	}
	
	
	public void create() {
		create(threads, timeout);
	}
	
	public void create(Integer threads, Long timeout) {
		MainLogger.log(this).debug("Creating new pool");
		enabled = true;
		this.executor = new ThreadPoolExecutor(threads, threads,
											   timeout, TimeUnit.SECONDS,
											   new LinkedBlockingQueue<>());
		this.executor.allowCoreThreadTimeOut(true);
	}
	
	public void shutdown() {
		synchronized (this) {
			if (isEnabled()) {
				MainLogger.log(this).debug("Shuting down pool");
				executor.shutdownNow();
				try {
					if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
						executor.shutdownNow();
						if (!executor.awaitTermination(5, TimeUnit.SECONDS))
							MainLogger.log(this).debug("Pool did not terminated");
					}
				} catch (InterruptedException ie) {
					executor.shutdownNow();
					Thread.currentThread().interrupt();
				}
			}
		}
		subPools.forEach(subPool -> {
			subPool.shutdown();
			subPools.remove(subPool);
		});
	}
	
	public void pause() {
		runnables = new ArrayList<>(executor.getQueue());
		executor.getQueue().clear();
		//executor.setCorePoolSize(0);
		//executor.setMaximumPoolSize(1);
		shutdown();
		subPools.forEach(Pool::pause);
	}
	
	public void resume() {
		create();
		//executor.setCorePoolSize(threads);
		//executor.setMaximumPoolSize(threads);
		runnables.forEach(executor::submit);
		subPools.forEach(Pool::resume);
	}
	
	public boolean isEnabled() {
		return executor != null && !(executor.isTerminated() || executor.isShutdown() || executor.isTerminating()) && enabled;
	}
	
	@Override
	public void sendTask(Runnable lambda, boolean wait) {
		Callable<?> call = runnableToCallable(lambda);
		sendTask(call, wait);
	}
	
	@Override
	public void sendTaskOrRunIfFull(Runnable lambda, boolean wait) {
		Callable<?> call = runnableToCallable(lambda);
		sendTaskOrRunIfFull(call, wait);
	}
	
	@Override
	public <R> R sendTaskOrRunIfFull(Callable<R> callable, boolean wait) {
		try {
			return executor.getQueue().size() <= 0//executor.getActiveCount() < executor.getMaximumPoolSize()
					? sendTask(callable, wait)
					: callable.call();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public <R> R sendTask(Callable<R> callable, boolean wait) {
		Future<R> future = executor.submit(callable);
		try {
			return wait ? future.get() : null;
		} catch (InterruptedException | ExecutionException e) {
			MainLogger.log(this).fatal("Thread was interrupted!");
			return null;
		}
	}
	
	@Override
	public <R> List<R> sendTasks(List<Callable<R>> callables) {
		List<R> list = new ArrayList<>(callables.size());
		try {
			executor.invokeAll(callables).forEach(future -> {
				try {
					list.add(future.get());
				} catch (InterruptedException | ExecutionException e) {
					MainLogger.log(this).fatal(e);
				}
			});
		} catch (InterruptedException e) {
			MainLogger.log(this).fatal("Thread was interrupted!");
		}
		list.removeIf(Objects::isNull);
		return list;
	}
	
	@Override
	public <R> List<R> sendTasksOrRunIfFull(List<Callable<R>> callables) {
		return callables.stream()
				.map(callable ->
							 sendTaskOrRunIfFull(callable, false))
				.collect(Collectors.toList());
	}
	
	public <T, R> List<R> sendToInternalSubPool(Collection<T> list, Function<T, R> function, Integer threads) {
		list = new ArrayList<>(list);
		threads = threads > getThreads() ? getThreads() : threads;
		threads = threads >= list.size() ? list.size() : threads;
		
		Integer responses = list.size();
		
		CompletionService<R> completionService = getCompletionService();
		
		List<R> completed = new ArrayList<>();
		
		for (int i = 0; i < threads; i++) {
			T element = list.stream().findFirst().get();
			list.remove(element);
			completionService.submit(() -> function.apply(element));
		}
		
		while (completed.size() < responses) {
			R completedFunc = null;
			try {
				completedFunc = completionService.take().get();
			} catch (InterruptedException | ExecutionException ignored) {
				MainLogger.log(this).fatal("Thread was interrupted!");
			}
			
			completed.add(completedFunc);
			
			if (list.isEmpty()) break;
			T element = list.stream().findFirst().get();
			list.remove(element);
			
			completionService.submit(() -> function.apply(element));
		}
		
		return completed;
	}
	
	private Callable runnableToCallable(Runnable runnable) {
		return () -> {
			runnable.run();
			return null;
		};
	}
	
	public Pool getNewSubPool() {
		return getNewSubPool(defaultPoolThreads, 10L);
	}
	
	public Pool getNewSubPool(Integer threads, Long timeout) {
		Pool newSubPool = new Pool(threads, timeout);
		newSubPool.create();
		subPools.add(newSubPool);
		return newSubPool;
	}
	
	@Override
	public <R> List<R> forEach(List<R> list, Function<R, R> consumer) {
		List<Callable<R>> calls = list
				.stream()
				.map(e -> (Callable<R>) () ->
						consumer.apply(e))
				.collect(Collectors.toList());
		return sendTasks(calls);
	}
	
	@Override
	public ThreadPoolExecutor getExecutor() {
		return executor;
	}
	
	public <T> ExecutorCompletionService<T> getCompletionService() {
		return new ExecutorCompletionService<>(executor);
	}
	
	public static void setDefaultThreads(Integer threads) {
		Pool.defaultPoolThreads = threads;
	}
	
	@Override
	public void setThreads(Integer threads) {
		this.threads = threads;
		executor.setCorePoolSize(threads);
		executor.setMaximumPoolSize(threads);
	}
	
	public static void setDefaultTimeout(Long timeout) {
		Pool.defaultPoolTimeout = timeout;
	}
	
	private void setTimeout(Long timeout) {
		this.timeout = timeout;
		executor.setKeepAliveTime(timeout, TimeUnit.SECONDS);
	}
	
	public boolean isAvaiableThreadsGraterThan(Integer value) {
		return (getThreads() - getPoolSize()) > value;
	}
	
	public Integer getPoolSize() {
		return executor.getPoolSize();
	}
	
	
	@Override
	public Integer getThreads() {
		return defaultPoolThreads;
	}
	
	@Override
	public Integer getActiveThreads() {
		return executor.getActiveCount();
	}
	
	
}
