package com.lxl.tiger.designpattern.annotation;

import java.util.ArrayList;
import java.util.List;

public class Sample2 {

    @ExceptionTest(ArithmeticException.class)
    public static void devide() {
        int i = 23 / 0;
    }

    @ExceptionTest(ArithmeticException.class)
    public static void m2() {
        int[] a = new int[0];
        int i = a[1];
    }

    @ExceptionTest(ArithmeticException.class)
    public static void m3() { }

    @ExceptionTest({ IndexOutOfBoundsException.class,NullPointerException.class })
    public static void doublyBad() {
        List<String> list = new ArrayList<>();
// The spec permits this method to throw either
// IndexOutOfBoundsException or NullPointerException
        list.addAll(5, null);
    }
}
