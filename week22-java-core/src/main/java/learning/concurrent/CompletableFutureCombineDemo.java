package learning.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureCombineDemo {
    static void simulateWait(long millis) {
        try{
            Thread.sleep(millis);
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
            throw new IllegalStateException("任务被中断", e);
        }
    }

    public static void main(String[] args) throws Exception {
        long start=System.nanoTime();
        try(ExecutorService pool=Executors.newFixedThreadPool(2)) {
            CompletableFuture<String>productFuture=CompletableFuture.supplyAsync(()->{
                System.out.println("开始查询商品信息");
                simulateWait(500);
                return "A001: Apple";
            },pool);
            CompletableFuture<Integer>StockFuture=CompletableFuture.supplyAsync(()->{
                System.out.println("开始查询库存");
                simulateWait(300);
                return 12;
            },pool);
            CompletableFuture<String>combined=productFuture.thenCombine(StockFuture,(product,stock)->{
                System.out.println("开始合并结果");
                return product + "，库存：" + stock;
            });
            System.out.println("main：两个查询已安排");
            String result = combined.get();
            System.out.println("最终结果：" + result);
        }
        long end=System.nanoTime();
        System.out.println("总耗时："+(end-start)/1000000+"ms");
    }
}
