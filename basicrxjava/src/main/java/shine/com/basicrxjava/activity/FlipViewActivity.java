package shine.com.basicrxjava.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ViewSwitcher;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import shine.com.basicrxjava.R;

public class FlipViewActivity extends AppCompatActivity {


    @Bind(R.id.viewflip)
    ViewSwitcher mViewflip;
    private Subscription mSubscription;

    @OnClick(R.id.btn_finish)
    void finish(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip_view);
        ButterKnife.bind(this);
        mSubscription = Observable.interval(1000, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }
    Subscriber<Long> mObserver=new Subscriber<Long>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Long aVoid) {
            Log.d("FlipViewActivity", "heihei");
            mViewflip.showNext();
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null&&!mObserver.isUnsubscribed()) {
            mObserver.unsubscribe();
        }
    }
}
