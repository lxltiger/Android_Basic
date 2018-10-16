package shine.com.advance.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/*
*使用AIDL会同时向服务端发送多个请求，服务端必须具有多线程处理能力
* 使用Messenger可以将客户端请求加入队列从而简化服务端处理
*
* 对于大多数应用，服务不需要执行多线程处理，因此使用 Messenger 可让服务一次处理一个调用。
* 如果您的服务必须执行多线程处理，则应使用 AIDL 来定义接口。
*
*/
public class MessengerService extends Service {
    private static final String TAG = "MessengerService";
    public static final int MSG_REGISTER=1;
    public static final int MSG_UNREGISTER=2;
    public static final int MSG_SET_VALUE=3;
    private List<Messenger> messengerList = new ArrayList<>();
    public MessengerService() {
    }

   private class IncomingHander extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER:
                    messengerList.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER:
                    messengerList.remove(msg.replyTo);
                    break;
                case MSG_SET_VALUE:
                    for (int i = messengerList.size() - 1; i >= 0; i--) {
                        try {
                            messengerList.get(i).send(Message.obtain(null, MSG_SET_VALUE, msg.arg1 * 2, 0));
                        } catch (RemoteException e) {
                            messengerList.remove(i);
                        }
                    }
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    private final Messenger mMessenger=new Messenger(new IncomingHander());
    @Override
    public IBinder onBind(Intent intent) {

        return mMessenger.getBinder();
    }
}
