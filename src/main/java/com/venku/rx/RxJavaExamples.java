package com.venku.rx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.functions.Func2;

public class RxJavaExamples {

    public static void main(String[] args) {
        new RxJavaExamples().zip();

        new RxJavaExamples().zip2();

        new RxJavaExamples().map();

        new RxJavaExamples().take();

        new RxJavaExamples().filter();

        new RxJavaExamples().reduce();
    }

    // This does not use lambda. See zip2 for use of lambda.
    public void zip() {
        Observable<Integer> obs1 = Observable.from(Arrays.asList(1, 3, 5, 7, 9));
        Observable<Integer> obs2 = Observable.from(Arrays.asList(2, 4, 6));

        Observable<List<Integer>> obs = Observable.zip(obs1, obs2, new Func2<Integer, Integer, List<Integer>>() {
            @Override
            public List<Integer> call(Integer value1, Integer value2) {
                List<Integer> list = new ArrayList<Integer>();
                list.add(value1);
                list.add(value2);

                return list;
            }
        });

        obs.subscribe(new Observer<List<Integer>>() {
            @Override
            public void onCompleted() {
                System.out.println("completed");
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("failure");
            }

            @Override
            public void onNext(List<Integer> value) {
                System.out.println("onnext=" + value);
            }
        });

    }

    public void zip2() {
        Observable<Integer> obs1 = Observable.just(1, 3, 5, 7, 9);
        Observable<Integer> obs2 = Observable.just(2, 4, 6);

        Observable<List<Integer>> obs = Observable
                .zip(obs1, obs2, (value1, value2) -> {
                    List<Integer> list = new ArrayList<Integer>();
                    list.add(value1);
                    list.add(value2);

                    return list;
                });

        obs.subscribe((value) -> {
            System.out.println("SubscribeValue = " + value);
        });
    }

    // MapValue = 1
    // SubscribeValue = 1aaa
    // MapValue = 2
    // SubscribeValue = 2aaa
    // MapValue = 3
    // SubscribeValue = 3aaa
    private void map() {
        Observable<Integer> obs = Observable.just(1, 2, 3);

        Observable<String> map = obs.map(value -> {
            System.out.println("MapValue = " + value);
            return value + "aaa";
        });

        map.subscribe((value) -> {
            System.out.println("SubscribeValue = " + value);
        });
    }

    // one
    // two
    private void take() {
        Observable<String> obs = Observable.just("one", "two", "three");
        Observable<String> take2 = obs.take(2);

        take2.subscribe(arg -> {
            System.out.println(arg);
        });

    }

    // SubscribeValue = 1
    // SubscribeValue = 2
    // SubscribeValue = 3
    private void filter() {
        Observable.just(1, 2, 3, 4, 5)
                .filter(v -> {
                    return v < 4;
                })
                .subscribe(value -> {
                    System.out.println("SubscribeValue: " + value);
                });
    }

    // reduce-seed = 1
    // reduce-value = 2
    //
    // reduce-seed = 3 (from 1 + 2)
    // reduce-value = 3
    //
    // reduce-seed = 6 (from 3 + 3)
    // reduce-value = 4
    //
    // reduce-seed = 10 (from 6 + 4)
    // reduce-value = 5
    //
    // map-v = 15 (from 10 + 5)
    // subscribe-value = MapValue: 15
    private void reduce() {
        Observable.just(1, 2, 3, 4, 5)
                .reduce((seed, value) -> {
                    // sum all values from the sequence
                    System.out.println("reduce-seed = " + seed);
                    System.out.println("reduce-value = " + value);
                    return seed + value;
                })
                .map(v -> {
                    System.out.println("map-v = " + v);
                    return "MapValue: " + v;
                })
                .subscribe(value -> {
                    System.out.println("subscribe-value = " + value);
                });
    }

}
