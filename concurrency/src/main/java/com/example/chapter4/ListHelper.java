package com.example.chapter4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *
 *
 * 通过帮助类扩展功能
 * 以下写法不能正确同步，即使 list 和putIfAbsent都是同步的，但他们不在一个锁上
 */
public class ListHelper<E> {
    public List<E> list =  Collections.synchronizedList(new ArrayList<E>());

    //使用this锁 导致线程不安全@NotThreadSafe
    public synchronized boolean putIfAbsent(E x) {
        boolean absent = !list.contains(x);
        if (absent)
            list.add(x);
        return absent;
    }
    //使用同一个锁， client-side locking，安全但脆弱
    public  boolean putIfAbsent2(E x) {

        synchronized (list) {
            boolean absent = !list.contains(x);
            if (absent) {
                list.add(x);
            }
            return absent;
        }
    }
}