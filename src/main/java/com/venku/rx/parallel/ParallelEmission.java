package com.venku.rx.parallel;

import java.util.Random;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * The Class ParallelEmission.
 *
 * @author Venkatesha Chandru
 */
public class ParallelEmission {

    private static final Random RAND = new Random();

    public static void main(String... values) {
        Observable<Integer> observable = Observable.range(1, 20);

        observable.flatMap(
                value -> Observable.just(value)
                        .subscribeOn(Schedulers.computation())
                        .map(i -> intenseCalculation(i)))
                .subscribe(val -> System.out
                        .println("Subscriber received " + val + " on " + Thread.currentThread().getName()));

        waitSleep();
    }

    private static int intenseCalculation(Integer i) {
        try {
            System.out.println("Calculating " + i +
                    " on " + Thread.currentThread().getName());
            Thread.sleep(randInt(1000, 5000));
            return i;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static int randInt(int min, int max) {
        return RAND.nextInt((max - min) + 1) + min;
    }

    public static void waitSleep() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
