package com.venku.rx.basics;

import java.util.Arrays;
import java.util.List;

import rx.Observable;

/**
 * https://github.com/meddle0x53/learning-rxjava
 *  
 * @author Venkatesha Chandru
 *
 */
public class RxBasics {

    public static void main(String[] args) {
        List<String> list = Arrays.asList(new String[] { "One", "Two", "Three", "Four", "Five" });

        Observable<String> observable = Observable.from(list);

        observable.subscribe(string -> {
            System.out.println(string);
            if (string.equals("Four")) {
                // throw new RuntimeException("Throw");
            }
        } ,
                thro -> {
                    System.err.println(thro);
                } ,
                RxBasics::finish);

        System.out.println("Main thread");
    }

    public static void finish() {
        System.out.println("Finished");
    }
}
