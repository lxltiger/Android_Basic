package shine.com.advance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.TextView;


/*
android进程和线程
* 进程process是程序的一个运行实例
* 线程是cpu调度的基本单位, 属于抽象范畴
* 线程类的本质仍是可执行代码,在java中对应Thread类
* Thread类只是一个中介,任务就是启动一个线程运行指定的Runnable
* 在其start()方法中会调用 nativeCreate(this, stackSize, daemon)
* 其调用底层代码,才是cpu创建线程的地方,他会间接调用run()
*
* CPU在一个时刻只能运行一个线程，当在运行一个线程的过程中转去运行另外一个线程，
* 这个叫做线程上下文切换（对于进程也是类似）。
*　由于可能当前线程的任务并没有执行完毕，所以在切换时需要保存线程的运行状态，
* 以便下次重新切换回来时能够继续切换之前的状态运行
* 线程上下文切换过程中会记录程序计数器、CPU寄存器状态等数据。
* 实际上就是 存储和恢复CPU状态的过程，它使得线程执行能够从中断点恢复执行
* 普通线程没有消息队列及Looper,不能使用Handler向其发送消息,但UI线程,HandlerThread可以
* 有关线程导致内存泄漏的案例
*/
public class SimpleThreadActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private static final String TAG = "SimpleThreadActivity";
    private RadioGroup rgThread;
    private TextView tvDisplay;
    public static final String THREAD_KEY = "exampleThread";
    private int exampleThreadId= R.id.rb_two;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_thread);

        rgThread = (RadioGroup) findViewById(R.id.rg_thread);
        rgThread.setOnCheckedChangeListener(this);
        tvDisplay = (TextView) findViewById(R.id.tv_display);
        if (savedInstanceState != null) {
            exampleThreadId = savedInstanceState.getInt(THREAD_KEY);
        }
        runExample();
    }

    private void printStack() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            Log.d(TAG, stackTraceElement.toString());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(THREAD_KEY,exampleThreadId);
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }



    private void runExample() {
        switch (exampleThreadId) {
            case R.id.rb_one:
                exampleOne();
                break;
            case R.id.rb_two:
                exampleTwo();
                break;
            case R.id.rb_three:
                exampleThree();
                break;
        }
        printThread();
    }
/*显示当前所有测试线程信息,每当屏幕旋转都会增加*/
    private void printThread() {
        tvDisplay.setText("");
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.toString().startsWith("background thread") && !thread.isInterrupted()) {
                tvDisplay.append(thread.toString() + "\n");
            }
        }
    }


    /* 中断当前开启的所有测试线程,准备下一个测试*/
    private void resetThread() {
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.toString().startsWith("background thread")) {
                thread.interrupt();
            }
        }
    }

    /*线程属于内部类,对外部类持有隐式引用,
    在设备旋转过程中,外部Activity被销毁,但仍有引用指向它导致无法回收*/
    private void exampleOne() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                    Log.d(TAG, "exampleOne run: ");
                }
            }

            @Override
            public String toString() {
                return "background thread" + getId() + "running";

            }
        }.start();

    }

    private void exampleTwo() {
        new MyThread().start();
    }

    private MyThread mMyThread;
    /*与exampleTwo类似,不同的是有个成员变量mMyThread 可以在onDestroy 时改变参数取消线程的运行*/
    private void exampleThree() {
        mMyThread = new MyThread();
        mMyThread.start();
    }
    /*
    *
    * 静态内部类不持有外部引用,所以设备旋转不用导致内存泄漏
    *但线程依然在运行,DVK持用运行的线程引用,所以不管activity有没有销毁回收都不影响线程运行,直到应用内核进程被杀死
    * */
    private static class MyThread extends Thread{
        private boolean isRunning=true;
        @Override
        public void run() {
            while (isRunning) {
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    isRunning=false;
                    e.printStackTrace();
                }
                Log.d(TAG, "my thread is running");
            }
        }

        @Override
        public String toString() {
            String status=isRunning?"running":"not running";
            return "background thread"+getId()+status;
        }

        public void cancel() {
            isRunning=false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exampleThreadId==R.id.rb_three) {
            mMyThread.cancel();
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId != exampleThreadId) {
            resetThread();
            exampleThreadId=checkedId;
            runExample();
        }

    }


}
