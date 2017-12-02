package shine.com.advance.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/*
* 使用Messenger实现IPC
* android.intent.action.messenger.service
*/
public class MessengerService extends Service {
    public MessengerService() {
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    try {
                        msg.replyTo.send(Message.obtain(null,3, msg.arg1,msg.arg2));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
    private final Messenger mMessenger=new Messenger(mHandler);
    @Override
    public IBinder onBind(Intent intent) {

        return mMessenger.getBinder();
    }
}
