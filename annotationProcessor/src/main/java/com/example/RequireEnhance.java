package com.example;

/**
 * Created by  on 2017/3/14
 * qq:1220289215
 */

public @interface RequireEnhance {
    int id();

    String one();

    String engineer() default "[unsigned]";

    String date() default "[unknown]";
}
