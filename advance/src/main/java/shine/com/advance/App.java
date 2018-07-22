package shine.com.advance;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Process;
import android.util.Log;

/**
 * Created by lixiaolin on 18/4/4.
 */

public class App extends Application {
    private static final String TAG = "Application";
    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationInfo applicationInfo = getApplicationInfo();
        Log.d(TAG, "getApplicationInfo()" + applicationInfo.processName);
        Log.d(TAG, ".uid:" + Process.myPid());
    }
}
