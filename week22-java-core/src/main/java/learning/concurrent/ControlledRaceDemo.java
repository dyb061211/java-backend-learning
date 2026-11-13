
package learning.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ControlledRaceDemo {
    static class Counter {
        int count = 0;
    }

    public static void main(String[] args) throws InterruptedException {

        Counter counter = new Counter();

        // 两个线程都完成读取后，计数变成0
        CountDownLatch bothRead = new CountDownLatch(2);

        // 主线程允许后，两个线程才能写回
        CountDownLatch allowWrite = new CountDownLatch(1);

        Runnable task = () -> {
            try {
                int oldValue = counter.count;

                System.out.println(
                        Thread.currentThread().getName()
                                + " 读取：" + oldValue
                );

                bothRead.countDown();

                // 暂停在这里，等待主线程放行
                allowWrite.await();

                counter.count = oldValue + 1;

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread t1 = new Thread(task, "worker-1");
        Thread t2 = new Thread(task, "worker-2");

        t1.start();
        t2.start();

        try {
            // 最多等待2秒，确认两个线程完成读取
            if (!bothRead.await(2, TimeUnit.SECONDS)) {
                System.out.println("等待读取超时");
                return;
            }

            System.out.println("两个线程都读取完成");

        } finally {
            // 即使等待失败，也释放工作线程
            allowWrite.countDown();
            t1.join();
            t2.join();
        }

        System.out.println("预期结果：2");
        System.out.println("实际结果：" + counter.count);
    }
}
