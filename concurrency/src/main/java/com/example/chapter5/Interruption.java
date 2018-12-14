package com.example.chapter5;


import java.util.concurrent.BlockingQueue;

/**
 * Created by 李晓林 on 2016/12/22
 * qq:1220289215
 * 对中断异常的处理
 *
 * 1.能抛则抛，将异常传递给方法的调用者。
 * 在不能上抛的情况下，如Runnable方法，必须捕获InterruptedException，
 * 并通过当前线程的interrupt()方法恢复中断状态，这样在调用栈中更高层的代码将看到引发了一个中断。
 * Sometimes you cannot throw InterruptedException, for
 * instance when your code is part of a Runnable. In these situations, you must
 * catch InterruptedException and restore the interrupted status by calling
 * interrupt on the current thread,
 * so that code higher up the call stack can see that an interrupt was issued,
 */

public class Interruption implements Runnable {
//    BlockingQueue<Task> mTasks;
    String[] importantMsgs=new String[100];
    @Override
    public void run() {
      /*  try {
            mTasks.take();
        } catch (InterruptedException e) {
            //恢复中断状态
            Thread.currentThread().interrupt();
        }*/
    }

    /**
     * 官方教程
     * 如果线程频繁调用抛出中断异常的方法，一旦捕获中断异常就返回
     */
    private void one() {
        for (String importantMsg : importantMsgs) {
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                return;
            }
            System.out.println(importantMsg);
        }
    }

    /**
     * 官方教程
     * 如果线程调用的方法耗时长又不抛出中断异常，就需要不断去检测线程的中断状态
     * Thread.interrupt 会设置中断状态，
     * Thread.interrupted 会检查中断状态，并清除这个标记
     * 非静态的某个具体线程使用的方法isInterrupted 不会清除标记
     * 抛出中断异常也会清除标记
     */
    private void two() throws InterruptedException {
        for (String importantMsg : importantMsgs) {
            System.out.println("take a long time to print "+importantMsg);
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }
        }
    }

}
