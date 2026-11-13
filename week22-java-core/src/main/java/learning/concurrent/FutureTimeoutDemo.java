package learning.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

public class FutureTimeoutDemo {
    public static void main(String[] args) throws Exception {
        try(ExecutorService pool=Executors.newFixedThreadPool(1)) {
            Future<String>future=pool.submit(()->{
                System.out.println("worker：任务开始");
                Thread.sleep(1000);
                System.out.println("worker：任务执行完成");
                return "商品处理成功";
                    }
            );
            try{
                String result=future.get(200,TimeUnit.MILLISECONDS);
                System.out.println("main：" + result);
            }
            catch(TimeoutException e){
                System.out.println("main：等待超时");
            }
            System.out.println("main：继续执行其他代码");
            System.out.println("main：再次等待最终结果");
            String finalResult = future.get();
            System.out.println("main：" + finalResult);
        }
        System.out.println("线程池已关闭");
    }
}
