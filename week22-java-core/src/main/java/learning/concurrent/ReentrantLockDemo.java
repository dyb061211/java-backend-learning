package learning.concurrent;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {
    static class Counter{
        int count=0;
        private final ReentrantLock lock = new ReentrantLock();
        void increment() {
            lock.lock();
            try{
                count++;
            }
            finally{
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        Runnable task = () -> {
            for (int i = 0; i < 100000; i++) {
                counter.increment();
            }
        };

        Thread t1 = new Thread(task, "worker-1");
        Thread t2 = new Thread(task, "worker-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("预期结果：200000");
        System.out.println("实际结果：" + counter.count);
    }
}
