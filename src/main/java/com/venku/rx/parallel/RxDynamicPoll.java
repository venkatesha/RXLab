package com.venku.rx.parallel;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * 
 * @author Venkatesha Chandru
 * 
 */
public class RxDynamicPoll {

    public static void main(String[] args) {
        BehaviorSubject<Integer> timerSubject = BehaviorSubject.create(1);

        timerSubject.switchMap(interval -> Observable.timer(interval, TimeUnit.SECONDS, Schedulers.newThread()))
                .flatMap(i -> Observable.just(1))
                .doOnNext(timerSubject::onNext)
                .subscribe(System.out::println);
    }
}
