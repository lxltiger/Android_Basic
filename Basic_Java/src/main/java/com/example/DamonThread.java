package com.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/*
 * 守护线程的特点，在所有前台线程退出后，Java虚拟机会退出，那么即使守护线程在执行状态或等待状态都会同样退出
 * */
public class DamonThread {
    public static void main(String[] args) throws Exception {
        DamonThread damonThread = new DamonThread();
        damonThread.startDamonThread2();


    }

//    使用一般线程
    private void startDamonThread() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            Thread thread = new Thread(new SimpleDamon());
//           必须start之前设置
            thread.setDaemon(true);
            thread.start();

        }
        TimeUnit.MILLISECONDS.sleep(4000);
    }

//    使用线程池
    private void startDamonThread2() throws InterruptedException{
//        DamonThreadFactory 定义了线程初始化参数
        ExecutorService service = Executors.newCachedThreadPool(new DamonThreadFactory());
        for (int i = 0; i < 5; i++) {
            service.execute(new SimpleDamon());
        }
        System.out.println("all daemon start");
        TimeUnit.MILLISECONDS.sleep(4000);

    }

    private static class SimpleDamon implements Runnable {

        public void run() {
            try {
                while (true) {
                    TimeUnit.MILLISECONDS.sleep(100);
                    System.out.println(this);
                }
            } catch (Exception e) {

            }
        }

        public String toString() {
            return Thread.currentThread().toString();
        }
    }


   static  class DamonThreadFactory implements ThreadFactory{

        @Override
        public Thread newThread(Runnable runnable) {
            Thread t = new Thread(runnable);
            t.setDaemon(true);
            return t;
        }
    }
}