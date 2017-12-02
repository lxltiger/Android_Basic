package com.lxl.yuer.advance.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lxl.yuer.advance.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessengerEntrance extends AppCompatActivity {
    @Bind(R.id.tv_status)
    TextView mTvStatus;
    private Messenger mService = null;
    private boolean mbound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_service_entrance);
        ButterKnife.bind(this);
        mTvStatus.setText("unattached");
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    mTvStatus.setText("recevier message from server"+msg.arg1+"---"+msg.arg2);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
    private final Messenger mMessenger = new Messenger(mHandler);

    @OnClick({R.id.btn_messenger_service, R.id.btn_call_messenger_method})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_messenger_service:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.messenger.service");
                intent.setPackage("shine.com.advance");
                mbound = bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
                break;
            case R.id.btn_call_messenger_method:
                unBind();
                break;
        }
    }

    private void unBind() {
        if (mbound) {
            if (mService != null) {
                try {
                    Message message = Message.obtain(null, 2);
                    message.replyTo=mMessenger;
                    mService.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            unbindService(mServiceConnection);
            mbound=false;
            mTvStatus.setText("unbind");
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //服务端的信使
            mService = new Messenger(service);
            mTvStatus.setText("attached");
            try {
                Message message =Message.obtain(null,3,this.hashCode(),4);
                //设置消息的接受者为客户端的信使
                message.replyTo=mMessenger;
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService=null;
            mTvStatus.setText("disconnect");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBind();
    }
}
