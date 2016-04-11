package com.venku.rx;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.google.common.base.Stopwatch;

import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observers.TestSubscriber;

/**
 * Some reactive implementations of traditional first program.
 * <p>
 * Given a list of strings, join with space, then append '!'.<p>
 * Like: String.join(" ", "Hello","world") + "!"
 * 
 * @version 1.1
 * @author jbetancourt
 */
public class HelloRxTest {

    private Observable<String> observable;
    private Exception thrown;
    private StringBuilder buf;
    private static final String SPACE = " ";

    /** Setup test fixture */
    @Before
    public void before() {
        observable = Observable.from(Arrays.asList("Hello", "world"));
        buf = new StringBuilder();
    }

    /** Correct result string? */
    private void assertHello() {
        assertThat(buf.toString(), is("Hello world!"));
    }

    /**
     * Simple imperative solution.
     */
    @Test
    public final void nonObservableApproach() {
        buf.append(String.join(SPACE, "Hello", "world")).append("!");
        assertHello();
    }

    /**
     * Using reduce.
     */
    @Test
    public final void test1() {
        observable.reduce((t1, t2) -> t1 + " " + t2)
                .subscribe((s) -> buf.append(s).append("!"));

        assertHello();
    }

    /**
     * Concatting concats. Meow
     */
    @SuppressWarnings("static-access")
    @Test
    public final void test2() {
        observable.concat(
                observable.concatMap(
                        data -> Observable.from(Arrays.asList(data, SPACE))).skipLast(1),
                Observable.just("!"))
                .subscribe((s) -> buf.append(s));

        assertHello();
    }

    /**
     * Using concatMap.
     */
    @Test
    public final void test3() {
        observable.concatMap(data -> Observable.from(new String[] { data, SPACE }))
                .skipLast(1)
                .subscribe(
                        data -> {
                            buf.append(data);
                        } ,
                        (e) -> {
                            thrown = new RuntimeException(((Throwable) e).getMessage());
                        } ,
                        () -> buf.append("!"));

        assertHello();
    }

    /**
     * Concatting zip<p>
     * Doesn't work!
     */
    @Test(expected = AssertionError.class)
    public final void test2b() {
        Observable.concat(
                Observable.zip(
                        observable,
                        Observable.just(SPACE).repeat(),
                        (s1, s2) -> s1 + s2),
                Observable.just("!"))
                .subscribe(s -> buf.append(s));

        assertHello();
    }

    /**
     * Dan Lew's example in 'Grokking RxJava, Part 1: The Basics'.<p>
     * Converted to a test.
     * @see "http://blog.danlew.net/2014/09/15/grokking-rxjava-part-1/"
     * @throws Exception
     */
    @Test
    public final void test6() throws Exception {
        TestSubscriber<String> testSubscriber = new TestSubscriber<>();

        Observable.just("Hello, world!")
                .map(s -> s + " -Dan")
                .map(s -> s.hashCode())
                .map(i -> Integer.toString(i))
                .subscribe(testSubscriber);
        // .subscribe(s -> System.out.println(s)); // -

        testSubscriber.assertValue("-238955153");
        testSubscriber.assertTerminalEvent();
        testSubscriber.assertNoErrors();
    }

    /**
     * Concatting zip<p>
     * Doesn't work!
     */
    @Test(expected = AssertionError.class)
    public final void test2c() {
        Observable.concat(
                Observable.zip(
                        observable,
                        Observable.just(SPACE).repeat(),
                        (a, b) -> a + b),
                Observable.just("!"))
                .subscribe((a) -> buf.append(a));

        assertHello();
    }

    /**
     * Fail test within an onCompleted.
     * Doesn't work!
     * 
     * Also, how to use a lambda for subscribe? The onError(e) is 
     * not visible.
     * 
     * @throws Exception
     */
    @Test(expected = RuntimeException.class)
    public final void test4() throws Exception {
        Action1<String> appender = data -> buf.append(data).append(SPACE);
        Action1<Throwable> errorFunction = (e) -> thrown = new RuntimeException(e.getMessage(), e);
        Action0 complete = () -> {
            try {
                String actual = buf.append("!").toString();
                Assert.assertEquals("Hello world!", actual);
            } catch (Throwable e) {
                thrown = new RuntimeException(e);
            }
        };

        observable.subscribe(appender, errorFunction, complete);

        if (null != thrown) {
            System.out.println("failed 4");
            throw thrown;
        }

    }

    /**
     * @param o
     * @return observable
     */
    public static <T> Observable<T> handleTestError(Observable<T> o) {
        return o.onErrorResumeNext(new Func1<Throwable, Observable<T>>() {
            @Override
            public Observable<T> call(Throwable err) {
                System.out.println("in error stuff ...");
                return Observable.error(err);
            }
        });
    }

    /**
     * Another approach that fails.
     * Doesn't work!
     * @throws Exception
     */
    @Test(expected = RuntimeException.class)
    public final void test5() throws Exception {
        observable.subscribe(
                (s) -> {
                    buf.append(s).append(SPACE);
                } ,
                (e) -> {
                    thrown = new RuntimeException(((Throwable) e).getMessage());
                } ,
                () -> buf.append("!"));

        if ("Hello world!".compareTo(buf.toString()) != 0) {
            System.out.println("failed 5");
            throw thrown;
        }
    }

    /**
     * Print name and elapsed time for each test.
     * @see "http://stackoverflow.com/a/32460841/1149606" 
     */
    @Rule
    public TestRule watcher = new TestWatcher() {
        private Stopwatch stopwatch = new Stopwatch();

        @Override
        protected void starting(Description description) {
            stopwatch.start();
            System.out.println("[[[<==== Start: [" + description.getTestClass().getSimpleName() + "."
                    + description.getMethodName() + "]");
        }

        @Override
        protected void finished(Description description) {
            long timeEnd = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            System.out
                    .println("[[[>==== End: [" + description.getMethodName() + "] Elapsed: " + timeEnd + " ms");

        }
    };
}