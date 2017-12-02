package com.example;


public class NoVisibility {
    private static boolean ready;
    private static int number;

    /**
     * 由于可见性问题，
     * 对ready的修改，如果对Reader线程不可见会导致死循环
     * 由于重排序问题，ready也可能在number之前被赋值
     * 如果主线程修改number为42对Reader线程不一定可见，打印可能为0
     * <p>
     * 使用volatile修饰的变量不会缓冲在寄存器或其他处理器的缓存中，
     * 所以一个线程的写操作总能被证其他读线程看见
     * 什么时候适合用volatile
     * a variable is suitable for being declared
     * volatile only if it does not participate in invariants involving other state variables.
     */
    private static class ReaderThread extends Thread {
        public void run() {
            while (!ready)
                Thread.yield();
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new ReaderThread().start();
        number = 42;
        ready = true;
    }
}