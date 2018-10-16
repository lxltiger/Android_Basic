package shine.com.advance.aidl;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import shine.com.advance.ISecondary;
import shine.com.advance.IService;
import shine.com.advance.IServiceCallBack;

/*
 * aidl的演示
 * 只有允许不同应用的客户端用 IPC 方式访问服务，并且想要在服务中处理多线程时，才有必要使用 AIDL。
 * 如果您不需要执行跨越不同应用的并发 IPC，就应该通过实现一个 Binder 创建接口；
 * 或者，如果您想执行 IPC，但根本不需要处理多线程，则使用 Messenger 类来实现接口
 */
public class RemoteService extends Service {
    private static final String TAG = "RemoteService";
    int value = 0;

    final RemoteCallbackList<IServiceCallBack> mCallbackList = new RemoteCallbackList<>();

    public RemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind() called with: " + "intent = [" + intent + "]");
        String action = intent.getAction();
        if (IService.class.getName().equals(action)) {
            return mServiceBinder;
        } else if (ISecondary.class.getName().equals(action)) {
            return secondaryBinder;
        }
        return null;


    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    ++value;
                    Log.d("RemoteService", "value:" + value);
                    int size = mCallbackList.beginBroadcast();
                    Log.d(TAG, "size:" + size);
                    for (int i = 0; i < size; i++) {
                        try {
                            mCallbackList.getBroadcastItem(i).onChange(value);
                        } catch (RemoteException e) {
                            // The RemoteCallbackList will take care of removing
                            // the dead object for us.
                        }
                    }
                    mCallbackList.finishBroadcast();
                    mHandler.sendMessageDelayed(obtainMessage(1), 1000);
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

    private final IService.Stub mServiceBinder = new IService.Stub() {
        @Override
        public void register(IServiceCallBack cb) throws RemoteException {
            Log.d(TAG, "register cb");
            if (cb != null) {
                mCallbackList.register(cb);
            }
        }

        @Override
        public void unregister(IServiceCallBack cb) throws RemoteException {
            Log.d(TAG, "unregister cb");
            if (cb != null) {
                mCallbackList.unregister(cb);
            }
        }

    };

    private ISecondary.Stub secondaryBinder = new ISecondary.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public int getPid() throws RemoteException {
            return Process.myPid();
        }
    };


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Toast.makeText(this, "Task removed: " + rootIntent, Toast.LENGTH_LONG).show();
    }
}

