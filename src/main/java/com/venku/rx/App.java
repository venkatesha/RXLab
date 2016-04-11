package com.venku.rx;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * 
 * @author Venkatesha Chandru
 * 
 */
public class App {

    public static Observable<Integer> getIds() {
        List<Integer> ts = Arrays.asList(1, 2);
        return Observable.defer(() -> Observable.from(ts)).subscribeOn(Schedulers.io());
    }

    public static Observable<String> getNames(Integer id) {
        Map<Integer, List<String>> names = new HashMap<>();
        names.put(1, Arrays.asList("A", "B", "C"));
        names.put(2, Arrays.asList("a", "b", "c"));

        List<String> strings = names.get(id);
        return Observable.defer(() -> Observable.from(strings)).subscribeOn(Schedulers.io());
    }

    public static Observable<String> getSummaries(String name) {
        Map<String, List<String>> summaries = new HashMap<>();
        summaries.put("A", Arrays.asList("AA", "AAA"));
        summaries.put("B", Arrays.asList("BB", "BBB"));
        summaries.put("C", Arrays.asList("CC", "CCC"));
        summaries.put("a", Arrays.asList("aa", "aaa"));
        summaries.put("b", Arrays.asList("bb", "bbb"));
        summaries.put("c", Arrays.asList("cc", "ccc"));

        List<String> strings = summaries.get(name);
        return Observable.defer(() -> Observable.from(strings)).subscribeOn(Schedulers.io());
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Start...");
        getIds().flatMap(id -> getNames(id).flatMap(name -> getSummaries(name))).forEach(item -> {
            System.out.println(Thread.currentThread() + "[Async] get " + item);
        });

        System.in.read();
    }
}
