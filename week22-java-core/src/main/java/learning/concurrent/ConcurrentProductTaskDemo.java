package learning.concurrent;

import java.util.List;
import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentProductTaskDemo {
    static final class ProductSnapshot{
        final String sku;
        final String name;
        final int stock;
        ProductSnapshot(String sku, String name, int stock) {
            this.sku = sku;
            this.name = name;
            this.stock = stock;
        }
    }

    static String process(ProductSnapshot product) throws InterruptedException {
        Thread.sleep(300);
        return product.sku+" | "+product.name+" | 库存："+product.stock+" | "+(product.stock>0?"有货":"缺货");
    }

    static void runConcurrent(List<ProductSnapshot>products,int threadCount,boolean simulateFailure) throws Exception {
        AtomicInteger active=new AtomicInteger(0);
        AtomicInteger maxActive=new AtomicInteger(0);
        List<String> successSkus = new ArrayList<>();
        List<String> failedSkus = new ArrayList<>();
        long start=System.nanoTime();
        try(ThreadPoolExecutor pool=new ThreadPoolExecutor(
                threadCount,
                threadCount,
                0L,
                TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(6),
                new ThreadPoolExecutor.AbortPolicy()
        )) {
            List<Future<String>>futures=new ArrayList<>();
            for(ProductSnapshot product:products){
                Future<String>future=pool.submit(()->{
                    int current=active.incrementAndGet();
                    maxActive.accumulateAndGet(current,Math::max);
                    try{
                        String result=process(product);
                        if(simulateFailure&&("A004".equals(product.sku))) {
                            throw new IllegalStateException("A004 模拟外部服务故障");
                        }
                        return result;
                    }finally{
                        active.decrementAndGet();
                    }
                });
                futures.add(future);
            }
            int success=0;
            int failed=0;
            for (int i = 0; i < futures.size(); i++) {
                Future<String> future = futures.get(i);
                ProductSnapshot product = products.get(i);
                try {
                    String result = future.get();
                    successSkus.add(product.sku);
                    success++;

                } catch (ExecutionException e) {
                    failedSkus.add(product.sku);
                    failed++;
                    System.out.println("FAILED：" + product.sku + "，原因：" + e.getCause());
                }
            }
            System.out.println("成功数量：" + success);
            System.out.println("失败数量：" + failed);
            System.out.print("成功SKU：[");
            for(int i=0;i<successSkus.size();i++){
                System.out.print(successSkus.get(i));
                if(i!=successSkus.size()-1){
                    System.out.print(",");
                }
            }
            System.out.println("]\n");
            System.out.print("失败SKU：[");
            for(int i=0;i<failedSkus.size();i++){
                System.out.print(failedSkus.get(i));
                if(i!=failedSkus.size()-1){
                    System.out.print(",");
                }
            }
            System.out.println("]\n");
        }
        long end=System.nanoTime();
        System.out.println("工作线程数：" + threadCount);
        System.out.println("最大活跃任务数：" + maxActive.get());
        System.out.println("并发耗时：" + (end - start) / 1000000 + " ms");
    }

    static void runTimeoutCancellation() throws Exception {
        CountDownLatch started=new CountDownLatch(1);
        CountDownLatch cleaned=new CountDownLatch(1);
        try(ExecutorService pool=Executors.newFixedThreadPool(1)) {
            Future<String> future=pool.submit(()->{
                try{
                    System.out.println("慢任务：开始");
                    started.countDown();
                    Thread.sleep(1000);
                    System.out.println("慢任务：正常完成");
                    return "商品处理成功";
                }catch(InterruptedException e) {
                    System.out.println("慢任务：响应中断");
                    Thread.currentThread().interrupt();
                    throw e;
                }finally {
                    System.out.println("慢任务：执行清理");
                    cleaned.countDown();
                }
            });
            if(!started.await(2,TimeUnit.SECONDS)) {
                System.out.println("任务启动超时");
                future.cancel(true);
                return;
            }
            try{
                String result=future.get(200,TimeUnit.MILLISECONDS);
                System.out.println("main：" + result);
            }catch(TimeoutException e){
                System.out.println("main：等待超时");
                boolean cancelled=future.cancel(true);
                System.out.println("main：取消请求结果：" + cancelled);
            }catch(ExecutionException e){
                System.out.println("main：任务执行失败：" + e.getCause());
            }
            boolean cleanFinished=cleaned.await(200,TimeUnit.SECONDS);
            System.out.println("main：已观察到清理：" + cleanFinished);
        }
        System.out.println("main：线程池已终止");
    }

    public static void main(String[] args) throws Exception {
        List<ProductSnapshot>products=List.of(
                new ProductSnapshot("A001", "键盘", 10),
                new ProductSnapshot("A002", "鼠标", 20),
                new ProductSnapshot("A003", "显示器", 0),
                new ProductSnapshot("A004", "耳机", 8),
                new ProductSnapshot("A005", "硬盘", 15),
                new ProductSnapshot("A006", "笔记本", 3)
        );
        long start=System.nanoTime();
        for(ProductSnapshot product:products){
            String result = process(product);
            System.out.println(result);
        }
        long end = System.nanoTime();
        System.out.println("处理数量：" + products.size());
        System.out.println("顺序执行耗时：" + (end - start) / 1000000 + " ms");

        runConcurrent(products, 2, false);
        runConcurrent(products, 3, false);

        System.out.println("\n=== 模拟部分失败 ===");
        runConcurrent(products, 3, true);

        System.out.println("\n=== 超时与取消实验 ===");
        runTimeoutCancellation();
    }
}
