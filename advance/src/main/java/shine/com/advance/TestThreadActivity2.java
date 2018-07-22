package shine.com.advance;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



/*开启子线程定时执行操作
* 使用Handler Looper MessageQueue message
*
* */
@Deprecated
public class TestThreadActivity2 extends AppCompatActivity implements Handler.Callback {
    private static final String TAG = "TestThreadActivity2";
    //message.what
    public static final int PING_BAIDU=100;
    //不能少了http前缀
    private static final String PING_URL = "http://www.baidu.com";
    //每分钟连接一次
    public static final int VISIT_INTERVAL=60*1000;
    //是否连接标志
    private boolean mPingBaiDu=false;

    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        *经测试,主线程只有一个
        *Binder相关未知?
        * 在清单中注册的四大组件,如无特别声明都是运行在同一进程中,并且都有主线程处理事件
        */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
        //打印值为2,证明确实在同一进程,
        // 即使MainActivity已finish() ??
        //可以在application节点或组件节点使用android:process指明想要依存的进程环境
        Log.d(TAG, "test_co_exist:" + MainActivity.test_co_exist);
        HandlerThread handlerThread = new HandlerThread("background");
        //run方法中创建了looper并开启循环
        handlerThread.start();
        //When you create a new Handler, it is bound to the thread
        // message queue of the thread that is creating it
        mHandler = new Handler(handlerThread.getLooper(),this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置标志为真 定时执行
        mPingBaiDu=true;
        //开始发送ping消息指令
        mHandler.sendEmptyMessage(PING_BAIDU);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在页面停止时执行和OnResume相反操作,
        mPingBaiDu=false;
        mHandler.removeMessages(PING_BAIDU);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //退出时显式终止线程
        mHandler.getLooper().quit();

    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case PING_BAIDU:
                pingBaidu();
                break;
        }
        //查看源码可知道,返回true很必要,否则代码还会被handler.handleMessage进一步处理
        return true;
    }

    /*定时访问百度网站*/
    private void pingBaidu() {
        Log.d(TAG, "pingBaidu: start here");
         HttpURLConnection mURLConnection=null;
        try {
            URL url = new URL(PING_URL);
            mURLConnection = (HttpURLConnection) url.openConnection();
            mURLConnection.setRequestMethod("GET");
            mURLConnection.connect();
            Log.d(TAG, "mURLConnection.getResponseCode():" + mURLConnection.getResponseCode());
            InputStream inputStream = mURLConnection.getInputStream();
            if (inputStream == null) {
                Log.d(TAG, "pingBaidu: fail to connect");
                return;
            }

            Log.d(TAG, "succeed to connect");

        } catch (IOException e) {
            Log.d(TAG, "pingBaidu: io Exception");
            e.printStackTrace();
        }finally {
            if (mURLConnection != null) {
                mURLConnection.disconnect();
            }
        }
        if (mPingBaiDu) {
            mHandler.sendEmptyMessageDelayed(PING_BAIDU, VISIT_INTERVAL);
        }
    }
}
