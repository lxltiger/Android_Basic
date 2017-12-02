package com.lxl.yuer.advance.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;


/**
 * 使用IntentService演示ApiDemo
 * 没有如预期运行，process重启没有redeliver
 */
public class ArgumentService extends IntentService {
    private static final String TAG = "ArgumentService";
    public ArgumentService() {
        super("ArgumentService");
        setIntentRedelivery(true);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            String name=intent.getStringExtra("name");
            boolean redeliver=intent.getBooleanExtra("redeliver",false);

            Log.d(TAG, "redeliver"+redeliver);
            SystemClock.sleep(5000);
            boolean fail = intent.getBooleanExtra("fail", false);
            Log.d(TAG, "fail:" + fail);

        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() called with: " + "intent = [" + intent + "], flags = [" + flags + "], startId = [" + startId + "]");
        return super.onStartCommand(intent, flags, startId);
    }
}
