package com.example;

import java.math.BigInteger;

import static com.example.Utils.encodeIntoResponse;
import static com.example.Utils.extractFromRequest;
import static com.example.Utils.factor;

public class StatelessFactorizer {

    private BigInteger lastNumber;
    private BigInteger[] lastFactors;

    /**
     * 低效的线程安全
     * @param req
     * @param resp
     */
    public synchronized void service(ServletRequest req, ServletResponse resp) {
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber))
            encodeIntoResponse(resp, lastFactors);
        else {
            BigInteger[] factors = factor(i);
            lastNumber = i;
            lastFactors = factors;
            encodeIntoResponse(resp, factors);
        }
    }

    /**
     * 只同步导致线程不安全的可修改的公共变量
     * @param req
     * @param resp
     */
    public void service2(ServletRequest req, ServletResponse resp) {
        BigInteger requestInteger = extractFromRequest(req);
        BigInteger[] temp=null;
        synchronized (this) {
            if (requestInteger.equals(lastNumber)) {
                temp=lastFactors.clone();
            }
        }
        if (temp == null) {
            temp = factor(requestInteger);
            synchronized (this) {
                lastNumber=requestInteger;
                lastFactors=temp.clone();
            }
        }
        encodeIntoResponse(resp, temp);


    }

}
