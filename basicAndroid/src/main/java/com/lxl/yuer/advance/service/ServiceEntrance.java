package com.lxl.yuer.advance.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lxl.yuer.advance.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * startService : 第一次启动服务会调用服务的onCreate()   和OnStartCommand（）
 * 以后启动服务只会执行OnStartCommand（）
 * Service 不会随Activity销毁而销毁
 * bindService ： 第一次启动会调用服务的 onCreate()   和OnBind() ，如果onBind返回null，不会执行OnServiceConnect()
 * 以后点击邦定不会执行任何操作，这种邦定的服务不会出现在设置页面中
 * 如果Activity销毁 Service 也随之销毁，所以在销毁时要解绑，否则和泄漏Connection,但只能解绑一次
 * 如果开启混合服务 先startService 保证服务在后台运行，再bindService 调用服务中方法
 * 关闭 是先unBindSerivce（） 再 stopService
 * 只有 Activity、服务和内容提供程序可以绑定到服务—您无法从广播接收器绑定到服务
 */
public class ServiceEntrance extends AppCompatActivity {
    private static final String TAG = "ServiceEntrance";
    private Intent mIntent;
    private boolean mBound;
    private boolean mBoundAdvance;
    private SimpleService.SimpleBinder mBinder;
    private AdvanceService.IService mAdvanceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_entrance2);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_start_service, R.id.btn_stop_service,
            R.id.btn_intent_service, R.id.btn_intent_service2,
            R.id.btn_intent_service3, R.id.btn_intent_service4,
            R.id.btn_bind_service, R.id.btn_advance_service,
            R.id.btn_call, R.id.btn_call_advance_method
    })
    public void onClick(View view) {
        switch (view.getId()) {
            //启动服务
            case R.id.btn_start_service:
                mIntent = new Intent(this, SimpleService.class);
                startService(mIntent);
                break;
            //手动停止服务
            case R.id.btn_stop_service:
                if (mIntent != null) {
                    stopService(mIntent);
                }
                break;
            case R.id.btn_intent_service:
                mIntent = new Intent(this, ArgumentService.class);
                mIntent.putExtra("name", "shine");
                startService(mIntent);
                break;
            case R.id.btn_intent_service2:
                mIntent = new Intent(this, ArgumentService.class);
                mIntent.putExtra("name", "shine_tiger");
                mIntent.putExtra("redeliver", true);
                startService(mIntent);
                break;
            case R.id.btn_intent_service3:
                mIntent = new Intent(this, ArgumentService.class);
                mIntent.putExtra("name", "Failure");
                mIntent.putExtra("fail", true);
                startService(mIntent);
                break;
            //模拟后台服务进程被终止
            case R.id.btn_intent_service4:
                Process.killProcess(Process.myPid());

                break;
            case R.id.btn_bind_service:
                mIntent = new Intent(this, SimpleService.class);
                mBound = bindService(mIntent, mServiceConnection, Service.BIND_AUTO_CREATE);
                break;
            case R.id.btn_call:
                if (mBound && mBinder != null) {
                    mBinder.call();
                } else {
                    Toast.makeText(this, "没有邦定服务", Toast.LENGTH_SHORT).show();
                }
                break;
            /**
             *  高级邦定,服务通过接口暴露公共方法，同时内部可调用私有方法
             */
            case R.id.btn_advance_service:
                mIntent = new Intent(this, AdvanceService.class);
                mBoundAdvance = bindService(mIntent, mAdvanceConnection, Service.BIND_AUTO_CREATE);
                break;
            case R.id.btn_call_advance_method:
                if (mBoundAdvance && mAdvanceService != null) {
                    mAdvanceService.play();
                } else {
                    Toast.makeText(this, "没有邦定高级服务", Toast.LENGTH_SHORT).show();
                }
                break;


        }
    }

    //一般邦定
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected() called with: " + "name = [" + name + "], service = [" + service + "]");
            mBinder = (SimpleService.SimpleBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected() called with: " + "name = [" + name + "]");
        }
    };

    //高级邦定
    ServiceConnection mAdvanceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected() called with: " + "name = [" + name + "], service = [" + service + "]");
            mAdvanceService = (AdvanceService.IService) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBound) {
            unbindService(mServiceConnection);
        }

        if (mBoundAdvance) {
            unbindService(mAdvanceConnection);
        }


    }
}
