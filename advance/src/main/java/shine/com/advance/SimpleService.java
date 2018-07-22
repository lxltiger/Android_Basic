package shine.com.advance;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;
/*
*service也是寄存ActivityThread中,启动流程和Activity基本一致也同一需要两个Binder线程支持
* 当执行与界面无关的耗时后天操作,Service是不错的选择,但需开启子线程
* 如果需要的是单线程有序执行任务,配合ThreadHandler 使用
* 如果需处理并发多任务,使用ThreadExcutor*/

public class SimpleService extends Service {
    private static final String TAG = "SimpleService";
    //处理子线程任务
    private Handler mHandler;



    /*处理service收到的任务*/
    private final class ServiceHandler extends Handler{
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            int startId=msg.arg1;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                //恢复中断状态?
                Thread.currentThread().interrupt();
            }
            //当执行完毕为了节约资源需关闭service
            //每次onStartCommand接受到请求都会产生新startId,
            // 带参数的stopSelf会比较参数startId和最新的startId,如果相同说明没有新任务,会停止服务,否则继续运行
            //所以发送startId的顺序很重要,
            Log.d(TAG, "handle msg"+startId);
            stopSelf(startId);

        }
    }

    //  在生命周期只调用一次
    @Override
    public void onCreate() {
        Log.d(TAG, "service create");
        //为Handler创建线程,Looper,messageQueue
        HandlerThread handlerThread = new HandlerThread("background", Process.THREAD_PRIORITY_BACKGROUND);
        handlerThread.start();
        mHandler = new ServiceHandler(handlerThread.getLooper());
    }
  /*
  * 关于返回值
  * START_NOT_STICKY 表明这个方法调用后如果在资源紧张情况下service被killed 不会重新启动,除非有pendingIntent
  * 通常用来定时获取网络资料,如果被killed,下次会被alarm重启
  *
  * START_REDELIVER_INTENT 表示service 被killed后会如果还有未处理的Intent或pending Intent,会重启继续Intent
  * 不会在onStartCommand收到一个null Intent,适合由于文件下载
  *
  * START_STICKY 在service被killed后 会保留start状态,重启后如果没有pendingIntent 不会处理遗留的Intent
  * 而是在onStartCommand收到一个null Intent,这个模式适合显示的启动和关闭 如音乐播放器*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: "+startId);
        Message message=mHandler.obtainMessage();
        // 使用startId作为参数,这样任务完成就直到停止相应的服务
        message.arg1=startId;
        mHandler.handleMessage(message);
        //Start——sticky 表示服务需显示启动,显示关闭,如果被系统杀死会自启
        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        //当版本达到18可使用quitSafely,在终止looper前把message处理完,但delayed除外
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mHandler.getLooper().quitSafely();
        }else{
            //终止之前直接清除所以message
            mHandler.getLooper().quit();
        }
        Log.d("SimpleService", "service destroty");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
