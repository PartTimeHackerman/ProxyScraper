package org.scraper.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainPool implements IPool {
	
	private static IPool pool;
	
	private ThreadPoolExecutor executor;
	
	private static Integer threads = 100;
	
	private MainPool(int threads) {
		executor = new ThreadPoolExecutor(threads, threads,
										  60L, TimeUnit.SECONDS,
										  new LinkedBlockingQueue<>());
	}
	
	public static IPool getInstance() {
		if (pool == null) {
			synchronized (MainPool.class) {
				if (pool == null)
					pool = new MainPool(threads);
			}
		}
		return pool;
	}
	
	//Send one lambda
	@Override
	public void sendTask(Runnable lambda, boolean wait) {
		sendTask(lambda, wait, 1);
	}
	
	//Send lambda converted to call
	@Override
	public void sendTask(Runnable lambda, boolean wait, Integer times) {
		Callable<?> call = () -> {
			lambda.run();
			return null;
		};
		sendTask(call, wait, times);
	}
	
	//Send one call
	@Override
	public <R> R sendTask(Callable<R> callable, boolean wait) {
		try {
			return executor.getActiveCount() < executor.getMaximumPoolSize()
					? sendTask(callable, wait, 1)
					: callable.call();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public <R> R sendTask(Callable<R> callable, boolean wait, Integer times) {
		
		if (times <= 1) {
			Future<R> future = executor.submit(callable);
			try {
				return wait ? future.get() : null;
			} catch (InterruptedException | ExecutionException e) {
				MainLogger.log().fatal(Thread.currentThread().getName() + " thread was interrupted!");
			}
		}
		
		Future<R> future = executor.submit(() -> {
			List<Callable<R>> calls = new ArrayList<>();
			IntStream.range(0, times)
					.forEach(i -> calls.add(callable));
			
			sendTasks(calls);
			return null;
		});
		
		if (wait) try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			MainLogger.log().fatal("Some thread was interrupted!");
		}
		
		return null;
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
	public <R> List<R> sendTasks(List<Callable<R>> callables) {
		List<R> list = new ArrayList<>(callables.size());
		try {
			executor.invokeAll(callables).forEach(future -> {
				try {
					list.add(future.get());
				} catch (Exception e) {
					MainLogger.log().catching(e.getCause());
				}
			});
		} catch (InterruptedException e) {
			MainLogger.log().fatal("Some thread was interrupted!");
		}
		list.removeIf(r -> r == null);
		return list;
	}
	
	@Override
	public ThreadPoolExecutor getExecutor() {
		return executor;
	}
	
	@Override
	public <T> ExecutorCompletionService<T> getCompletionService() {
		return new ExecutorCompletionService<>(executor);
	}
	
	public static void setThreadsStatic(Integer threads) {
		MainPool.threads = threads;
		if (pool != null)
			pool.setThreads(threads);
	}
	
	@Override
	public void setThreads(Integer threads) {
		MainPool.threads = threads;
		executor.setCorePoolSize(threads);
		executor.setMaximumPoolSize(threads);
		
	}
	
	@Override
	public Integer getThreads() {
		return threads;
	}
	
	@Override
	public Integer getActiveThreads() {
		return executor.getActiveCount();
	}
}
