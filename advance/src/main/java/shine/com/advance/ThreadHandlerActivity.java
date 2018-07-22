package shine.com.advance;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
* 线程,messageQueue,handler,looper的关系
* 在ActivityThread入口函数中main中
* 1.Looper.prepareMainLooper(),这是由android环境创建,不可手动调用
*  Looper Class used to run a message loop for a thread
*   Looper(boolean quitAllowed)的构造函数中,初始化了MessageQueue,和Thread,false表示线程不允许退出
*   prepare(false),调用上面构造函数,并把Looper对象设给ThreadLocal,这样使Looper和Thread联系起来
*   最后使用ThreadLocal.get 初始化mainLooper;这个looper是为handler准备的
*
* 2.使用自带的Handler初始化一次sMainThreadHandler,
* 调用的是Handler(Callback callback, boolean async) ,参数为null,false
* 然后Handler中的looper指向Looper中的looper,messageQueue指向looper中的messageQueue
* handler的send 或post 方法最终都调用enqueueMessage(queue, msg, uptimeMillis)
* 这个信息入栈最终由messageQueue实现
*
* 3.Looper.loop
*不断从Queue中取message,调用handler的dispatchMessage(msg)
* 派发流程为:
* 如果msg的callBack不为空为runnable,直接msg.callback.run()
*   如果handler.mCallback不为空调用mCallback.handlerMessage
*       如果前两个都为空,调用handler.handleMessage
*

*当表示主线程需要自己初始化Loopper来循环处理消息
* ThreadHandler 在内部解决了这一问题,其是一个线程类,在run方法中初始化了Looper
* 我们使用ThreadHandler 定时从网络请求数据 成功后显示在Ui 上
* 设备旋转的问题*/
public class ThreadHandlerActivity extends AppCompatActivity implements Handler.Callback {
    private static final String TAG = "ThreadHandlerActivity";
    public static final int INTERVAL = 10 * 1000;
    //http 前缀不可少,否则IO异常
    public static final String PATH = "http://www.baidu.com";
    //请求数据
    public static final int REQUEST_CODE = 100;
    //更新数据
    public static final int UPDATE_CODE = 200;
    //发送定时网络请求信息,调用请求方法
    private Handler mHandler;
    private Handler mMainHandler;
    //是否继续请求标志
    private boolean mKeepRequest = false;
    private TextView mTextViewContent;
    private java.text.DateFormat mTimeInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_handler);
        Log.d(TAG, "onCreate: "+MainActivity.test_co_exist);
        mTimeInstance = SimpleDateFormat.getTimeInstance();
        //显示从网络获取的内容
        mTextViewContent = (TextView) findViewById(R.id.tv_content);
        //初始化HandlerThread
        HandlerThread handlerThread = new HandlerThread("workThread");
        //启动线程,即调用run(),在run方法中,调用了Looper的实例化方法,Looper的构造函数实例化了MessageQueue
        handlerThread.start();
        //由于Looper是在线程中实例化,不是同步的,到此处还不定完成,
        // 所以getLooper方法中会wait,直到looper不为空
        //第二个参数是信息处理的回调函数,使用this 监听
        //When you create a new Handler, it is bound to the thread
        // message queue of the thread that is creating it
        mHandler = new Handler(handlerThread.getLooper(), this);
        //用于更新UI内容
        mMainHandler = new Handler(getMainLooper(), this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume start request");
        //设置不断请求
        mKeepRequest = true;
        //发送请求信息,message会被handler发给handlerThread的messageQueue中,最终交由mHandler处理
        mHandler.sendEmptyMessage(REQUEST_CODE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "on stop canle request");
        //当页面停止取消不断请求并移除消息队列中的消息
        mKeepRequest = false;
        mHandler.removeMessages(REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "destroy quit looper");
        //显式停止loop
        mHandler.getLooper().quit();
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case REQUEST_CODE:
                requestData();
                break;
            case UPDATE_CODE:
                // 获取当前信息发送的时间
                long time = (long) msg.obj;
                mTextViewContent.append(String.format("update success %s\n", mTimeInstance.format(new Date(time))));
                break;
        }
        //查看源码可知道,返回true很必要,否则代码还会被handler.handleMessage进一步处理
        return true;
    }


    private void requestData() {
        //还有一个HttpsURLConnection 所谓安全连接,不能搞错
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(PATH);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            //GET不能写错,区分大小写
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            int status = httpURLConnection.getResponseCode();
            if (status == 200) {
                Log.d(TAG, "status:" + status);
            }
            InputStream inputStream = httpURLConnection.getInputStream();
            if (inputStream == null) {
                Log.d(TAG, "get nothing ");
                return;
            }
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Log.d(TAG, "succeed to get content");
            //获取网络信息成功后发给主线程的MessageQueue
            Message.obtain(mMainHandler, UPDATE_CODE, 0, 0, System.currentTimeMillis()).sendToTarget();
        } catch (IOException e) {
            Log.d(TAG, "IO exception");
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        //为真的话延迟继续发送,从而实现定时执行
        if (mKeepRequest) {
            mHandler.sendEmptyMessageDelayed(REQUEST_CODE, INTERVAL);
        }
    }
}
