package com.venku.rx.basics;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.observables.BlockingObservable;

/**
 * The Class CreateObservable.
 *
 * @author Venkatesha Chandru
 */
public class CreateObservable {

    public static Observable<Long> create() {
        return Observable.create(subscriber -> {
            try {
                System.out.println("Thread creating observable - " + Thread.currentThread().getName());
                if (!subscriber.isUnsubscribed()) {
                    BlockingObservable<Long> obs = Observable.interval(1, TimeUnit.SECONDS).take(5).toBlocking();
                    obs.forEach(value -> {
                        System.out.println("for each thread - " + Thread.currentThread().getName());
                        subscriber.onNext(value);
                    });
                }
                subscriber.onCompleted();
            } catch (Exception e) {
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onError(e);
                }
            }
        });
    }

    public static void printNameList() {
        Observable.just("The", "Dave", "Brubeck", "Quartet", "Time", "Out")
                .take(5)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("Completed Observable.");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.err.println("Whoops: " + throwable.getMessage());
                    }

                    @Override
                    public void onNext(String name) {
                        System.out.println("Got: " + name + " - " + Thread.currentThread().getName());
                    }
                });
    }

    public static void asyncObservable() {
        Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long counter) {
                        System.out.println("Got: " + counter);
                    }
                });
    }

    public static void main(String[] args) {
        create().subscribe(CreateObservable::print, System.err::println);
        printNameList();
        asyncObservable();
    }

    public static void print(Long value) {
        System.out.println("Value - " + value + " - " + Thread.currentThread().getName());
    }
}
