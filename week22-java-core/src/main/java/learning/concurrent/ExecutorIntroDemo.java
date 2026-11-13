package learning.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

public class ExecutorIntroDemo {
    public static void main(String[] args) throws InterruptedException {
        try(ExecutorService pool=Executors.newFixedThreadPool(2)) {
            for(int i=1;i<=5;++i) {
                int taskId=i;
                pool.execute(
                        ()->{
                            String name=Thread.currentThread().getName();
                            System.out.println("任务"+taskId+"开始"+name);
                            try{
                                Thread.sleep(300);
                            }
                            catch(InterruptedException e) {
                                Thread.currentThread().interrupt();
                                return ;
                            }
                            System.out.println("任务"+taskId+"结束"+name);
                        }
                );
            }
            System.out.println("主线程已提交5个任务");
        }
        System.out.println("线程池已关闭，主线程结束");
    }
}
