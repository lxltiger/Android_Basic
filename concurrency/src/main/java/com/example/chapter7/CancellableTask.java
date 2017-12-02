package com.example.chapter7;

import java.util.concurrent.Callable;
import java.util.concurrent.RunnableFuture;

/**
 * Created by 李晓林 on 2016/12/28
 * qq:1220289215
 */

public interface CancellableTask<T> extends Callable<T> {
    void cancel();

    RunnableFuture<T> newTask();
}
