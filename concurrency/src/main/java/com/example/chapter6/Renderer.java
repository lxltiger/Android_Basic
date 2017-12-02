package com.example.chapter6;

import com.example.Photo;
import com.example.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by 李晓林 on 2016/12/23
 * qq:1220289215
 * ExecutorService可以获取线程运行状态，但在获取其结果的时候，如查询多个任务结果就需要轮询
 * 所以需要一个服务能把完成的结果放在一个阻塞队列上，只有有结果立马可以获取
 * 这里使用的是ExecutorCompletionService,它把线程池完成的结果封装成Future<T>放在阻塞队列上
 * <p>
 * 当一个任务提交上来，就会被QueueingFuture包装，这是FutureTask的子类，重写了isDone方法，
 * 当任务完成就把Future<T>放在默认阻塞队列上
 */

public class Renderer {
    ExecutorService mExecutorService = Executors.newFixedThreadPool(100);

    private void one(String resource) {
        ExecutorCompletionService<Photo> completionService =
                new ExecutorCompletionService<>(mExecutorService);
        //从resource获取图片url
        String[] urls = resource.split(",");
        //这次采用多图片并行下载
        for (String url : urls) {
            completionService.submit(new Callable<Photo>() {
                @Override
                public Photo call() throws Exception {
                    //download from url
                    return new Photo();
                }
            });
        }
        //处理文本的渲染
        // renderText(resource)
        //从阻塞队列获取结果
        for (String url : urls) {
            try {
                Future<Photo> future = completionService.take();
                Photo photo = future.get();
//                处理图片渲染renderPhoto(photo);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                throw Utils.launderThrowable(e.getCause());
            }
        }
    }

    //使用invokeAll方法处理并行下载，并设置超时时间
    private long fixtime = 4;
    private void two(String resource) throws InterruptedException {
        List<Callable<Photo>> tasks = new ArrayList<>();
        String[] urls = resource.split(",");
        for (String url : urls) {
            Callable<Photo> callable = new Callable<Photo>() {
                @Override
                public Photo call() throws Exception {
                    //get photo from url
                    return new Photo();
                }

            };
            tasks.add(callable);
        }

        /**
         * 在指定时间获取结果，
         * 返回的集合和任务集合是一一对应的，
         * 无论是否全部完成或超时，isDone都是true，
         * 没有及时完成的都是取消的
         */
        List<Future<Photo>> futures = mExecutorService.invokeAll(tasks, fixtime, TimeUnit.SECONDS);
        for (Future<Photo> future : futures) {
            try {
                Photo photo = future.get();

            } catch (ExecutionException e) {
                // TODO: 2016/12/23
            } catch (CancellationException e) {
                // TODO: 2016/12/23@ThreadSafe
            }
        }


    }
}
