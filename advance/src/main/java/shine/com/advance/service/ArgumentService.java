package shine.com.advance.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import shine.com.advance.R;

/* 演示不同参数的service运行情况*/
public class ArgumentService extends Service {
    private static final String TAG = "ArgumentService";
    private MyHandler mHandler;
    private NotificationManager mNotificationManager;
    private int notificationId=3;
    private class MyHandler extends Handler{
        public MyHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle= (Bundle) msg.obj;
            String name = bundle.getString("name");
            int startId=msg.arg1;
            int flag=msg.arg2;
            Log.d(TAG, "handleMessage() called with: " + "msg = [" + msg + "]");
            Log.d(TAG, "handle message flag:" + flag);
            if ((flag & START_FLAG_REDELIVERY) == 0) {
                name="new cmd"+startId+name;
            }else{
                name="redeliver"+startId+name;
            }
            showNotification(name + startId);

            long endTime = System.currentTimeMillis() + 5*1000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                    }
                }
            }
            Log.d(TAG, "done with "+startId);
            mNotificationManager.cancel(notificationId);
            stopSelf(startId);

        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        HandlerThread handlerThread = new HandlerThread("background", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        mHandler = new MyHandler(handlerThread.getLooper());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle=intent.getExtras();
        Message message=mHandler.obtainMessage();
        message.arg1=startId;
        message.arg2=flags;
        message.obj=bundle;
        mHandler.sendMessage(message);
        Log.d(TAG, "send message :" + message.toString());
        Log.d(TAG, "onstart command flags:" + flags);
        if (bundle.getBoolean("fail",false)) {
            if ((flags & START_FLAG_RETRY) == 0) {
                Process.killProcess(Process.myPid());
            }
        }
        return bundle.getBoolean("redeliver", false)?START_REDELIVER_INTENT:START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.getLooper().quit();
        mNotificationManager.cancel(notificationId);
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void showNotification(String content) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("title")
                        .setContentText(content);

        // Make something interesting happen when the user clicks on the notification.
        // In this case, opening the app is sufficient.
        Intent resultIntent = new Intent(this, StartServiceActivity.class);
        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);

        // WEATHER_NOTIFICATION_ID allows you to update the notification later on.
        mNotificationManager.notify(notificationId, mBuilder.build());

    }

}
