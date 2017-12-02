package com.example.chapter7;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 李晓林 on 2016/12/28
 * qq:1220289215
 */

public class CancellingExecutor extends ThreadPoolExecutor{
    public CancellingExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        if (callable instanceof CancellableTask) {
            CancellableTask<T> cancellableTask= (CancellableTask<T>) callable;
            return cancellableTask.newTask();
        }else{
            return super.newTaskFor(callable);
        }
    }
}
