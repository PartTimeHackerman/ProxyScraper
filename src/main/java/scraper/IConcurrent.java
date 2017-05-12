package scraper;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IConcurrent {
	
	default <V> V send(Callable<V> callable, Pool pool) {
		return send(callable, false, pool);
	}
	
	default <V> V send(Callable<V> callable, Boolean wait, Pool pool) {
		if (pool.isAvaiableThreadsGraterThan(pool.getPoolSize() / 10))
			return pool.sendTask(callable, wait);
		else
			try {
				return callable.call();
			} catch (Exception e) {
				//MainLogger.log().fatal(e);
				return null;
			}
	}
	
	
	default void send(Runnable runnable, Pool pool) {
		send(runnable, false, pool);
	}
	
	default void send(Runnable runnable, Boolean wait, Pool pool) {
		pool.sendTask(runnable, wait);
	}
	
	default <R> List<R> sendTasks(List<Callable<R>> callables, Pool pool) {
		if (pool.isAvaiableThreadsGraterThan(pool.getPoolSize() / 10))
			return pool.sendTasks(callables);
		else
			try {
				return callables.stream().map(callable -> {
					try {
						return callable.call();
					} catch (Exception e) {
						return null;
					}
				}).collect(Collectors.toList());
			} catch (Exception e) {
				MainLogger.log(this).fatal(e);
				return null;
			}
	}
	
	default <T, R> List<R> sendToSubPool(Collection<T> list, Function<T, R> function, Integer threads, Pool pool) {
		return pool.sendToInternalSubPool(list, function, threads);
	}
	
}
