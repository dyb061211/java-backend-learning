package learning.catalog;

import java.io.IOException;

public class ExceptionDemo {
    public static void simulateRead() throws IOException{
        throw new IOException("模拟文件读取失败");
    }

    public static void runTask(boolean fail) {
        try{
            System.out.println("开始执行任务");
            if (fail) {
                throw new IllegalStateException("模拟任务失败");
            }
            System.out.println("任务执行成功");
        }
        finally{
            System.out.println("finally：执行清理工作");
        }
    }

    static class DemoResource implements AutoCloseable{
        public DemoResource() {
            System.out.println("资源已打开");
        }

        public void work (boolean fail) {
            System.out.println("开始使用资源");
            if (fail) {
                throw new IllegalStateException("模拟操作失败");
            }
            System.out.println("操作成功");
        }
        @Override
        public void close() {
            System.out.println("资源已关闭");
        }
    }

    public static void main(String[] args) {
        try{
            simulateRead();
            System.out.println("读取成功");
        }
        catch (IOException e){
            System.out.println("捕获异常：" + e.getMessage());
        }
        System.out.println("程序继续执行");

        System.out.println("=== 正常情况 ===");
        runTask(false);

        System.out.println("=== 异常情况 ===");

        try {
            runTask(true);
        } catch (IllegalStateException e) {
            System.out.println("捕获异常：" + e.getMessage());
        }

        System.out.println("=== 自动关闭：正常情况 ===");

        try (DemoResource resource = new DemoResource()) {
            resource.work(false);
        }

        System.out.println("=== 自动关闭：异常情况 ===");

        try (DemoResource resource = new DemoResource()) {
            resource.work(true);
        } catch (IllegalStateException e) {
            System.out.println("捕获异常：" + e.getMessage());
        }

        System.out.println("资源实验结束");

        System.out.println("实验结束");
    }
}
