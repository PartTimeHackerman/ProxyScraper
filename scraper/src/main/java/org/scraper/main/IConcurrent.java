package org.scraper.main;

import java.util.List;
import java.util.concurrent.Callable;

public interface IConcurrent {
	
	default <V> V send(Callable<V> callable){
		return send(callable, false);
	}
	
	default void send(Runnable runnable){
		send(runnable, false);
	}
	
	default <V> V send(Callable<V> callable, Boolean wait){
		return MainPool.getInstance().sendTask(callable, wait);
	}
	
	default void send(Runnable runnable, Boolean wait){
		MainPool.getInstance().sendTask(runnable, wait);
	}
	
	default <R> List<R> sendTasks(List<Callable<R>> callables){
		return MainPool.getInstance().sendTasks(callables);
	}
	
}
