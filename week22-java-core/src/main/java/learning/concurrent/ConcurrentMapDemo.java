
package learning.concurrent;

import java.util.concurrent.*;

public class ConcurrentMapDemo {
    static Callable<String> createTask(
            String requester,
            ConcurrentHashMap<String, String> map,
            CountDownLatch ready,
            CountDownLatch start) {
        return () -> {
            ready.countDown();
            if (!start.await(2, TimeUnit.SECONDS)) {
                throw new IllegalStateException("等待开始信号超时");
            }
            String previous = map.putIfAbsent("A001", requester);
            if (previous == null) {
                return requester + " 登记成功";
            }
            return requester + " 登记失败，已有：" + previous;
        };
    }

    public static void main(String[] args) throws Exception {

        ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>();

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        try (ExecutorService pool = Executors.newFixedThreadPool(2)) {

            Future<String> f1 = pool.submit(
                    createTask("线程1", map, ready, start)
            );

            Future<String> f2 = pool.submit(
                    createTask("线程2", map, ready, start)
            );

            try {
                if (!ready.await(2, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("任务就绪超时");
                }
            } finally {
                start.countDown();
            }

            System.out.println(f1.get());
            System.out.println(f2.get());

            System.out.println("Map大小：" + map.size());
            System.out.println("A001归属：" + map.get("A001"));
        }

        System.out.println("线程池已关闭");
    }
}
