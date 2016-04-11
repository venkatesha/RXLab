package com.venku.rx.basics;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.observables.BlockingObservable;

/**
 * check -> http://docs.couchbase.com/developer/java-2.0/observables.html
 * 
 * @author Venkatesha Chandru
 *
 */
public class RxBlockingObservable {

    public static void main(String[] args) {
        blockedAsync();
        // asyncForever();
        // emitSingle();
        // singleOrDefault();
    }

    public static void blockedAsync() {
        BlockingObservable<Long> observable = Observable.interval(1, TimeUnit.SECONDS).take(5).toBlocking();
        observable.forEach(System.out::println);
    }

    public static void asyncForever() {
        BlockingObservable<Long> observable = Observable.interval(1, TimeUnit.SECONDS).toBlocking();
        observable.forEach(System.out::println);
    }

    public static void emitSingle() {
        int value = Observable.just(1).toBlocking().single();
        System.out.println(value);
    }

    public static void singleOrDefault() {
        String name = Observable.just("Name").filter(nam -> nam.equalsIgnoreCase("n")).toBlocking()
                .singleOrDefault("noName");
        System.out.println(name);
    }
}
