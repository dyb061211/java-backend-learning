
package learning.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SubmitGetOrderDemo {

    static Callable<String> createTask(int id) {
        return () -> {
            Thread.sleep(300);
            return "任务" + id + "完成";
        };
    }
    public static void main(String[] args) throws Exception {
        long start = System.nanoTime();
        try (ExecutorService pool = Executors.newFixedThreadPool(3)) {
            for (int i = 1; i <= 3; i++) {
                Future<String> future = pool.submit(createTask(i));
                System.out.println(future.get());
            }
        }
        System.out.println("方式一耗时：" + (System.nanoTime() - start) / 1_000_000 + " ms");
        start = System.nanoTime();
        try (ExecutorService pool = Executors.newFixedThreadPool(3)) {
            List<Future<String>> futures = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                futures.add(pool.submit(createTask(i)));
            }
            for (Future<String> future : futures) {
                System.out.println(future.get());
            }
        }
        System.out.println("方式二耗时：" + (System.nanoTime() - start) / 1_000_000 + " ms");
    }
}
