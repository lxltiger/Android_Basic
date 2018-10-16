package com.lxl.yuer.advance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.lxl.yuer.advance.date.DateDemoActivity;
import com.lxl.yuer.advance.fragment.WaitingDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * this app will display some useful knowledge I collect
 */
public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        closeWaitingDialog();
    }



    private void jump(Class T) {

        Intent intent = new Intent(this, T);
        startActivity(intent);
    }

    @OnClick({R.id.btn_date_demo, R.id.btn_service_demo,R.id.btn_data_bind,
            R.id.btn_back, R.id.btn_messenger_service_demo, R.id.btn_remote_service_demo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_date_demo:
                jump(DateDemoActivity.class);
                break;
            case R.id.btn_service_demo:
                break;
            case R.id.btn_messenger_service_demo:
                break;
            case R.id.btn_remote_service_demo:
                break;
            case R.id.btn_data_bind:
                showWatingDialog();
                break;
        }
    }

    private void showWatingDialog() {
        DialogFragment dialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag("waiting");
        if (dialogFragment == null) {
            dialogFragment = WaitingDialog.newInstance("Waiting");
        }
        dialogFragment.show(getSupportFragmentManager(),"waiting");
    }

    private void closeWaitingDialog() {
        DialogFragment dialogFragment = (DialogFragment) getSupportFragmentManager().findFragmentByTag("waiting");
        if (dialogFragment != null) {
            dialogFragment.dismiss();
        }
    }
}
