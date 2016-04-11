package com.venku.rx;

import java.util.Arrays;

import rx.Observable;

/**
 * Example for flatMap
 * 
 * @author Venkatesha Chandru
 * 
 */
public class HelloRxJava {

    public static void main(String[] args) {
        Observable.from(Arrays.asList("Ms.Alice", "Mr.Ben", "Mr.Carlos", "Mr.Dylan", "Ms.Ellie"))
                .map(String::toUpperCase)
                .flatMap(name -> Observable.from(name.split("\\.")))
                .filter(s -> {
                    System.out.println("Filter : " + s);
                    return !(s.equals("MR") || s.equals("MS"));
                })
                .take(3)
                .subscribe(System.out::println);
    }
}
