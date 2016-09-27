package org.scraper.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Pool {
	
	private ThreadPoolExecutor pool;
	
	private int threads;
	
	public Pool(int threads) {
		this.threads = threads;
		pool = new ThreadPoolExecutor(threads, threads,
									  60L, TimeUnit.SECONDS,
									  new LinkedBlockingQueue<Runnable>());
	}
	
	//Send one lambda
	public void sendTask(Runnable lambda, boolean wait) {
		sendTask(lambda, wait, 1);
	}
	
	//Send lambda converted to call
	public void sendTask(Runnable lambda, boolean wait, int times) {
		Callable<?> call = () -> {
			lambda.run();
			return null;
		};
		sendTask(pool, call, wait, times);
	}
	
	//Send one call
	public <R> R sendTask(Callable<R> callable, boolean wait) {
		return sendTask(pool, callable, wait, 1);
	}
	
	//Send more than one call by default pool
	public <R> R sendTask(Callable<R> callable, boolean wait, int times) {
		return sendTask(pool, callable, wait, times);
	}
	
	/*
	* Get one thread from chosen pool and send call n-times through it
	* Wait blocks main thread, use only to load
	 */
	public <R> R sendTask(ExecutorService pool, Callable<R> callable, boolean wait, int times) {
		
		if (times <= 1) {
			Future<R> future = pool.submit(callable);
			try {
				return wait ? future.get() : null;
			} catch (InterruptedException | ExecutionException e) {
				Main.log.fatal("Some thread was interrupted!");
			}
		}
		
		Future<?> future = pool.submit(new Callable<R>() {
			@Override
			public R call() throws Exception {
				
				List<Callable<R>> calls = new ArrayList<>();
				IntStream.range(0, times)
						.forEach(i -> calls.add(callable));
				
				sendTasks(calls);
				return null;
			}
		});
		
		if (wait) try {
			future.get();
		} catch (InterruptedException | ExecutionException e) {
			Main.log.fatal("Some thread was interrupted!");
		}
		
		return null;
	}
	
	
	public <R> List<R> forEach(List<R> list, Function<R, R> consumer) throws InterruptedException {
		List calls = list
				.stream()
				.map(e ->
							 (Callable) () ->
									 consumer.apply(e))
				.collect(Collectors.toList());
		return sendTasks(pool, calls);
	}
	
	//Send calls list to default pool
	public <R> List<R> sendTasks(List<Callable<R>> callables) {
		return sendTasks(pool, callables);
	}
	
	public <R> List<R> sendTasks(ExecutorService pool, List<Callable<R>> callables){
		try {
			return pool.invokeAll(callables).stream()
					.map(future -> {
						try {
							return future.get();
						} catch (Exception e) {
							Main.log.catching(e.getCause());
						}
						return null;
					}).collect(Collectors.toList());
		} catch (InterruptedException e) {
			Main.log.fatal("Some thread was interrupted!");
			return null;
		}
	}
	
	public ThreadPoolExecutor getPool() {
		return pool;
	}
	
	public void setThreads(int threads) {
		this.threads = threads;
		pool.setCorePoolSize(threads);
		pool.setMaximumPoolSize(threads);
	}
	
	public int getThreads() {
		return threads;
	}
}
