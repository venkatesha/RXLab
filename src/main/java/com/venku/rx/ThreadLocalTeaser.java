package com.venku.rx;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author Venkatesha Chandru
 *
 */
public class ThreadLocalTeaser {

    ExecutorService service = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        Thread t1 = new Thread(getTask("Neo"));
        Thread t2 = new Thread(getTask("Leo"));

        t1.start();
        t2.start();
    }

    static class Counter {
        private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

        static {
            threadLocal.set("Thread-Nil");
        }

        public static void setName(String name) {
            threadLocal.set(name);
        }

        public static String getName() {
            return threadLocal.get();
        }

        public static void unset() {
            threadLocal.remove();
        }
    }

    public static Runnable getTask(final String name) {
        return () -> {
            Counter.setName(name);
            new UserService().createUser();
            Counter.unset();
        };
    }

    public static class UserService {

        public void createUser() {
            System.out.println(Counter.getName());
        }
    }

}
