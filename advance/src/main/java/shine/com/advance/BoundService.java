package shine.com.advance;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

/*for practise*/
public class BoundService extends Service {
    private static final String TAG = "BoundService";
    private Random mGenerate = new Random();

    public  class LocalBinder extends Binder{
        public BoundService getService() {
            return BoundService.this;
        }
    }

    public int generateInt() {
        return mGenerate.nextInt(100);
    }


    @Override
    public IBinder onBind(Intent intent) {

        return new LocalBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}
