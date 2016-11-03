package org.scraper.model;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Interval {
	
	final static private HashMap<String, Callable> vars = new HashMap<>();
	final static private HashMap<String, List<Action1>> actions = new HashMap<>();
	final static private BehaviorSubject<Long> subject = BehaviorSubject.create(500L);
	
	static {
		subject.switchMap(inter ->
								  Observable.interval(inter, TimeUnit.MILLISECONDS)
										  .publish()
										  .refCount())
				.subscribe(getSubscriber());
	}
	
	public static void setInterval(long interval) {
		subject.onNext(interval);
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

