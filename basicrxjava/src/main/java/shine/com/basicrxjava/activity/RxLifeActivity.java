package shine.com.basicrxjava.activity;

import android.os.Bundle;
import android.util.Log;

import com.trello.rxlifecycle.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import shine.com.basicrxjava.R;

/**
 * 使用RxAppCompatActivity解决订阅的生命周期同步
 */
public class RxLifeActivity extends RxAppCompatActivity {
    private static final String TAG = "RxLifeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_life);
        ButterKnife.bind(this);
        //邦定到pause
        Observable.interval(1, TimeUnit.SECONDS).doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                Log.d(TAG, "called when unsubscribe stop at pause");
            }
        }).
                compose(this.<Long>bindUntilEvent(ActivityEvent.PAUSE))
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted start from oncrete " );
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "onNext() called with:onCreate " + "aLong = [" + aLong + "]");
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //onstart 订阅的将在onstop解除订阅
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnUnsubscribe(new Action0() {
                    @Override
                    public void call() {
                        Log.i(TAG, "from start to stop");

                    }
                })
                .compose(this.<Long>bindToLifecycle())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called with: " + "start from Onstart");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "onNext() called with: onStart" + "aLong = [" + aLong + "]");
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Observable.interval(1, TimeUnit.SECONDS).doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                Log.d(TAG, "from resume to destory");
            }
        }).
                compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called with: " + "start from onresume");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "onNext() called with:onResume " + "aLong = [" + aLong + "]");
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @OnClick(R.id.btn_back)
    public void onClick() {
        finish();
    }
}
