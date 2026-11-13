package learning.concurrent;

public class ThreadStartDemo {
    public static void main(String[] args) throws InterruptedException {
        Runnable task= () -> {
            System.out.println("执行任务的线程"+Thread.currentThread().getName());
        };
        Thread t1=new Thread(task,"worker-1");
        t1.run();
        Thread t2=new Thread(task,"worker-2");
        t2.start();
        t2.join();
        System.out.println("主线程结束");
    }
}
