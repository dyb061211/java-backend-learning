package learning.concurrent;

public class VirtualThreadDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread[] threads=new Thread[3];
        long start=System.nanoTime();
        for(int i=0;i<3;++i) {
            int taskId=i+1;
            threads[i]=Thread.ofVirtual().name("virtual-" + taskId).start(()->{
                System.out.println("任务" + taskId + "，线程：" + Thread.currentThread().getName());
                System.out.println("是否是虚拟线程："+Thread.currentThread().isVirtual());
                try{
                    Thread.sleep(300);
                }catch(InterruptedException e){
                    Thread.currentThread().interrupt();
                    return;
                }
            });
        }
        for(Thread thread:threads){
            thread.join();
        }
        long end = System.nanoTime();
        System.out.println("总耗时：" + (end - start) / 1000000 + "ms");
    }
}
