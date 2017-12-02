package com.example.chapter7;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * 如果取消正在执行的线程
 */
//@ThreadSafe
public class PrimeGenerator implements Runnable {
//    @GuardedBy("this")
    private final List<BigInteger> primes= new ArrayList<BigInteger>();
    private volatile boolean cancelled;

    //这种使用标记取消线程的方式前提是线程是非阻塞的
    public void run() {
        BigInteger p = BigInteger.ONE;
        while (!cancelled) {
            p = p.nextProbablePrime();
            synchronized (this) {
                primes.add(p);
            }
        }
    }

    public void cancel() {
        cancelled = true;
    }

    public synchronized List<BigInteger> get() {
        return new ArrayList<BigInteger>(primes);
    }


    private List<BigInteger> one() throws InterruptedException {
        PrimeGenerator generator=new PrimeGenerator();
        new Thread(generator).start();
        try {
            Thread.sleep(1000);
        } finally {
            generator.cancel();
        }
        return generator.get();
    }
}