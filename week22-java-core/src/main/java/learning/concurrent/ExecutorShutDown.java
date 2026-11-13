package learning.concurrent;

import java.util.concurrent.*;

public class ExecutorShutDown {
    public static void main(String[] args) throws InterruptedException {
        try(ExecutorService pool=Executors.newFixedThreadPool(1)) {
            for(int i=1;i<=2;++i) {
                int taskId=i;
                pool.execute(()->{
                    System.out.println("任务" + taskId + "开始");
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    System.out.println("任务" + taskId + "完成");
                        }

                );
            }
            pool.shutdown();
            System.out.println("main：shutdown已返回");
            System.out.println("isShutdown：" + pool.isShutdown());
            try{
                pool.execute(()->System.out.println("新任务"));
            }catch(RejectedExecutionException e) {
                System.out.println("main：新任务被拒绝");
            }
            boolean terminated=pool.awaitTermination(2,TimeUnit.SECONDS);
            System.out.println("main：线程池已终止：" + terminated);
        }
        System.out.println("main：程序结束");
    }
}
