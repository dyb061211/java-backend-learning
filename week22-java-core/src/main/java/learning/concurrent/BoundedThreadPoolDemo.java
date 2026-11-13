package learning.concurrent;

import java.util.concurrent.*;

public class BoundedThreadPoolDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch started=new CountDownLatch(2);
        CountDownLatch release=new CountDownLatch(1);
        try(ThreadPoolExecutor pool=new ThreadPoolExecutor(
                2,2,
                0L,TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(2),
                new ThreadPoolExecutor.CallerRunsPolicy()
        )) {
            for(int i=1;i<=2;++i) {
                int taskId=i;
                pool.execute(()->{
                    System.out.println("任务" + taskId + "开始");
                    started.countDown();
                    try{
                        release.await(5,TimeUnit.SECONDS);
                    }catch(InterruptedException e){
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("任务" + taskId + "结束");
                });
            }
            try{
                if(!started.await(2,TimeUnit.SECONDS)){
                    System.out.println("工作线程启动超时");
                    return;
                }
                pool.execute(() -> System.out.println("任务3执行"));
                pool.execute(() -> System.out.println("任务4执行"));
                System.out.println("等待队列的长度："+pool.getQueue().size());
                try{
                    pool.execute(()->System.out.println("任务5执行"+Thread.currentThread().getName()));
                }catch(RejectedExecutionException e){
                    System.out.println("任务5被拒绝");
                }
            }finally{
                release.countDown();
            }
        }
        System.out.println("线程池已关闭");
    }
}
