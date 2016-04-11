package com.venku.rx.basics;

import java.util.concurrent.TimeUnit;

import rx.Observable;

/**
 * 
 * @author Venkatesha Chandru
 *
 */
public class RxMap {

    public static void fizzBuzz() {
        Observable.interval(1, TimeUnit.SECONDS)
                .take(20)
                .map(value -> {
                    if (value % 3 == 0) {
                        return "Fizz";
                    } else if (value % 5 == 0) {
                        return "Buzz";
                    }

                    return Long.toString(value);
                }).toBlocking().forEach(System.out::println);
    }

    public static void main(String[] args) {
        fizzBuzz();
    }
}
