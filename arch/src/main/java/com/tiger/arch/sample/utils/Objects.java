package com.tiger.arch.sample.utils;

import java.util.Arrays;

public class Objects {

    public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));

    }

    public static int hash(Object o) {
        return o != null ? o.hashCode() : 0;
    }

    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }

}
