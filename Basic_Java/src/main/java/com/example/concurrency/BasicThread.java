package com.example.concurrency;

import java.util.concurrent.TimeUnit;

/*
* 线程的状态
* NEW 线程还没开始的状态
* RUNNABLE 可运行状态，线程在等待像CPU这样的资源
* BLOCKED,等待锁的阻塞状态 具体状况？？
* WAITING,无限等待状态 由于下列方法调用导致
*   没有超时的object.wait（），需要其他线程调用 Object.notify()或者Object.notifyAll()唤醒
    没有超时的thread.join（），等待指定线程终止，能被其他线程interrupt中断
    LockSupport.park（）？

* TIMED_WAITING,有指定时间的等待状态与无限等待区别就是到了指定时间间隔能自己终止等待状态， 由于下列方法调用导致
*   Thread.sleep（long millis）
*   object.wait（long millis）
*   thread.join（long millis）
*   LockSupport.parkNanos
*   LockSupport.parkUntil
*
* TERMINATED 完成执行的终止状态
*/


public class BasicThread {
    /*
     *
     * 为什么wait、notify、notifyAll操作线程的方法定义在Object类中？
     * 因为这些方法在操作同步线程时,都必须标识他们所操作线程持有的锁，锁可以是任意对象,所以可以被任意对象调用的方法定义Object类中.
     */

    private final Object lock = new Object();
    private boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        BasicThread basicThread = new BasicThread();
//        basicThread.interruptWait();
        basicThread.stopThread();
    }

    /*
     * 开启一个线程获t取同步锁后等待
     * 在主线程中使用notify方法唤醒t，
     * 随后使用interrupt方法中断等待
     *
     * 演示了sleep、wait、notify、join、interrupt方法的使用
     */
    private void interruptWait() throws InterruptedException {
        Thread t = new Thread() {
            @Override
            public void run() {
//                获取lock对象上的锁，如果已被其他线程获取，将会阻塞在此
                synchronized (lock) {
                    /*
                     * JDK文档中明确说明wait方法存在虚假唤醒，建议在while(condition)循环中使用
                     * */
                    while (flag) {
                        System.out.println("begin of wait");
                        try {
                            /*
                             * 使当前线程t进入无限等待状态，释放在lock上的锁
                             * 等待过程中，当前线程如果有同步其他对象，其锁不会被释放。
                             *
                             * 线程调用此方法的前提是必须拥有这个object的锁，
                             *
                             * 其他线程调用lock的notify或notifyAll方法唤醒等待
                             * 或者调用t.interrupt（）中断等待
                             *
                             *等待状态打破后，恢复被调度的资格，与其他线程公平竞争lock对象的同步权
                             * 一旦它获得了同步权，所有在此对象的同步声明都恢复到wait时的状态

                             */
                            lock.wait();
                        } catch (InterruptedException e) {
//                            等待状态被中断,异常唤醒后不再等待,修改等待条件
                            flag = false;
                            System.out.println("get interrupted ");
                        }
                        System.out.println("end of wait");
                    }
                    System.out.println("end of loop");
                    try {
                        //结束循环后睡眠，为了演示join方法
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        //直接调用线程run方法不会开启新线程
        t.start();
        /*
         * 底层调用的是Thread.sleep(ms, ns)方法
         * 中止主线程的执行，确保t线程先执行，不需要获取锁，中止期间也不会丢失任何锁
         */
        TimeUnit.SECONDS.sleep(2);
        /*
         * 在主线程非同步代码块调用下面方法都会抛IllegalMonitorStateException，因为没有拿到锁
         * this.notify();
         * lock.notify();
         *
         * 一个线程获取对象锁有三种方式：
         * 1.通过执行该对象的synchronized实例方法，锁为this。
         * 2.执行同步代码块 synchronized（object）｛statement｝ 锁为object
         * 3.调用类的synchronized静态方法，锁为类名.class。
         *
         * synchronized作用
         *  1.保证同一时间只有一个线程进入方法或代码块执行
         *  2.保证其他线程修改的效果能被后来的进入的线程看见
         *  注意：尽量缩小同步代码块的范围，只同步必须要同步的代码，
         *      不要在同步中调用其他同步方法，容易死锁
         */
        synchronized (lock) {
            /*
             *唤醒在lock对象上等待的单个线程，如果有多个线程等待，会随意唤醒一个；notifyAll 唤醒在此对象上等待的所有线程
             *被唤醒的线程与其他线程以公平的方式竞争这个对象的锁
             *
             *this.notify() 抛IllegalMonitorStateException
             *至此主线程有lock对象上的锁，但没有this对象上的锁，一个线程只有是这个对象锁的所有者才能调用此方法
             *
             *t线程被唤醒后，没人竞争直接拿到锁，恢复到wait时的状态，打印几句话又接着进入等待状态
             * 等待和唤醒必须是同一把锁.
             */
            lock.notify();
        }
        /**
         * interrupt()方法仅仅是改变中断状态，并不是直接中断正在运行的线程。
         * wait/join/sleep这些方法内部会不断地检查线程的中断状态值,假如正常为1
         * 中断的原理是当线程被object.wait(),thread.join()或Thread.sleep(long milli)方法阻塞时，
         * 调用interrupt()方法后改变中断状态，比如改为0，
         * 当这些方法发现中断状态值改变时则抛出InterruptedException异常；并清除中断状态，即改回1，表明现在正常了，已经通知了
         * 对于没有阻塞的线程，调用interrupt()方法没有作用。
         *
         * 如果这个线程阻塞在 java.nio.channels.InterruptibleChannel 这样的IO操作上，
         * 这个channel会被关闭，线程的中断状态会被设置，收到java.nio.channels.ClosedByInterruptException
         *
         * 如果这个线程阻塞在 java.nio.channels.Selector 上
         * 线程的中断状态会被设置，然后从selection操作立马返回一个（可能）非零值，就像Selector.wakeup方法被调用
         *
         */
        TimeUnit.SECONDS.sleep(2);
        t.interrupt();
        //使主线程阻塞 等待t线程结束，底层使用的wait方法
        t.join();

        System.out.println("finish all");
    }


    private void stopThread() throws InterruptedException {

        StopThread stopThread = new StopThread();
        stopThread.start();
        TimeUnit.SECONDS.sleep(3);
        stopThread.cancle();
    }

    class StopThread extends Thread {
        /*
         * volatile 防止编译器优化重排序
         * 线程的working memory只是cpu的寄存器和高速缓存的抽象描述
         * 使用volatile修饰的变量不会缓冲在寄存器或其他处理器的缓存中，
         * 所以一个线程对其的写操作，会刷新到主存，其他读线程读取这个变量总会从主存更新
         * 但不能mutual exclude （互斥）
         */
        private volatile boolean stop = false;

        @Override
        public void run() {
            int i = 0;
//            无意义的循环会被优化
            while (!stop) {
                i++;
            }
            System.out.println(Thread.currentThread().getName() + "finsh r");

        }

        //此处停止不了的原因是编译器的优化，不是可见性导致的
        public void cancle() {
            System.out.println(Thread.currentThread().getName());
            stop = true;
        }
    }


}


