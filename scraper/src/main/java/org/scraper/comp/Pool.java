package org.scraper.comp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Pool {
	
	private ThreadPoolExecutor pool;
	
	public Pool(int threads) {
		if (pool != null) pool.shutdown();
		pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(threads);
	}
	
	//Send one lambda
	public void sendTask(Runnable lambda, boolean wait) throws Exception {
		sendTask(lambda, wait, 1);
	}
	
	//Send lambda converted to call
	public void sendTask(Runnable lambda, boolean wait, int times) throws Exception {
		Callable<?> call = () -> {
			lambda.run();
			return null;
		};
		sendTask(pool, call, wait, times);
	}
	
	//Send one call
	public <R> R sendTask(Callable<R> callable, boolean wait) throws Exception {
		return sendTask(pool, callable, wait, 1);
	}
	
	//Send more than one call by default pool
	public <R> R sendTask(Callable<R> callable, boolean wait, int times) throws Exception {
		return sendTask(pool, callable, wait, times);
	}
	
	/*
	* Get one thread from chosen pool and send call n-times through it
	* Wait blocks main thread, use only to load
	 */
	public <R> R sendTask(ExecutorService pool, Callable<R> callable, boolean wait, int times) throws Exception {
		
		if (times > 1) {
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
			
			if (wait) future.get();
			return null;
		}
		
		if (times == 1) {
			Future<R> future = pool.submit(callable);
			return wait ? future.get() : null;
		} else {
			throw new Exception("Times <= 0");
		}
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
	public <R> List<R> sendTasks(List<Callable<R>> callables) throws InterruptedException {
		return sendTasks(pool, callables);
	}
	
	public <R> List<R> sendTasks(ExecutorService pool, List<Callable<R>> callables) throws InterruptedException {
		return pool.invokeAll(callables).stream()
				.map(future -> {
					try {
						return future.get();
					} catch (Exception e) {
						Main.log.catching(e.getCause());
					}
					return null;
				}).collect(Collectors.toList());
	}
	
	public ThreadPoolExecutor getPool() {
		return pool;
	}
	
	
}
