package learning.concurrent;

public class ThreadTimingDemo {
    public static void main(String[] args) throws InterruptedException {
        Runnable task=()->{
            String name=Thread.currentThread().getName();
            System.out.println(name+"开始执行");
            try{
                Thread.sleep(3000);
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
                return ;
            }
            System.out.println(name+"执行结束");
        };
        long start=System.nanoTime();
        for(int i=0;i<3;++i) {
            task.run();
        }
        long end=System.nanoTime();
        System.out.println("顺序耗时"+(end-start)/1_000_000+"ms");
        Thread[] threads=new Thread[3];
        start=System.nanoTime();
        for(int i=0;i<3;++i) {
            threads[i]=new Thread(task,"worker"+i);
            threads[i].start();
        }
        for(Thread thread:threads){
            thread.join();
        }
        end = System.nanoTime();
        System.out.println("并发耗时"+(end-start)/1_000_000+"ms");
    }
}
