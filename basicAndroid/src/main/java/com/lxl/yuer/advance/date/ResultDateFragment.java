package com.lxl.yuer.advance.date;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lxl.yuer.advance.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * 用来接收返回的日期和时间
 * 对同一日期进行三种不同方式的格式化
 */
public class ResultDateFragment extends Fragment implements View.OnClickListener {
    //日期按钮请求码
    private static final int DATE = 1;
    //时间按钮请求码
    private static final int TIME = 2;
    //日期时间按钮请求码
    private static final int DATE_TIME = 3;

    private Button mButtonDate;
    private Button mButtonTime;
    private Button mButtonDateTime;
    //DatePicker起始日期
    private Date mDateStart;
    //DatePicker要显示的日期，没指定就是当前日期
    private Date mDateCurrent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_result_date, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButtonDate = (Button) view.findViewById(R.id.btn_get_date);
        mButtonTime = (Button) view.findViewById(R.id.btn_get_time);
        mButtonDateTime = (Button) view.findViewById(R.id.btn_get_date_time);
        mButtonDate.setOnClickListener(this);
        mButtonTime.setOnClickListener(this);
        mButtonDateTime.setOnClickListener(this);
        init();

    }

    private void init() {
        //起始日期设为当前时间
        mDateStart = new Date();
        //以三种方式格式化日期
        formatDate(mButtonDate, mDateStart);
        formatTime(mButtonTime, mDateStart);
        formatDateTime(mButtonDateTime, mDateStart);
        //设定要显示的日期为6个月后的日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 6);
        mDateCurrent = calendar.getTime();
    }

    /**
     * DEFAULT	Jun 30, 2009	30 juin 2009
     * SHORT	6/30/09	30/06/09
     * MEDIUM	Jun 30, 2009	30 juin 2009
     * LONG	June 30, 2009	30 juin 2009
     * FULL	Tuesday, June 30, 2009	mardi 30 juin 2009
     *
     * @param button
     * @param date
     */
    private void formatDate(Button button, Date date) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.LONG);
        button.setText(dateFormat.format(date));
    }

    /**
     * Style	U.S. Locale	German Locale
     * DEFAULT	7:03:47 AM	7:03:47
     * SHORT	7:03 AM	    07:03
     * MEDIUM	7:03:47 AM	07:03:07
     * LONG	7:03:47 AM PDT	07:03:45 PDT
     * FULL	7:03:47 AM PDT	7.03 Uhr PDT
     * @param button
     * @param date
     */
    private void formatTime(Button button, Date date) {
        DateFormat dateFormat = DateFormat.getTimeInstance();
        button.setText(dateFormat.format(date));
    }

    /**
     * Style	U.S. Locale	            French Locale
     * DEFAULT	Jun 30, 2009 7:03:47 AM	    30 juin 2009 07:03:47
     * SHORT	6/30/09 7:03 AM	            30/06/09 07:03
     * MEDIUM	Jun 30, 2009 7:03:47 AM	      30 juin 2009 07:03:47
     * LONG	June 30, 2009 7:03:47 AM PDT	    30 juin 2009 07:03:47 PDT
     * FULL	Tuesday, June 30, 2009 7:03:47 AM PDT	mardi 30 juin 2009 07 h 03 PDT
     *
     * @param button
     * @param date
     */
    private void formatDateTime(Button button, Date date) {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        button.setText(dateFormat.format(date));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_get_date:
                showDatePickerDialog(mDateStart, mDateCurrent, DATE, DateFragment.DATE);
                break;
            case R.id.btn_get_time:
                showDatePickerDialog(mDateStart, mDateCurrent, TIME, DateFragment.TIME);
                break;
            case R.id.btn_get_date_time:
                showDatePickerDialog(mDateStart, mDateCurrent, DATE_TIME, DateFragment.DATE);
                break;
        }
    }

    /**
     *
     * @param start 起始日期
     * @param current 当前日期
     * @param requestCode 请求码
     * @param tag 初始化时显示日期还是时间标记
     */
    private void showDatePickerDialog(Date start, Date current, int requestCode, String tag) {
        DateFragment fragment = DateFragment.newInstance(start, current);
        fragment.setTargetFragment(this, requestCode);
        fragment.show(getActivity().getSupportFragmentManager(), tag);
    }

    /**
     * 格式化返回的日期值
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Date date_result = (Date) data.getSerializableExtra("result");
            switch (requestCode) {
                case DATE:
                    formatDate(mButtonDate, date_result);
                    break;
                case TIME:
                    formatTime(mButtonTime, date_result);
                    break;
                case DATE_TIME:
                    formatDateTime(mButtonDateTime, date_result);
                    break;
            }
        }
    }
}
