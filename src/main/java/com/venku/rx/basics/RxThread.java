package com.venku.rx.basics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 
 * @author Venkatesha Chandru
 *
 */
public class RxThread {

    public static void main(String[] args) throws InterruptedException {
        // schedulerThreading();
        // mainThreading();

        // observeOn();

        // obsSubon();

        timer();

        Thread.sleep(2000);
    }

    public static void mainThreading() {
        Observable
                .range(1, 5)
                .map(integer -> {
                    System.out.println("Map: (" + Thread.currentThread().getName() + ")");
                    return integer + 2;
                })
                .subscribe(integer -> System.out
                        .println("Got: " + integer + " (" + Thread.currentThread().getName() + ")"));
    }

    public static void schedulerThreading() {
        Observable
                .range(1, 5)
                .map(integer -> {
                    System.out.println("Map: (" + Thread.currentThread().getName() + ")");
                    return integer + 2;
                })
                .subscribeOn(Schedulers.computation())
                .subscribe(integer -> System.out
                        .println("Got: " + integer + " (" + Thread.currentThread().getName() + ")"));
    }

    /**
     * Observe on.
     * If you need tighter control which parts are executed on what pool, use observeOn(). Here, the order matters:
     * 
     */
    public static void observeOn() {
        Observable
                .range(1, 5)
                .map(integer -> {
                    System.out.println("Map: (" + Thread.currentThread().getName() + ")");
                    return integer + 2;
                })
                .observeOn(Schedulers.computation())
                .subscribe(integer -> System.out
                        .println("Got: " + integer + " (" + Thread.currentThread().getName() + ")"));
    }

    public static void obsSubon() {
        Observable.create(sub -> {
            sub.onNext("onNext(" + Thread.currentThread().getName() + ")");
            sub.onCompleted();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(value -> "map(" + Thread.currentThread().getName() + value + ")")
                .subscribe(value -> {
                    System.out.println("subscribe(" + Thread.currentThread().getName() + "," + value);
                });
    }

    public static void timer() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(50);
        Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long counter) {
                        latch.countDown();
                        System.out.println("Got: " + counter + " - Thread : " + Thread.currentThread().getName());
                    }
                });
        latch.await();
    }
}
