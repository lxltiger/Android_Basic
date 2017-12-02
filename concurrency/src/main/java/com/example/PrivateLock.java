package com.example;

/**
 * 原文关于使用私用同步锁的好处
 * There are advantages to using a private lock object instead of an object’s intrinsic
 * lock (or any other publicly accessible lock). Making the lock object private
 * encapsulates the lock so that client code cannot acquire it, whereas a publicly
 * accessible lock allows client code to participate in its synchronization policy—
 * correctly or incorrectly. Clients that improperly acquire another object’s lock
 * could cause liveness problems, and verifying that a publicly accessible lock is
 * properly used requires examining the entire program rather than a single class.
 * 主要好处就是同步之能在此类中使用
 * 在一个外部类不可能使用 synchronized (myLock) 方法获取锁来操作此类代码
 */
public class PrivateLock {
    private final Object myLock = new Object();
    ServletRequest widget;

    /**
     * 使用私用锁
     */
    void someMethod() {
        synchronized (myLock) {
            // Access or modify the state of widget
        }
    }

    /**
     * 使用this锁，对象自带的 可以被其他对象获取
     */
     synchronized void anotherMethod() {
        System.out.println("another one");
    }

    /**
     *在一个外部类开启两个线程
     * 如果其中一个获取对象自带的this锁执行耗时操作
     * 就会导致其他尝试获取this锁的线程阻塞
     * 这种公共锁就会因为外部类的不正确操作导致巨大不确定性
     */
    static class Test {
        public static void main(String[] args) {
            final PrivateLock privateLock = new PrivateLock();
            new Thread() {
                @Override
                public void run() {
                    synchronized (privateLock) {
                        System.out.println("get lock to sleep to mock long-time running");
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        System.out.println("when wake from sleep,execute another");
                        //方法阻塞，因为锁已经被前一个线程获取
                        privateLock.anotherMethod();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

        }
    }

}
