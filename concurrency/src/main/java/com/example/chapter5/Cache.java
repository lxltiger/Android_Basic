package com.example.chapter5;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import static com.example.Utils.launderThrowable;

/**
 * Created by 李晓林 on 2016/12/22
 * qq:1220289215
 */

public class Cache {
    public interface Compute<A, C> {
        C compute(A arg) throws InterruptedException;
    }

    public class ExpensiveCompution implements Compute<String, BigInteger> {
        @Override
        public BigInteger compute(String arg) throws InterruptedException {
            //after a long computation

            return new BigInteger(arg);
        }
    }

    /**
     * 缓冲处理过的数据
     * 使用自带的锁同步，并发效率低
     * @param <A>
     * @param <C>
     */
    public class Memoizer1<A, C> implements Compute<A, C> {
        private final HashMap<A, C> mCached = new HashMap<>();
        private final Memoizer1<A,C> mMemoizer1;

        public Memoizer1(Memoizer1<A, C> memoizer1) {
            mMemoizer1 = memoizer1;
        }
        //由于HashMap不是同步的
        @Override
        public synchronized C compute(A arg) throws InterruptedException {
            C c = mCached.get(arg);
            if (c == null) {
                c = mMemoizer1.compute(arg);
                mCached.put(arg, c);
            }
            return c;
        }
    }

    /**
     * 使用ConcurrentHashMap来处理数据并发修改和读取
     * 缺陷是可能同时进行相同的耗时计算，违背缓存目的，
     * @param <A>
     * @param <C>
     */
    class Memorizer2<A, C> implements Compute<A, C> {
        private final ConcurrentHashMap<A, C> mCached = new ConcurrentHashMap<>();
        private final Compute<A, C> mMerialer2;

        public Memorizer2(Compute<A, C> compute) {
            mMerialer2 = compute;
        }

        @Override
        public C compute(A arg) throws InterruptedException {
            C c = mCached.get(arg);
            if (c == null) {
                c = mMerialer2.compute(arg);
                mCached.put(arg, c);
            }
            return c;
        }
    }

    /**
     * 使用Future来避免计算相同的请求
     * 但由于缓存过程不是同步的，还有微小的可能出现相同的计算
     * 而且如果计算被取消 也会缓存，这样后续获取的结果都是错误的
     * @param <A>
     * @param <C>
     */
    class Memorizer3<A, C> implements Compute<A, C> {
        private final ConcurrentMap<A, Future<C>> mCached = new ConcurrentHashMap<>();
        private final Compute<A,C> mMemorizer3;

        public Memorizer3(Compute<A, C> compute) {
            mMemorizer3 = compute;
        }

        @Override
        public C compute(final A arg) throws InterruptedException {
            Future<C> cFutureTask = mCached.get(arg);
            if (cFutureTask == null) {
                FutureTask<C> futureTask=new FutureTask<>(new Callable<C>() {
                    @Override
                    public C call() throws Exception {
                        return  mMemorizer3.compute(arg);
                    }
                });
                cFutureTask=futureTask;
                mCached.put(arg, futureTask);

                //开始计算
                futureTask.run();
            }

            try {
                return cFutureTask.get();
            } catch (ExecutionException e) {
                throw launderThrowable(e.getCause());
            }
        }
    }

    /**
     * 终极解决方案
     * 原文在复写的Computer使用的while（true）无限循环，不理解
     * @param <A>
     * @param <C>
     */
    class Memorizer<A, C> implements Compute<A, C> {
        private final ConcurrentMap<A, Future<C>> mCached = new ConcurrentHashMap<>();
        private final Compute<A,C> mMemorize;

        public Memorizer(Compute<A, C> memorize) {
            mMemorize = memorize;
        }

        @Override
        public C compute(final A arg) throws InterruptedException {
            Future<C> cFuture = mCached.get(arg);
            if (cFuture == null) {
                FutureTask<C> futureTask=new FutureTask<C>(new Callable<C>() {
                    @Override
                    public C call() throws Exception {
                        return mMemorize.compute(arg);
                    }
                });
                //如果缓存没有就缓存起来，这样就杜绝任何相同计算的可能
                cFuture = mCached.putIfAbsent(arg, futureTask);
                if (cFuture == null) {
                    cFuture=futureTask;
                    futureTask.run();
                }

            }
            try {
                return cFuture.get();
            } catch (CancellationException e) {
                //如果计算被取消，移除无效值
                mCached.remove(arg, cFuture);
                return null;
            }catch (ExecutionException e) {
                throw launderThrowable(e.getCause());
            }
        }

    }

}
