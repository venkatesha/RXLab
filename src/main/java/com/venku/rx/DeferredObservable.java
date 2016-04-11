package com.venku.rx;

import rx.Observable;

/**
 * 
 * @author Venkatesha Chandru
 *
 */
public class DeferredObservable {

    public static void main(String[] args) {
        NonDeferredObservable instance = new NonDeferredObservable();
        Observable<String> value = instance.valueObservable();
        instance.setValue("Some Value");
        value.subscribe(System.out::println);

        DeferredObservable deferred = new DeferredObservable();
        Observable<String> observable = deferred.valueObservable();
        deferred.setValue("Some Value");
        observable.subscribe(System.out::println);
    }

    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * New observable is created for each subscription and emits stream only after subscription
     *
     * @return the observable
     */
    public Observable<String> valueObservable() {
        return Observable.defer(() -> Observable.just(value));
    }

    /**
     * NonDeferredObservable - Here observable starts emitting stream as soon as it is created.
     *
     * @author Venkatesha Chandru
     */
    public static class NonDeferredObservable {
        private String value;

        public void setValue(String value) {
            this.value = value;
        }

        public Observable<String> valueObservable() {
            return Observable.just(value);
        }

    }
}
