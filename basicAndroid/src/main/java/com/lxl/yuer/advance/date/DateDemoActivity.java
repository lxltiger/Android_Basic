package com.lxl.yuer.advance.date;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.lxl.yuer.advance.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Date表示特定瞬间,精确到毫秒一天是 24 * 60 * 60 = 86400 秒
 * 世界时(UT 或 UTC) , 格林威治时间(GMT), 格林威治时(GMT)和世界时(UT) 是相等的，
 * 格林威治时(GMT) 是标准的"民间"称呼, 世界时(UT) 是相同标准的科学称呼。
 * UTC 和 UT 的区别是：UTC 是基于原子时钟的，UT 是基于天体观察的。
 *
 *
 */
public class DateDemoActivity extends AppCompatActivity {
    //DateFormat 是日期时间格式化的抽象类，它以语言无关的方式解析日期
    //SimpleDateFormat是DateFormat的直接子类，它以语言相关的方式解析日期，（格式化）日期-->文本，（解析）文本-->日期
    //SimpleDateFormat 可以通过用户自定义的方式格式化日期
    ////更为自由的格式，嵌入字符、换行等,字符要用''包含
   // mDateFormatCustom.applyPattern("yyyy'年'M'月'd'日'\nh'时'm'分'mm'秒'");
    // 如果使用mDateFormatDefault.parse(String date)中解析date，样式要与date一样
    DateFormat mDateFormatCustom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss EE", Locale.CHINA);

    //建议通过 DateFormat 中的 getTimeInstance、getDateInstance 或 getDateTimeInstance 来新的创建日期-时间格式化程序。
    //无参的getDateInstance，使用的Date和time 的style为medium，Local为本机设置
    //DateFormat 不是同步的。建议为每个线程创建独立的格式实例。如果多个线程同时访问一个格式，则它必须保持外部同步
    DateFormat mDateFormatDefault = DateFormat.getDateTimeInstance(DateFormat.FULL,DateFormat.FULL,Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_demo);
        FragmentManager fragmentManager=getSupportFragmentManager();
        ResultDateFragment fragment = (ResultDateFragment) fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            Log.d("DateDemoActivity", "fragment is null");
            fragment = new ResultDateFragment();
            fragmentManager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        initDate();
        initCalendar();
    }

    /**
     * Date常用的构造方法 : Date(long date) date表示从标准基准时间
     * （称为 "历元" epoch ，即1970.1.1 00:00:00 GMT）经历的毫秒数。
     * Date();无参的构造函数 调用的是 this(System.currentTimeMillis());
     * 表示当前从基准时间经历的毫秒数
     * boolean after(Date when) 测试此日期是否在指定日期之后。
     * boolean before(Date when) 测试此日期是否在指定日期之前。
     * long getTime() 返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
     * void setTime(long time) 设置此 Date 对象，以表示 1970 年 1 月 1 日 00:00:00 GMT 以后 time 毫秒的时间点。time参数要加'L'表示长整型
     */
    private void initDate() {
        Date date_default=new Date();
        TextView textView = (TextView) findViewById(R.id.tv_date);
        String format_default = mDateFormatDefault.format(date_default);
        String format_custom = mDateFormatCustom.format(date_default);
        textView.setText(String.format("当前日期\n 自定义格式化: %s \n默认格式化%s", format_custom, format_default));
    }

    /**
     * Calendar是个抽象类，是系统时间的抽象表示，
     * 它为特定瞬间与一组诸如 YEAR、MONTH、DAY_OF_MONTH、HOUR 等 日历字段之间的转换提供了一些方法，
     * 并为操作日历字段（例如获得下星期的日期）提供了一些方法。
     * 瞬间可用毫秒值来表示，它是距历元（即格林威治标准时间 1970 年 1 月 1 日的 00:00:00.000，格里高利历）的偏移量。
     * 与其他语言环境敏感类一样，Calendar 提供了一个类方法 getInstance，
     * 以获得此类型的一个通用的对象，其日历字段已由当前日期和时间初始化。
     * Calendar的星期是从周日开始的，常量值为1。
     * Calendar的月份是从一月开始的，常量值为0。
     * Calendar的每个月的第一天值为1。
     */


    private void initCalendar() {
        //获取当前时间的日历，使用默认的时区和本地
        Calendar calendar_current=Calendar.getInstance();
        Calendar calendar_custom = new GregorianCalendar(2014, 5, 5, 4, 52, 45);
        Calendar calendar_custom_zone = new GregorianCalendar(TimeZone.getTimeZone("GMT-8:00"));
        TextView textView = (TextView) findViewById(R.id.tv_calender);
        //以当前日期作为参数
        calendar_current.setTime(new Date());
        //以当前毫秒作为参数
        calendar_current.setTimeInMillis(System.currentTimeMillis());
        String format_current = mDateFormatDefault.format(calendar_current.getTime());
        String format_custom = mDateFormatCustom.format(calendar_custom.getTime());
        String format_custom_zone = mDateFormatCustom.format(calendar_custom_zone.getTime());
        textView.setText(String.format("自定义日期: %s \n当前日期格式化%s \n 当前含时区 %S", format_custom, format_current, format_custom_zone));
        int firstDayOfWeek = calendar_current.get(Calendar.DAY_OF_WEEK)-1;
        Log.d("DateDemoActivity", "firstDayOfWeek:" + firstDayOfWeek);
        int day_of_year = calendar_current.get(Calendar.DAY_OF_YEAR);
        Log.d("DateDemoActivity", "day_of_year:" + day_of_year);
        int day_of_week_in_month = calendar_current.get(Calendar.DAY_OF_WEEK_IN_MONTH);
        Log.d("DateDemoActivity", "day_of_week_in_month:" + day_of_week_in_month);
        int day_of_week = calendar_current.get(Calendar.DAY_OF_WEEK);
        Log.d("DateDemoActivity", "day_of_week:" + day_of_week);
        int week_of_year = calendar_current.get(Calendar.WEEK_OF_YEAR);
        Log.d("DateDemoActivity", "week_of_year:" + week_of_year);

        calendar_current.add(Calendar.DAY_OF_MONTH, -1);
        Log.d("DateDemoActivity", "yesterday is" + mDateFormatDefault.format(calendar_current.getTime()));
        calendar_current.add(Calendar.DAY_OF_MONTH, 2);
        Log.d("DateDemoActivity", "tomorrow is" + mDateFormatDefault.format(calendar_current.getTime()));
        //当月最大天数
        int max_day = calendar_current.getActualMaximum(Calendar.DAY_OF_MONTH);
        int min_day = calendar_current.getActualMinimum(Calendar.DAY_OF_MONTH);
        Log.d("DateDemoActivity", "当月最大天数:" + max_day+"\n当月最小天数"+min_day);
        //当月最后一天
        DateFormat dateFormat_lastDay = new SimpleDateFormat("yyyy-MM-" + max_day, Locale.CHINA);
        Log.d("DateDemoActivity", "当月最后一天:" + dateFormat_lastDay.format(calendar_current.getTime()));
        DateFormat dateFormat_firstDay = new SimpleDateFormat("yyyy-MM-01", Locale.CHINA);
        Log.d("DateDemoActivity", "当月第一天:" + dateFormat_firstDay.format(calendar_current.getTime()));
        //两个日期相隔天数
        long day_margin = (calendar_current.getTimeInMillis() - calendar_custom.getTimeInMillis()) / (24 * 3600 * 1000);
        Log.d("DateDemoActivity", "相隔天数"+day_margin);
    }
}
