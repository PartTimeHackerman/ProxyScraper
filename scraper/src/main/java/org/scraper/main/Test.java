package org.scraper.main;

import rx.Notification;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Test {
	
	static Integer test = 0;
	
	public static void main(String[] args) throws InterruptedException {
		
		AtomicBoolean b = new AtomicBoolean(true);
		
		b.set(false);
		
		
		Interval.addFunc("test", () -> test, var -> System.out.println(Thread.currentThread().getName() +" :: "+var));
		
		Observable<String> interval = Observable.interval(0, 1000, TimeUnit.MILLISECONDS)
				.map(v -> ""+v);
		
		
		Observable.interval(500, TimeUnit.MILLISECONDS).take(5).doOnEach(threadDebug()).publish().connect();
		
		Observable<String> published = interval.share();
		
		Subject<String, String> subject = PublishSubject.create();
		ReplaySubject<String> replaySubject = ReplaySubject.create();
		BehaviorSubject<String> behaviorSubject = BehaviorSubject.create();
		BehaviorSubject<String> behaviorSubject2 = BehaviorSubject.create();
		
		
		
		behaviorSubject.onNext("1");
		behaviorSubject2.onNext("1");
		
		Observable<String> sum = Observable.combineLatest(behaviorSubject,behaviorSubject2, (v1,v2)->v1+v2);
		print(sum, "sum");
		
		behaviorSubject.onNext("2");
				
		interval.subscribe(subject);
		
		//Observable autoConnect = published.autoConnect(1);
		Subscription first = print(subject,"interval first");
		subject.onNext("first subbed");
		Thread.sleep(2000);
		Interval.setInterval(100);
		
		behaviorSubject.onNext("8");
		Subscription second = print(subject,"interval second");
		subject.onNext("second subbed");
		Thread.sleep(2000);
		Interval.setInterval(1000);
		first.unsubscribe();
		subject.onNext("first unsubbed");
		Thread.sleep(2000);
		Interval.setInterval(100);
		second.unsubscribe();
		subject.onNext("second unsubbed");
		Thread.sleep(2000);
		Interval.setInterval(1000);
		Subscription third = print(subject,"interval third");
		subject.onNext("third subbed");
		subject.onNext("two second to end");
		Thread.sleep(2000);
		Interval.setInterval(100);
		subject.onCompleted();
		
		
		
		for (; ; ) {
			Thread.sleep(100);
			test++;
		}
		
	}
	
	
	public static <T> Subscription print(Observable<T> observable, String name){
		return observable.subscribe(val -> System.out.println(name + " : "+val), err->{}, () -> System.out.println("done"));
	}
	
	public static <T> Action1<Notification<? super T>> threadDebug(){
		return notification -> {
			switch (notification.getKind()) {
				case OnNext:
					System.out.println(
							Thread.currentThread().getName() +
									"|"  + notification.getValue()
									  );
					break;
				case OnError:
					System.err.println(
							Thread.currentThread().getName() +
									"|" + " X " + notification.getThrowable()
									  );
					break;
				case OnCompleted:
					System.out.println(
							Thread.currentThread().getName() +
									"|" + "|"
									  );
				default:
					break;
			}};
	}
	
}
