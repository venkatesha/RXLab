package com.venku.rx.basics;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * 
 * @author Venkatesha Chandru
 *
 */
public class RxError {

    public static void main(String[] args) throws InterruptedException {
        error();
        errorRetry();
        errorRetryPause();

        // Thread.sleep(10000);
    }

    public static void error() {
        // Prints:
        // Default
        // Oops: I don't like: Apples
        Observable
                .just("Apples", "Bananas")
                .doOnNext(s -> {
                    throw new RuntimeException("I don't like: " + s);
                })
                .onErrorReturn(throwable -> {
                    System.err.println("Oops: " + throwable.getMessage());
                    return "Default";
                }).subscribe(System.out::println);
    }

    public static void errorRetry() {
        Observable
                .just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .doOnNext(integer -> {
                    if (new Random().nextInt(10) + 1 == 5) {
                        throw new RuntimeException("Boo!");
                    }
                })
                .retry() // If you only want to retry for a max amount, replace the retry() with a retry(count) call.
                .distinct()
                .subscribe(System.out::println);
    }

    public static void errorRetryPause() {
        Observable
                .range(1, 10)
                .doOnNext(integer -> {
                    if (new Random().nextInt(10) + 1 == 5) {
                        throw new RuntimeException("Boo!");
                    }
                })
                .retryWhen(attempts -> attempts.zipWith(Observable.range(1, 3), (n, i) -> i)
                        .flatMap(i -> {
                            System.out.println("delay retry by " + i + " second(s)");
                            return Observable.timer(i, TimeUnit.SECONDS);
                        }))
                .distinct()
                .toBlocking()
                .forEach(System.out::println);
        // .subscribe(System.out::println);
    }

}
