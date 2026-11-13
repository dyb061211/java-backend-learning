package learning.concurrent;

import java.util.concurrent.*;

public class CompletableFutureCancelDemo {
    public static void main(String[] args) throws Exception {
        CountDownLatch started=new CountDownLatch(1);
        CountDownLatch finished=new CountDownLatch(1);
        try(ExecutorService pool=Executors.newFixedThreadPool(1)) {
            CompletableFuture<String>future=CompletableFuture.supplyAsync(()->{
                try{
                    System.out.println("worker：任务开始");
                    started.countDown();
                    Thread.sleep(600);
                    System.out.println("worker：实际执行完成");
                    return "商品处理成功";
                }catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("任务中断", e);
                }finally{
                    System.out.println("worker：执行清理");
                    finished.countDown();
                }
            },pool);
            if(!started.await(2,TimeUnit.SECONDS)){
                System.out.println("任务启动超时");
                future.cancel(true);
                return;
            }
            System.out.println("main：cancel(true) = " + future.cancel(true));
            System.out.println("main：isCancelled = " + future.isCancelled());
            try{
                System.out.println(future.get());
            }catch(CancellationException e) {
                System.out.println("main：Future已经取消");
            }
            System.out.println("main：已观察到清理 = " + finished.await(2, TimeUnit.SECONDS));
        }
        System.out.println("线程池已关闭");
    }
}
