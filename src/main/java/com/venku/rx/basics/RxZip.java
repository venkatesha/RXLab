package com.venku.rx.basics;

import org.springframework.util.StopWatch;

import javaslang.control.Try;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Demo of {@link Observable#zip(Observable, rx.functions.FuncN)}
 *
 * @author Venkatesha Chandru
 */
public class RxZip {

    public static void zip() {
        Observable<Integer> evens = Observable.just(2, 4, 6, 8, 10);
        Observable<Integer> odds = Observable.just(1, 3, 5, 7, 9);

        Observable
                .zip(evens, odds, (v1, v2) -> v1 + " + " + v2 + " is: " + (v1 + v2)) // .toList()
                .subscribe(System.out::println);

        StopWatch watch = new StopWatch();
        watch.start("Zip");
        Observable.zip(getAccountHolderName(),
                getAccountBalance().subscribeOn(Schedulers.io()),
                getAccountNumber().subscribeOn(Schedulers.io()),
                getAccountAddress().subscribeOn(Schedulers.io()),
                isActive().subscribeOn(Schedulers.io()),
                (name, balance, number, address, active) -> {
                    return String.join("-", name, balance + "", number + "", address, "" + active);
                })
                .toBlocking()
                .forEach(System.out::println);
        watch.stop();
        System.out.println("Total Time Zip -> " + watch.getTotalTimeMillis());
    }

    public static void main(String[] args) {
        zip();
        merge();
    }

    public static void merge() {
        StopWatch watch = new StopWatch();
        watch.start("Merge");
        Observable.merge(getAccountHolderName().subscribeOn(Schedulers.io()),
                getAccountBalance().subscribeOn(Schedulers.io()),
                getAccountNumber().subscribeOn(Schedulers.io()),
                getAccountAddress().subscribeOn(Schedulers.io()),
                isActive().subscribeOn(Schedulers.io()))
                .toBlocking()
                .forEach(System.out::println);
        watch.stop();
        System.out.println("Total Time Merge -> " + watch.getTotalTimeMillis());
    }

    public static Observable<String> getAccountHolderName() {
        return Observable.defer(() -> {
            sleep();
            return Observable.just("House");
        });
    }

    public static Observable<Integer> getAccountBalance() {
        return Observable.defer(() -> {
            sleep();
            return Observable.just(1000);
        });
    }

    public static Observable<Long> getAccountNumber() {
        return Observable.defer(() -> {
            sleep();
            return Observable.just(123459794L);
        });
    }

    public static Observable<String> getAccountAddress() {
        return Observable.defer(() -> {
            sleep();
            return Observable.just("Bengaluru");
        });
    }

    public static Observable<Boolean> isActive() {
        return Observable.defer(() -> {
            sleep();
            return Observable.just(true);
        });
    }

    public static void sleep() {
        System.out.println("Sleeping thread - " + Thread.currentThread().getName());
        Try.run(() -> Thread.sleep(10));
    }

    public static void sleep(long value) {
        Try.run(() -> Thread.sleep(value));
    }
}
