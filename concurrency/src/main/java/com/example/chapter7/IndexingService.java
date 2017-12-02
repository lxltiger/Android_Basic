package com.example.chapter7;

import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by 李晓林 on 2017/1/3
 * qq:1220289215
 * 一个线程检索文件，另一个线程处理结果
 * 使用毒丸终止线程，
 * 当停止检索时望队列添加事先设计好的终止条件
 * 这样消费者执行到这个条件就终止
 */

public class IndexingService {
    private final BlockingQueue<File> mQueue = new LinkedBlockingQueue<>();
    private static final File mPoison = new File("");
    private final CrawlThread mCrawlThread = new CrawlThread();
    private final IndexThread mIndexThread = new IndexThread();

    public void start() {
        mCrawlThread.start();
        mIndexThread.start();
    }

    public void stop() {
        mCrawlThread.interrupt();
    }

    public void awaitTerminate() throws InterruptedException {
        mIndexThread.join();
    }


    private final class CrawlThread extends Thread {
        @Override
        public void run() {
            try {
                searchFile();
            } catch (InterruptedException e) {
                /**/
            } finally {
                while (true) {
                    try {
                        mQueue.put(mPoison);
                        break;
                    } catch (InterruptedException e) {
                       /* retry*/
                    }
                }
            }
        }

        private void searchFile() throws InterruptedException {
            //search file
        }
    }

    private final class IndexThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    File file = mQueue.take();
                    if (file == mPoison) {
                        break;
                    }else{
                        //处理file
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

