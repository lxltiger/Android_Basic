package com.lxl.foundation;

import android.content.Context;
import android.content.Intent;
import android.view.View;


/**
 * author:
 * 时间:2017/5/19
 * qq:1220289215
 * 类描述：HomeActivity页面按钮点击监听
 */

public class HomeClickHandler {
    private Context mContext;

    public HomeClickHandler(Context context) {
        mContext = context;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                ((MainActivity)mContext).finish();
                break;
            case R.id.btn_data_bind:
                homeActivity2Another(UserActivity.class);
                break;
            case R.id.btn_observable:
                homeActivity2Another(ObservableActivity.class);
                break;
            case R.id.btn_collection:
                homeActivity2Another(CollectionActivity.class);
                break;
            case R.id.btn_source:
                homeActivity2Another(ResourceActivity.class);
                break;
            case R.id.btn_recycler:
                homeActivity2Another(DynamicActivity.class);
                break;
            case R.id.btn_conversion:
                homeActivity2Another(ConversionActivity.class);
                break;
        }
    }

    private void homeActivity2Another(Class cls) {
        Intent intent=new Intent(mContext,cls);
        mContext.startActivity(intent);
    }
}
