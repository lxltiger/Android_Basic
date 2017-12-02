package com.example;

import java.math.BigInteger;

/**
 * Created by 李晓林 on 2016/12/20
 * qq:1220289215
 */

public class VolatileCachedFactorizer {
    /**
     *
     */
    private  volatile OneValueCache mOneValueCache=new OneValueCache(null,null);

    public void service(ServletRequest req, ServletResponse resp) {
        BigInteger bigInteger = Utils.extractFromRequest(req);
        BigInteger[] factors = mOneValueCache.getFactors(bigInteger);
        if (factors == null) {
           factors = Utils.factor(bigInteger);
            mOneValueCache = new OneValueCache(bigInteger, factors);
        }
        Utils.encodeIntoResponse(resp, factors);
    }
}
