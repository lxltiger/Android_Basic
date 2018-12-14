package com.example;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

//  NEW 线程还没开始的状态


// RUNNABLE 可运行线程状态，线程正在Java虚拟机中执行，但可能在等待像CPU这样的资源


//BLOCKED,等待monitor lock的阻塞状态，等待获取monitor lock进入synchronized 代码块或方法；或在调用Object.wait方法后等待重新进入进入synchronized 代码块或方法

/*
 *  WAITING,没有超时的等待状态 由于下列方法调用导致的等待状态
 *
 * 没有超时的Object.wait
 * 没有超时的Thread.join
 * LockSupport.park
 * 此状态的线程需要其他线程唤醒
 * Object.wait 需要其他线程调用 Object.notify()或者Object.notifyAll()
 * Thread.join 等待指定线程终止
 */

/**
 *
 * TIMED_WAITING,有指定时间的等待状态   由于下列方法调用导致
 * Thread.sleep（long millis）
 * Object.wait（long timeout）
 * Thread.join（long timeout）
 * parkNanos LockSupport.parkNanos
 * parkUntil LockSupport.parkUntil
 */

//TERMINATED 完成执行的终止状态

//Thread.sleep(long millis) 中止当前线程的执行，参数为中止时长，中止期间不会丢失任何监视器
//Thread.yield() 使当前线程让出CPU使用权，从Running到Runnable状态，不会丢失监视器，极少使用
//thread.join 等待该线程终止

/**
 * thread.interrupt 中断操作，除非当前线程中断自己（其他线程如何中断？），否则会抛SecurityException
 *
 *如果这个线程因为Object类的wait方法的调用或Thread类的sleep或join方法的调用而进入阻塞状态，
 * 它的中断状态会被清除还能收到InterruptedException  ？？？
 *
 * 如果这个线程阻塞在 java.nio.channels.InterruptibleChannel 这样的IO操作上，
 *这个channel会被关闭，线程的中断状态会被设置，还能收到java.nio.channels.ClosedByInterruptException
 *
 * 如果这个线程阻塞在 java.nio.channels.Selector 上
 * 线程的中断状态会被设置，然后从selection操作立马返回一个（可能）非零值，就像Selector。wakeup方法被调用

 *
 * <p> If none of the previous conditions hold then this thread's interrupt
 * status will be set. </p>
 *
 
 */

/*
 *线程的working memory只是cpu的寄存器和高速缓存的抽象描述
 * 使用volatile修饰的变量不会缓冲在寄存器或其他处理器的缓存中，
 * 所以一个线程的写操作总能被证其他读线程看见,但不保证mutual exclude （互斥）
 * */
public class StopThread {

    private static volatile boolean stop = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread() {
            @Override
            public void run() {
                int number = 0;
                System.out.println("start");
                while (!stop) {
                    number++;
                }
                System.out.println(number);
            }
        }.start();

        TimeUnit.SECONDS.sleep(1);
        System.out.println("stop");
        stop = true;
    }
}
