package scraper;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;

public interface IPool {
	
	void sendTask(Runnable lambda, boolean wait);
	
	void sendTaskOrRunIfFull(Runnable lambda, boolean wait);
	
	<R> R sendTask(Callable<R> callable, boolean wait);
	
	<R> R sendTaskOrRunIfFull(Callable<R> callable, boolean wait);
	
	<R> List<R> sendTasks(List<Callable<R>> callables);
	
	<R> List<R> sendTasksOrRunIfFull(List<Callable<R>> callables);
	
	<R> List<R> forEach(List<R> list, Function<R, R> consumer);
	
	ThreadPoolExecutor getExecutor();
	
	Integer getThreads();
	
	void setThreads(Integer threads);
	
	Integer getActiveThreads();
}
