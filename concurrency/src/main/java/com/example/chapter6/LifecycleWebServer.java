package com.example.chapter6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Executor 创建的线程池不能管理线程的状态，在关闭的时候不方便处理
 * ExecutorService继承Executor，添加对线程生命周期的管理，有三种状态
 * 正在运行，正在关闭，已终止
 * 当调用shutdown方法时，会停止接受新任务，如果提交会报异常
 * 而之前运行的任务不会停止,包括没有还没有执行的任务会继续执行
 *
 */
class LifecycleWebServer {
    private final ExecutorService exec = Executors.newFixedThreadPool(100);

    public static void main(String[] args) {
        LifecycleWebServer webServer=new LifecycleWebServer();


    }
    private Random mRandom=new Random();
    private Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(mRandom.nextInt(5000));
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    private Runnable mRunnable2=new Runnable() {
        @Override
        public void run() {
            System.out.println("duang");
        }
    };

    /**
     * shutdown的演示
     */
    private void one() {
        for (int i = 0; i < 10; i++) {
            exec.execute(mRunnable);
        }
        exec.shutdown();
        try {
            //判断在指定时间里有没有终止
            boolean b =exec.awaitTermination(2, TimeUnit.SECONDS);
            System.out.println(b);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!exec.isShutdown()) {
           exec.execute(mRunnable2);
        }else{
            System.out.println("no more task is accepted ");
        }
    }
    //web程序接受客户访问
    public void start() throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (!exec.isShutdown()) {
            try {
                final Socket conn = socket.accept();
                exec.execute(new Runnable() {
                    public void run() {
                        handleRequest(conn);
                    }
                });
            } catch (RejectedExecutionException e) {
                if (!exec.isShutdown())
                    System.out.println("task submission rejected");
            }
        }
    }
    //web端关闭执行
    public void stop() {
        exec.shutdown();
    }
    //客户端请求终止
    void handleRequest(Socket connection) {
       /* Request req = readRequest(connection);
        if (isShutdownRequest(req))
            stop();
        else
            dispatchRequest(req);*/
    }
}