package shine.com.advance;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import java.util.Map;
import java.util.Set;

import shine.com.advance.messenger.MessengerActivity;
import shine.com.advance.service.LocalActivity;
import shine.com.advance.service.StartServiceActivity;

/*
 * 主线程由zygoteInit启动,经过一系列调用才执行到onCreate()
 * zygote为Activity创建的管理主线程的类为ActivityThread
 * 包含Activity 的客户端至少有三个线程
 * Activity启动会创建ViewRoot.W,同时ActivityThread会创建ApplicationThread对象
 * 这两个对象都继承于Binder,所有需要启动两个线程,负责接受Linux Binder驱动发送的IPC调用
 *最后一个是应用所在的线程,即UI线程,用于处理用户信息及界面绘制操作
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private WindowManager.LayoutParams layoutParams;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_simple_service).setOnClickListener(this);
        findViewById(R.id.btn_bound_service).setOnClickListener(this);
        findViewById(R.id.btn_messenger_service).setOnClickListener(this);

        addMyView();

    }

    @SuppressLint("ClickableViewAccessibility")
    public void addMyView() {
        button = new Button(this);
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        layoutParams.x = (int) event.getRawX() - button.getWidth() / 2;
                        layoutParams.y = (int) event.getRawY() - button.getHeight() / 2;
                        getWindowManager().updateViewLayout(button, layoutParams);
                        break;
                }

                return false;
            }
        });
        button.setText("one");
        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION,
                0, PixelFormat.TRANSPARENT);
// TODO: 19/1/4 理解flag含义  给View设置这样的布局参数会影响整个WIndow的行为吗？
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.x = 200;
        layoutParams.y = 300;
        getWindowManager().addView(button, layoutParams);

    }



    /*
     * 从桌面启动应用流程
     * 桌面Launcher也是一个应用程序,当点击图表就会调用startActivity
     * 此时intent会添加flag为new task 表示在新的栈中打开
     * 在activity中startActivity都会调用startActivityForResult(intent,int requestCode)
     * 如果requestCode=-1表示不需返回值
     *这个方法调用Instrumentation.execStartActivity
     */

    /*
    *Instrumentation 仪表,在attach()中,在Activity的OnCreate()之前完成初始化,用来监视应用程序和系统的交互

    *  execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
            Intent intent, int requestCode, Bundle options) 返回ActivityResult
    *  contextThread我们传人的是ActivityThread类型,代表应用程序主线程,也是一个Binder对象,
    *  ActivityManagerService(AMS)会使用它和主线程通讯,这里的主线程代表Launcher应用的进程
    *这里的token也是Activity类的成员变量，它是一个Binder对象的远程接口
    * 方法中如下代码
    *int result = ActivityManagerNative.getDefault().
    *       startActivity(whoThread, who.getBasePackageName(), intent,
    *       intent.resolveTypeIfNeeded(who.getContentResolver()),
    *      token, target != null ? target.mEmbeddedID : null,
    *     requestCode, 0, null, options);
    *
    * intent.resolveTypeIfNeeded返回这个intent的MIME类型
    *ActivityManagerNative.getDefault返回ActivityManagerService的远程接口，即ActivityManagerProxy接口
    * 也就是调用ActivityManagerProxy.startActivity
    *这步通过Binder驱动程序就进入到ActivityManagerService的startActivity(...)
    *
    *其又调用startActivityAsUser(...)主要做检查
    *   1.enforceNotIsolatedCaller("startActivity");检查调用者是否属于被隔离对象
     *   2.handleIncomingUser(…)检查调用者是否有权力这么做
    *      3.调用ActivityStackSupervisor.startActivityMayWait函数，
    *
    * ActivityStack,其是专门管理Activity的堆栈,先进后出,调用ActivityStackSupervisor是管理所有ActivityStack的
    *
    * 在startActivityMayWait方法中
    * 1.ActivityInfo aInfo =resolveActivity(intent, resolvedType, startFlags, profilerInfo, userId);
    *   对参数intent的内容进行解析，得到目标Activity的相关信息，保存在aInfo变量中：
    * 2.判断目标Activity是否属于重量级进程,如果当前系统已存在的这个不是即将要启动的这个,就要重新给intent 赋值
    * 3.调用startActivityLocked(...),
    *       1)其确保调用者本事的进程是存在的
    *       2)检查调用者是否有权限启动指定Activity
    * 4.调用startActivityUncheckedLocked(...),主要是启动模式和Intent标志的处理:
    *       应用程序通过两种方式改变ActivityTask的行为
    *       1)在清单activity标签指定属性,相关属性如下:
    *         android:taskAffinity,代表页面所希望归属的task,一般应用所有页面都又共同Affinity,即包名;
    *         一个页面task的Affinity属性取决于根页面,taskAffinity在以下情况下产生效果:Intent带new Task标志,一般
    *         要开启新task,如果新activity的affinity和当前task一样就不会开启新的;activity中allowTaskReparenting设为
    *         true,activity在适当情况下会被挪到和他更亲近的task 中
    *         android:launcherMode:activity启动模式
    *         还有其他不常用属性不扯了
    *        2)使用Intent标志位,这个更加动态 灵活,主要有
    *        new task 对应标签的single Task
    *        single top 对应标签的single Top
    *        clear top 没有相对应的标签,其表示如果要启动的activity已经存在,那么销毁其上面activity,intent通过
    *        onNewIntent传给他
    *        还有一些渣渣不扯了
    *
    *
    * 其判断处理后调用ActivityStack的startActivityLock(..)这里比较乱还没理清
    *
    */

    /*
     *
     *   ActivityInfo aInfo;
     *   try {
     *   ResolveInfo rInfo =  AppGlobals.getPackageManager().resolveIntent(intent, resolvedType,PackageManager.MATCH_DEFAULT_ONLY   ActivityManagerService.STOCK_PM_FLAGS);
     *   aInfo = rInfo != null ? rInfo.activityInfo : null;
     *   }catch (RemoteException e) {
     *   ......
     *   }
     *
     * 接下去就调用startActivityLocked 方法进一步处理,
     *
     *
     *
     *
     *
     *
     *

     *
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_simple_service:
                startActivity(new Intent(this, StartServiceActivity.class));
                break;
            case R.id.btn_bound_service:
                startActivity(new Intent(this, LocalActivity.class));
                break;
            case R.id.btn_messenger_service:
                startActivity(new Intent(this, MessengerActivity.class));
                /*Intent view = new Intent("android.intent.action.test");
                view.setClassName("wuhu.anhui.myapplication","wuhu.anhui.myapplication.TestActivity");
                startActivity(view);*/
                break;


        }
    }


    private void printStack() {

        Map<Thread, StackTraceElement[]> allStackTraces = Thread.getAllStackTraces();
        Set<Map.Entry<Thread, StackTraceElement[]>> entries = allStackTraces.entrySet();
        for (Map.Entry<Thread, StackTraceElement[]> entry : entries) {
            Log.d(TAG, entry.getKey().toString());
            StackTraceElement[] stackTrace = entry.getValue();
            for (StackTraceElement stackTraceElement : stackTrace) {
                Log.d(TAG, stackTraceElement.toString());
            }
            Log.d(TAG, "-------------: \n");
        }
//        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "onConfigurationChanged() called with: newConfig = [" + newConfig + "]");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent() called with: intent = [" + intent + "]");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: ");
    }
}

