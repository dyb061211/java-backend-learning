package learning.concurrent;

import java.util.concurrent.*;

public class CallableFutureDemo {
    public static void main(String[] args) throws Exception {
        try(ExecutorService pool=Executors.newFixedThreadPool(2)) {
            Callable<String>task1=()->{
                Thread.sleep(500);
                return "A001 商品处理成功";
            };
            Callable<String>task2=()->{
                Thread.sleep(300);
                return "A002 商品处理成功";
            };
            Future<String>future1=pool.submit(task1);
            Future<String>future2=pool.submit(task2);
            System.out.println("两个任务已经提交");
            System.out.println("main 正在做其他工作");
            String result1=future1.get();
            String result2=future2.get();
            System.out.println(result1);
            System.out.println(result2);
        }
        System.out.println("线程池已关闭");
    }
}
