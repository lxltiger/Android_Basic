package shine.com.advance.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import shine.com.advance.IService;
import shine.com.advance.IServiceCallBack;

/*
* aidl的演示
*/
public class RemoteService extends Service {
    private static final String TAG = "RemoteService";
    int value=0;

    final RemoteCallbackList<IServiceCallBack> mCallbackList=new RemoteCallbackList<>();
    public RemoteService() {}

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called with: " + "intent = [" + intent + "]");
        return mServiceBinder;


    }

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ++value;
                    Log.d("RemoteService", "value:" + value);
                    int size=mCallbackList.beginBroadcast();
                    Log.d(TAG, "size:" + size);
                    for (int i = 0; i < size; i++) {
                        try {
                            mCallbackList.getBroadcastItem(i).onChange(value);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    mCallbackList.finishBroadcast();
                    mHandler.sendMessageDelayed(obtainMessage(1),1000);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate() called with: " + "");
        mHandler.sendEmptyMessage(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消所有注册
        mCallbackList.kill();
        mHandler.removeMessages(1);
        Log.d("RemoteService", "remote service is down");
    }

    private final IService.Stub mServiceBinder=new IService.Stub() {
        @Override
        public void register(IServiceCallBack cb) throws RemoteException {
            if (cb != null) {
                Log.d(TAG, "register cb");
                mCallbackList.register(cb);
            }
        }

        @Override
        public void unregister(IServiceCallBack cb) throws RemoteException {
            if (cb != null) {
                Log.d(TAG, "unregister cb");
                mCallbackList.unregister(cb);
            }
        }

    };



    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Toast.makeText(this, "Task removed: " + rootIntent, Toast.LENGTH_LONG).show();
    }
}

