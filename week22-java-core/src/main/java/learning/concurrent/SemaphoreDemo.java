package learning.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SemaphoreDemo {
    public static void main(String[] args) throws Exception {
        Semaphore semaphore=new Semaphore(2);
        AtomicInteger active=new AtomicInteger(0);
        try(ExecutorService pool=Executors.newFixedThreadPool(3)) {
            List<Future<String>> futures=new ArrayList<>();
            for(int i=1;i<=3;++i) {
                int taskId=i;
                futures.add(pool.submit(()->{
                            System.out.println("任务" + taskId + "尝试获取许可");
                            semaphore.acquire();
                            try{
                                int current=active.incrementAndGet();
                                System.out.println("任务" + taskId + "进入资源，当前并发数：" + current);
                                Thread.sleep(300);
                                return "任务" + taskId + "完成";
                            }finally{
                                active.decrementAndGet();
                                semaphore.release();
                            }
                        }
                        ));
            }
            for (Future<String> future : futures) {
                System.out.println(future.get());
            }
        }
        System.out.println("线程池已关闭");
    }
}
