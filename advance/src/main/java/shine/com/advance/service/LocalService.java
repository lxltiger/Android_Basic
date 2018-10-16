package shine.com.advance.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

/**
 * 如果服务是供您的自有应用专用，并且在与客户端相同的进程中运行（常见情况），
 * 则应通过扩展 Binder 类并从 onBind() 返回它的一个实例来创建接口。
 * 客户端收到 Binder 后，可利用它直接访问 Binder 实现中乃至 Service 中可用的公共方法。
 * 如果服务只是您的自有应用的后台工作线程，则优先采用这种方法。
 * 不以这种方式创建接口的唯一原因是，您的服务被其他应用或不同的进程占用。
 */
public class LocalService extends Service {
    private static final String TAG = "LocalService";
    private Random mGenerate = new Random();

    public  class LocalBinder extends Binder{
        public LocalService getService() {
            return LocalService.this;
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
