
package learning.concurrent;

import java.util.concurrent.CountDownLatch;

public class ThreadInterruptDemo {

    public static void main(String[] args) throws InterruptedException {

        CountDownLatch started = new CountDownLatch(1);

        Thread worker = new Thread(() -> {
            try {
                System.out.println("worker：开始执行");
                started.countDown();

                Thread.sleep(1000);

                System.out.println("worker：正常完成");

            } catch (InterruptedException e) {
                System.out.println("worker：收到中断");
                Thread.currentThread().interrupt();
                return;

            } finally {
                System.out.println("worker：执行清理");
            }
        }, "worker");

        worker.start();

        // 确认工作线程已经开始
        started.await();

        // main 等待200ms
        Thread.sleep(200);

        System.out.println("main：请求中断");
        worker.interrupt();

        // 最多等待2秒确认退出
        worker.join(2000);

        System.out.println(
                "main：worker是否仍存活：" + worker.isAlive()
        );
    }
}
