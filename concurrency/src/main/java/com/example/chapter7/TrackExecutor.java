package com.example.chapter7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 李晓林 on 2017/1/3
 * qq:1220289215
 * 跟踪任务执行的结果，将任务已开始但到shutdownnow时没有完成的任务记录下来
 */

public abstract class TrackExecutor extends AbstractExecutorService{
    private final ExecutorService mExecutorService = Executors.newCachedThreadPool();
    private final Set<Runnable> mRunnables = Collections.synchronizedSet(new HashSet<Runnable>());

    public List<Runnable> getRunnables() {
        if (!isTerminated()) {
            throw new IllegalStateException("not terminate");
        }
        return new ArrayList<>(mRunnables);
    }
    @Override
    public void execute(final Runnable command) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    command.run();
                } finally {
                    if (isShutdown() && Thread.currentThread().isInterrupted()) {
                        mRunnables.add(command);
                    }
                }
            }
        });
    }
}
