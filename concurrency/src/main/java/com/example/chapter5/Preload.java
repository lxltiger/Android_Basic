package com.example.chapter5;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by 李晓林 on 2016/12/22
 * qq:1220289215
 */

public class Preload {
   private final  FutureTask<String> mFutureTask=new FutureTask<String>(new Callable<String>() {
        @Override
        public String call() throws Exception {
            //do something ahead
            return "";
        }
    });
    private final Thread mThread = new Thread(mFutureTask);

    public void start() {
        mThread.start();
    }

    /**
     * 对get方法抛出的异常处理相当复杂
     *
     */
    public void fetch() {
        try {
            String s = mFutureTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
