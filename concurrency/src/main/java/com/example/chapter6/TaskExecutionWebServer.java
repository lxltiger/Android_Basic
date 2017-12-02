package com.example.chapter6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 使用线程执行任务的时候，当任务完成线程即销毁，任务和线程是耦合的
 * 使用Executor可以按需创建线程池，从各种类型任务队列取任务执行，线程是可以重复利用的
 *
 */
class TaskExecutionWebServer {
    private static final int NTHREADS = 100;
    private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);

    public static void main(String[] args) throws IOException {
        ServerSocket socket = new ServerSocket(80);
        while (true) {
            final Socket connection = socket.accept();
            Runnable task = new Runnable() {
                public void run() {
//                    handleRequest(connection);
                }
            };

            exec.execute(task);

        }
    }
}