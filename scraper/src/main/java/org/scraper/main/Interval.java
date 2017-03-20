package org.scraper.main;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Interval {
	
	private static Long interval = 500L;
	private static final HashMap<String, Callable> vars = new HashMap<>();
	private static final HashMap<String, List<Action1>> actions = new HashMap<>();
	private static final BehaviorSubject<Long> subject = BehaviorSubject.create(interval);
	
	
	public static void start() {
		subject.switchMap(inter ->
								  Observable.interval(0, inter, TimeUnit.MILLISECONDS)
										  .publish()
										  .refCount())
				.subscribe(getSubscriber());
	}
	
	public static void setInterval(long interval) {
		subject.onNext(interval);
		Interval.interval = interval;
	}
	
	public static Long getInterval(){
		return interval;
	}
	
	private static <T> Subscriber<T> getSubscriber(){
		return new Subscriber<T>() {
			@Override
			public void onCompleted() {
				
			}
			
			@Override
			public void onError(Throwable e) {
				
			}
			
			@Override
			public void onNext(Object o) {
				
				vars.entrySet()
						.forEach(varEntry -> {
							List<Action1> actionsList;
							if ((actionsList = actions.get(varEntry.getKey())) != null) {
								actionsList.forEach(action -> {
									try {
										action.call(varEntry.getValue().call());
									} catch (Exception e) {
										e.printStackTrace();
									}
								});
							}
						});
			}
		};
	}
	
	public static void addFunc(String name, Callable var) {
		vars.put(name, var);
	}
	
	public static void addFunc(String name, Callable var, Action1 action) {
		addFunc(name, var);
		addAction(name, action);
	}
	
	public static void addAction(String name, Action1 action) {
		if (actions.containsKey(name)) {
			actions.get(name).add(action);
		} else {
			actions.put(name, Arrays.asList(action));
		}
	}
	
}

