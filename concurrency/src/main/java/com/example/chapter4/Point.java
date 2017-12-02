package com.example.chapter4;

//@Immutable

/**
 * 通过对象不可变来保证线程安全
 */
public class Point {
    public final int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}