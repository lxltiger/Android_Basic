package com.example.chapter7;

import com.example.Utils;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by 李晓林 on 2016/12/26
 * qq:1220289215
 * 关于中断
 * A good way to think about interruption is that it does not actually interrupt
 * a running thread; it just requests that the thread interrupt itself at the next convenient
 * opportunity.
 */

public class PrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> mQueue;

    public PrimeProducer(BlockingQueue<BigInteger> queue) {
        this.mQueue = queue;
    }

    @Override
    public void run() {

        try {
            BigInteger bigInteger = BigInteger.ONE;
            //显式的检查
            while (!Thread.currentThread().isInterrupted()) {
                //这个方法虽然是阻塞的，但也会检查中断状态，并且起主要作用
                mQueue.put(bigInteger = bigInteger.nextProbablePrime());
            }
        } catch (InterruptedException e) {
            System.out.println("produce");
            //不对异常处理，因为明确线程要终止
        }
        System.out.println("over");
    }

    public void cancel() {
        interrupt();
    }

    public void timeRun(Runnable r, long time, TimeUnit timeUnit) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Future<?> future = executorService.submit(r);
        try {
            //在指定指定时间获取结果，超时的取消
            future.get(time, timeUnit);
        } catch ( TimeoutException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            throw Utils.launderThrowable(e.getCause());
        } finally {
            //利用对已完成的task没有影响
            //true中断已运行的任务
            //false 不要运行没开始的任务
            future.cancel(true);
        }

    }


}
