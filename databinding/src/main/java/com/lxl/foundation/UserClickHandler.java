package com.lxl.foundation;

import android.view.View;
import android.widget.Toast;

import com.lxl.foundation.entity.User;

/**
 * author:
 * 时间:2017/5/22
 * qq:1220289215
 * 类描述：处理UserActivity页面按钮的点击
 */

public class UserClickHandler {
    public void onShowInfoClick(View view, User user) {
        Toast.makeText(view.getContext(), user.getFirstName()+"--"+user.getAge(), Toast.LENGTH_SHORT).show();
    }
    public void onCheckChanged(View view, boolean isChecked) {
        if (isChecked) {
            Toast.makeText(view.getContext(), "checked", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(view.getContext(), "unchecked", Toast.LENGTH_SHORT).show();
        }
    }


}
