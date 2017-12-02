package com.example.chapter5;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by 李晓林 on 2016/12/22
 * qq:1220289215
 * 原文对future的描述
 * Future represents the lifecycle of a task and provides methods to test whether
 *the task has completed or been cancelled, retrieve its result, and cancel the task.
 *Future代表一个任务的生命周期，并提供方法检测这个任务是否完成或取消，能获取结果并取消任务
 * 任务的生命周期只能向前移不能后退
 */

public class CallableAndFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {


    }

    private void one() throws ExecutionException, InterruptedException {
        //可以返回结果，抛出异常的Runnable
        Callable<Integer> callable=new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return new Random().nextInt();
            }
        };
        /**
         * 官方对FutureTask的定义：A cancellable asynchronous computation.
         * 实现了Future接口，可以开始或取消计算，查询计算是否完成，获取计算结果，如果结果没出来就获取会阻塞
         * 一旦计算完成就不能取消或重来，除非调用runAndReset
         */
        FutureTask<Integer> futureTask = new FutureTask<Integer>(callable);
        new Thread(futureTask).start();
        System.out.println("the future result "+futureTask.get());
    }

    private void two() throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Callable<String> callable=new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "heihei";
            }
        };
        Future<String> future = service.submit(callable);
        System.out.println("the future result "+future.get());

    }
}
