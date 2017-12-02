package com.lxl.yuer.advance.date;


import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.lxl.yuer.advance.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * A simple {@link Fragment} subclass.
 * 含有日期和时间选择器的Fragment
 */
public class DateFragment extends DialogFragment implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {
    private static final String DATE_START="date_start";
    private static final String DATE_CURRENT="date_current";
    //DateFragment的tag标志，用来识别显示日期还是时间
    public static final String DATE = "date";
    public static final String TIME = "time";
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;
    private boolean mShowDatePicker=true;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mMinute;
    private int mHour;



    /**
     *DateFragment 实例化
     * @param start 日期选择器的起始日期
     * @param current 日期选择器当前显示的日期
     * @return
     */
    public static DateFragment newInstance(Date start, Date current) {
        DateFragment fragment = new DateFragment();
        Bundle args = new Bundle();
        args.putSerializable(DATE_START, start);
        args.putSerializable(DATE_CURRENT, current);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 初始化当前要显示的年月
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Date current = (Date) getArguments().getSerializable(DATE_CURRENT);
        Calendar calendar=Calendar.getInstance();
        //显示指定要显示的日期，否则显示当前日期
        if (current != null) {
            calendar.setTime(current);
        }else {
            calendar.setTime(new Date());
        }
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        mMinute = calendar.get(Calendar.MINUTE);
        mHour = calendar.get(Calendar.HOUR_OF_DAY);
        //要显示时间还是日期的标记
        String tag=getTag();
        if (tag.equals(DATE)) {
            mShowDatePicker = true;
        }else{
            mShowDatePicker=false;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //获取自定义的View，其包含Toolbar，DatePicker，TimePicker，DatePicker和TimePicker有一个不可见
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_date_time_picker, null);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mDatePicker = (DatePicker) view.findViewById(R.id.datePicker);
        mTimePicker = (TimePicker) view.findViewById(R.id.timePicker);
        //初始化toolbar时根据mShowDatePicker来决定显示DatePicker还是TimePicker
        initToolbar(toolbar);
        //初始化日期选择器，是否设定起始日期和显示指定日期，并监听变动
        initDatePicker();
        //初始化时间选择器，并监听更改
        initTimePicker();
        builder.setView(view);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //构建当前日期返回给TargetFragment，即ResultFragment
                Date result = new GregorianCalendar(mYear, mMonth, mDay, mHour, mMinute).getTime();
                if (getTargetFragment() != null) {
                    Intent intent = new Intent();
                    intent.putExtra("result", result);
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                }
            }
        });
        return builder.create();
    }

    /**
     * 初始化日期选择器
     *
     */
    private void initDatePicker() {
         Date startDate= (Date) getArguments().getSerializable(DATE_START);
        if (startDate != null) {
            //如果startDate不为空设置起始日期
            mDatePicker.setMinDate(startDate.getTime());
        }
        //使用当前日期初始化日期选择器
        mDatePicker.init(mYear, mMonth, mDay, this);
    }

    @SuppressWarnings("deprecation")
    private void initTimePicker() {
        //24小时制显示
        mTimePicker.setIs24HourView(true);
        mTimePicker.setCurrentHour(mHour);
        mTimePicker.setCurrentMinute(mMinute);
        mTimePicker.setOnTimeChangedListener(this);
    }


    private void initToolbar(Toolbar toolbar) {
        toolbar.setSubtitle("选择时间或日期");
        //填充menu
        toolbar.inflateMenu(R.menu.fragment_date);
        //找到切换按钮
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.toggle);
        //初始化切换按钮和界面
        toggle(menuItem);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toggle:
                        //切换DatePicker和TimePicker的显示状态
                        mShowDatePicker = !mShowDatePicker;
                        toggle(item);
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * 更新界面
     * @param item toolbar的menuItem
     */
    private void toggle(MenuItem item) {
        if (mShowDatePicker) {
            mDatePicker.setVisibility(View.VISIBLE);
            mTimePicker.setVisibility(View.INVISIBLE);
            item.setTitle("选择时间");
        } else {
            mDatePicker.setVisibility(View.INVISIBLE);
            mTimePicker.setVisibility(View.VISIBLE);
            item.setTitle("选择日期");
        }
    }

    /**
     *监听日期变化
     * @param view
     * @param year new year user choose
     * @param monthOfYear new month user choose base 0
     * @param dayOfMonth new day user choose
     */
    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
    }

    /**
     * 监听时间变化
     * @param view
     * @param hourOfDay
     * @param minute
     */
    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        mHour=hourOfDay;
        mMinute=minute;
    }
}
