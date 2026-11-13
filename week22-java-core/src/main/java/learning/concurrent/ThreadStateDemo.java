package learning.concurrent;

public class ThreadStateDemo {
    public static void main(String[] args) throws InterruptedException {

        Thread worker = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "worker");

        System.out.println("启动前：" + worker.getState());

        worker.start();

        System.out.println("启动后：" + worker.getState());

        worker.join();

        System.out.println("结束后：" + worker.getState());
    }
}
