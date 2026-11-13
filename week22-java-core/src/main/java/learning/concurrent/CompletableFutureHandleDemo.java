package learning.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureHandleDemo {
    public static void main(String[] args) throws Exception {
        try(ExecutorService pool=Executors.newFixedThreadPool(2)) {
            CompletableFuture<String>future=CompletableFuture.supplyAsync(()->{
                System.out.println("开始查询 A002");
                throw new IllegalArgumentException("A002 商品数据非法");
            },pool).thenApply(result->{
                System.out.println("执行格式化");
                return "商品信息：" + result;
            }).handle((result,error)->{
                if(error!=null) {
                    Throwable cause=error;
                    if(error instanceof CompletionException&&error.getCause()!=null) {
                        cause = error.getCause();
                    }
                    return "FAILED：" + cause.getMessage();
                }
                return "SUCCESS：" + result;
            });
            System.out.println("main：处理链已安排");
            System.out.println("main：" + future.get());
        }
        System.out.println("线程池已关闭");
    }
}
