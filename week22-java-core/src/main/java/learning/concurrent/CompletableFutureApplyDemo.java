package learning.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureApplyDemo {
    public static void main(String[] args) throws Exception {
        try(ExecutorService pool=Executors.newFixedThreadPool(2)) {
            CompletableFuture<String>future=CompletableFuture.supplyAsync(()->{
                System.out.println("查询线程："+Thread.currentThread().getName());
                try{
                    Thread.sleep(300);

                }catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("模拟查询被中断",e);
                }
                return "A001: Apple";
                    },pool).thenApply(result->{
                System.out.println("格式化线程：" + Thread.currentThread().getName());
                return "商品信息：" + result;
            });
            System.out.println("main：已安排查询和格式化");
            String finalResult=future.get();
            System.out.println("main：" + finalResult);
        }
        System.out.println("main：线程池已关闭");
    }
}
