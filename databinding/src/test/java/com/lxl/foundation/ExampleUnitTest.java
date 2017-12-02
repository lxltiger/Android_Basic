package com.lxl.foundation;


import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void isDateValid() throws ParseException {
        //判断当前是否在指定日期内
        String startDate="2017-09-29 00:00:00";
        String endDate="2017-09-29 23:59:59";
        Date date_today = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String string_today = dateFormat.format(date_today);
        System.out.println("date tiem"+string_today);
        Date dateStart = dateFormat.parse(startDate);
        Date dateEnd = dateFormat.parse(endDate);
        int i = date_today.compareTo(dateStart);
        int i2 = date_today.compareTo(dateEnd);
        System.out.println(i+"margin "+i2);


    }

}