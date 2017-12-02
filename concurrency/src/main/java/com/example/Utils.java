package com.example;

import java.math.BigInteger;

/**
 * Created by 李晓林 on 2016/12/20
 * qq:1220289215
 */

public class Utils {
    private Utils() {

    }
    public static BigInteger[] factor(BigInteger i) {
        return new BigInteger[0];
    }

    public static void encodeIntoResponse(ServletResponse resp, BigInteger[] lastFactors) {

    }

    public static BigInteger extractFromRequest(ServletRequest req) {
        return null;
    }

    /** If the Throwable is an Error, throw it; if it is a
     * RuntimeException return it, otherwise throw IllegalStateException
     */
    public static RuntimeException launderThrowable(Throwable t) {
        if (t instanceof RuntimeException)
            return (RuntimeException) t;
        else if (t instanceof Error)
            throw (Error) t;
        else
            throw new IllegalStateException("Not unchecked", t);
    }
}
