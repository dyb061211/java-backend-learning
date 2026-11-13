package learning.concurrent;

import java.util.concurrent.*;

public class FutureCancelDemo {
    static void runCase(boolean interrupt) throws InterruptedException {
        CountDownLatch started=new CountDownLatch(1);
        CountDownLatch finished=new CountDownLatch(1);
        try(ExecutorService pool=Executors.newFixedThreadPool(1)) {
            Future<?> future=pool.submit(()->{
                try{
                    System.out.println("worker：任务开始");
                    started.countDown();
                    Thread.sleep(600);
                    System.out.println("worker：正常完成");
                }catch(InterruptedException e) {
                    System.out.println("worker：响应中断");
                    Thread.currentThread().interrupt();
                }finally{
                    System.out.println("worker：执行清理");
                    finished.countDown();
            }
            }
            );
            if(!started.await(2,TimeUnit.SECONDS)) {
                future.cancel(true);
                System.out.println("任务启动超时");
                return;
            }
            boolean cancelled = future.cancel(interrupt);
            System.out.println("main：cancel(" + interrupt + ") = " + cancelled);
            System.out.println("main：isCancelled = " + future.isCancelled());
            boolean exited=finished.await(2,TimeUnit.SECONDS);
            System.out.println("main：已观察到清理 = " + exited);
        }
        System.out.println("main：线程池已经关闭");
    }

    public static void main(String[] args) throws InterruptedException {

        System.out.println("=== 实验A ===");
        runCase(false);

        System.out.println("=== 实验B ===");
        runCase(true);
    }
}
