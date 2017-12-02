package com.example.chapter6;

/**
 * Created by 李晓林 on 2016/12/23
 * qq:1220289215
 */

import com.example.Photo;
import com.example.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 使用future和callable来提高获取内容速度
 */
public class FutureRenderer {
    private final ExecutorService mExecutor = Executors.newFixedThreadPool(100);


    private void one(String resource) {
        Callable<List<Photo>> photoCallable=new Callable<List<Photo>>() {
            @Override
            public List<Photo> call() throws Exception {
                List<Photo> photoList = new ArrayList<>();
                photoList.add(new Photo());
                return photoList;
            }
        };
        Future<List<Photo>> result = mExecutor.submit(photoCallable);

        //处理文本内容
//        handleText(resource);
        try {
            List<Photo> photos = result.get();
//            renderPhote(photos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            result.cancel(true);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            throw Utils.launderThrowable(cause);
        }
    }
    //使用Future带时间有效性的参数可提高程序的响应而不是无休止的等待
    private long time_waiting=4*1000*1000;
    private void two() throws InterruptedException {
        long nanotime=System.nanoTime()+time_waiting;
        Future<String> ads = mExecutor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "广告";
            }
        });
        //展示页面
//        webpage=displayPage();
        //如果时间是负数会当0处理
      long left=nanotime-System.nanoTime();
        String ad;
        try {
            //如果在指定时间内没有获取结果就超时
            ad = ads.get(left, TimeUnit.NANOSECONDS);
        } catch (ExecutionException e) {
            ad = "default";
            throw Utils.launderThrowable(e.getCause());
        } catch (TimeoutException e) {
            ad = "default";
            ads.cancel(true);
        }
        //返回带广告的页面
//        webpage.setad(ad);
    }
}
