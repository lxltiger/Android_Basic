package shine.com.advance.messenger;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import shine.com.advance.R;

/*使用message实现进程间通讯*/
public class MessengerActivity extends AppCompatActivity {
    private static final String TAG = "MessengerActivity";
    private Messenger service;
    final Messenger messenger = new Messenger(new InComingHandler());
    private boolean isBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = new Messenger(binder);
            try {
                // 向服务端注册客户端 以双向通信
                Message message = Message.obtain(null, MessengerService.MSG_REGISTER);
                message.replyTo = messenger;
                service.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            service = null;
        }
    };

    class InComingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_SET_VALUE:
                    Log.d(TAG, "msg.arg1:" + msg.arg1);
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        findViewById(R.id.btn_send_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBound) return;
                Message message = Message.obtain(null, MessengerService.MSG_SET_VALUE, 123, 0);
                try {
                    service.send(message);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }


            }
        });
    }

    /**
     * 如果从另外一个应用启动
     *  intent.setAction("android.intent.action.messenger.service");
     *  intent.setPackage("shine.com.advance");
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MessengerService.class);
        bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            Message message = Message.obtain(null, MessengerService.MSG_UNREGISTER);
            message.replyTo = messenger;
            try {
                service.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(mConnection);
            isBound = false;
        }
    }
}
