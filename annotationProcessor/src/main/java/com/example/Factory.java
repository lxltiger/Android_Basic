package com.example;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by  on 2017/3/18
 * qq:1220289215
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Factory {

    Class type();

    String id();
}
