package com.example;

import java.lang.annotation.Documented;

/**
 * Created by  on 2017/3/14
 * qq:1220289215
 */
@Documented
public @interface ClassPremable {
    String author();
    String data();
    int currentVersion() default  1;

    String lastModified() default "N/A";

    String lastModifiedBy() default "N/A";

    String[] reviewers();
}
