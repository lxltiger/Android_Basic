package com.lxl.foundation;

import java.util.Random;

/**
 * author:
 * 时间:2017/5/24
 * qq:1220289215
 * 类描述：
 */

public class Utils {
    public static final String[] FIRST_NAMES = {"Zhao", "Qian", "Sun", "Li", "Zhou", "Wu"};
    public static final String[] LAST_NAMES = {"Tiedan", "Ritian", "LiangChen"};

    private static final Random sRandom = new Random();

    public static String getRandomFirstName() {
        return FIRST_NAMES[sRandom.nextInt(FIRST_NAMES.length)];
    }

    public static String getRandomLastName() {
        return LAST_NAMES[sRandom.nextInt(LAST_NAMES.length)];
    }
}
