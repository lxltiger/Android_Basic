package com.example.chapter7;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 李晓林 on 2017/1/3
 * qq:1220289215
 * 日志的打印
 * 多生产者 单消费者模式
 */

public class Logwriter {
    private final BlockingQueue<String> mQueue ;
    private final LoggerThread mLoggerThread;
    private int reservation=0;
    private boolean isOpen=true;
    public Logwriter(Runnable printRunnable, LoggerThread loggerThread) {
        mQueue = new LinkedBlockingQueue<>();
        mLoggerThread = loggerThread;
    }

    public void start() {
        mLoggerThread.start();
    }

    public void log(String value) throws InterruptedException {
        synchronized (this) {
            if (isOpen) {
                ++reservation;
            }else{
                throw new IllegalStateException("logger is shut down");
            }
        }
        mQueue.put(value);
    }

    public void shutdown() {
        synchronized (this) {
            isOpen=false;
        }
        mLoggerThread.interrupt();
    }
    private final class LoggerThread extends Thread {

        //因为take是阻塞的，可以通过中断的方式退出日志打印
        //这样会导致队列上的日志还没打印完就退出了
        //另一方面生产者还在生产日志，一旦取消打印，队列永久阻塞
        public void run2() {
            while (true) {
                try {
                    String take = mQueue.take();
                    System.out.println(take);
                } catch (InterruptedException e) {

                }
            }
        }

        @Override
        public void run() {
            for (;;){
                try {
                    synchronized (Logwriter.this) {
                        if (!isOpen && reservation == 0) {
                            break;
                        }
                    }
                    String msg=mQueue.take();
                    synchronized (Logwriter.this) {
                        reservation--;
                    }
                    System.out.println(msg);
                } catch (InterruptedException e) {
                   /*retry*/
                }
            }
        }
    }
}
