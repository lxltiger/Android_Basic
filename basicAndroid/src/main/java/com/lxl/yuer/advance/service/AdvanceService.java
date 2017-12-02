package com.lxl.yuer.advance.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AdvanceService extends Service {
    private static final String TAG = "AdvanceService";
    public AdvanceService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new AdvancedBinder();
    }

    private void dance() {
        Log.d(TAG, "dance");
    }
    /**
     * 如果服务的有些方法不便暴露，使用接口的方式暴露，此时类为私有
     */
    private  class AdvancedBinder extends Binder implements IService {

        @Override
        public void play() {
            Log.d(TAG, "call play");
            dance();
        }

    }

    public interface IService {
        void play();
    }
}
