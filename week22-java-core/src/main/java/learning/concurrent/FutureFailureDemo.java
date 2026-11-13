package learning.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class FutureFailureDemo {
    public static void main(String[] args) throws Exception {
        try(ExecutorService pool=Executors.newFixedThreadPool(3)) {
            Callable<String>task1=()->{
                Thread.sleep(200);
                return "A001 处理成功";
            };
            Callable<String>task2=()->{
                Thread.sleep(100);
                throw new IllegalArgumentException("A002 商品数据非法");
            };
            Callable<String>task3=()->{
                Thread.sleep(150);
                return "A003 处理成功";
            };
            Future<String>future1=pool.submit(task1);
            Future<String>future2=pool.submit(task2);
            Future<String>future3=pool.submit(task3);
            System.out.println("三个任务已经提交");
            System.out.println(future1.get());
            try{
                System.out.println(future2.get());
            }
            catch(ExecutionException e) {
                System.out.println("A002 执行失败");
                System.out.println("原始异常"+e.getCause());
            }
            System.out.println("A002 isDone：" + future2.isDone());
            System.out.println("A002 isCancelled：" + future2.isCancelled());
            System.out.println(future3.get());
        }
        System.out.println("线程池已关闭");
    }
}
