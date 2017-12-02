package com.lxl.yuer.advance.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lxl.yuer.advance.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import shine.com.advance.IService;
import shine.com.advance.IServiceCallBack;

/**
 * 邦定远程服务，aidl的使用，使用接口暴露的方法只能在应用内即进程内调用，如果要进程间通信就要aidl
 * 1.在提供服务的程序中创建aidl文件夹，默认使用包名作为路径创建aidl文件，使用make project （ctrl+F9）更新工程，这样就能使用
 * 自动生成的接口文件
 * 2.创建service子类，在清单文件中提供隐式调用的action name，创建内部类继承aidl的stub从而成为IBinder的子类，其后操作同一般服务邦定
 * 3.在调用服务的程序中，同样创建aidl文件夹，其下的文件路和上述提供服务的aidl文件包名，文件名一致，同样make project
 * 4.开始邦定服务，新建的intent需要设置提供service在清单文件的action name 和 提供服务的应用的packageName
 * 5.页面销毁取消邦定
 *
 * 5.0以后隐式启动service会报错，必须设置服务的报名
 */

public class RemoteEntrance extends AppCompatActivity {

    @Bind(R.id.tv_status)
    TextView mTvStatus;
    private boolean mServiceBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_entrance);
        ButterKnife.bind(this);
        mTvStatus.setText("unattached");
    }

    @OnClick({R.id.btn_start_remote_service, R.id.btn_bind_remote_method,R.id.btn_kill_remote_method})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start_remote_service: {
                Intent intent = new Intent();
                intent.setAction("shine.com.advance.IService");
                intent.setPackage("shine.com.advance");
                startService(intent);
                Toast.makeText(this, "start service", Toast.LENGTH_SHORT).show();
            }
                break;
            //使用隐士调用两个远程服务
            case R.id.btn_bind_remote_method:
                Intent intent = new Intent();
                Log.d("RemoteEntrance", IService.class.getName());
                intent.setAction(IService.class.getName());
                intent.setPackage("shine.com.advance");
                mServiceBound = bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
                Log.d("RemoteEntrance", "mServiceBound:" + mServiceBound);
                mTvStatus.setText("binding");

                break;
            case R.id.btn_kill_remote_method:

                break;
        }
    }

    private IService mService = null;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IService.Stub.asInterface(service);
            mTvStatus.setText("attach");
            try {
                mService.register(mCallBack);
//                mService.play();
            } catch (RemoteException e) {
                Log.d("RemoteEntrance", "service register exception");
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mTvStatus.setText("disconect");
        }
    };


    //接受服务端的回调
    private IServiceCallBack mCallBack = new IServiceCallBack.Stub() {
        @Override
        public void onChange(int value) throws RemoteException {
            //进程间通信是在线程池中调用，不在UI线程
            Log.d("RemoteEntrance", "value:" + value);
            mHandler.sendMessage(mHandler.obtainMessage( 1, value, 0));
        }
    };
    private Handler mHandler = new Handler() {
        @Override public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mTvStatus.setText("Received from service: " + msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }

    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除邦定
        if (mServiceBound) {
            //取消回调
            if (mService != null) {
                try {
                    mService.unregister(mCallBack);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            unbindService(mServiceConnection);
            mServiceBound=false;
        }
        //如果所有邦定没有接触，无法停止服务
        Intent intent = new Intent();
        intent.setAction("android.intent.action.remote.service");
        intent.setPackage("shine.com.advance");
        stopService(intent);
    }
}
