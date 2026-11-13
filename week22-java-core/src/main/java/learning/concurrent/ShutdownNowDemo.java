package learning.concurrent;

import java.util.List;
import java.util.concurrent.*;

public class ShutdownNowDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch started=new CountDownLatch(1);
        CountDownLatch gate=new CountDownLatch(1);
        try(ExecutorService pool=Executors.newFixedThreadPool(1)) {
            pool.execute(()->{
                try{
                    System.out.println("任务1：开始");
                    started.countDown();
                    gate.await(2, TimeUnit.SECONDS);
                    System.out.println("任务1：正常结束");
                }catch(InterruptedException e){
                    System.out.println("任务1：收到中断");
                    Thread.currentThread().interrupt();
                }finally{
                    System.out.println("任务1：执行清理");
                }
                    }
            );
            if(!started.await(1,TimeUnit.SECONDS)){
                System.out.println("任务1启动超时");
                pool.shutdownNow();
                return;
            }
            pool.execute(() -> System.out.println("任务2：执行"));
            pool.execute(() -> System.out.println("任务3：执行"));
            System.out.println("main：请求 shutdownNow");
            List<Runnable>notStarted=pool.shutdownNow();
            System.out.println("未开始的任务数量：" + notStarted.size());
            boolean terminated=pool.awaitTermination(2,TimeUnit.SECONDS);
            System.out.println("线程池已终止：" + terminated);
        }
        System.out.println("main：程序结束");
    }
}
