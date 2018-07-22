package shine.com.advance;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

/*使用messenger 实现进程间通讯*/
public class MessengerService extends Service {

    class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(MessengerService.this, "hello", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }

    private Messenger mMessenger = new Messenger(new MessengerHandler());

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("MessengerService", "bind");
        return mMessenger.getBinder();
    }
}
