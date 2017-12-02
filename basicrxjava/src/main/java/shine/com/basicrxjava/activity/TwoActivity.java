package shine.com.basicrxjava.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import shine.com.basicrxjava.BeautyToPretty;
import shine.com.basicrxjava.NetWork;
import shine.com.basicrxjava.R;
import shine.com.basicrxjava.entity.Pretty;

public class TwoActivity extends AppCompatActivity {

    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        mSubscription = NetWork.getBeautyService().getBeauty(10, 1)
                .map(new BeautyToPretty())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null&&!mSubscriber.isUnsubscribed()) {
            mSubscriber.unsubscribe();
        }
    }
    Subscriber<List<Pretty>> mSubscriber=new Subscriber<List<Pretty>>() {
        @Override
        public void onCompleted() {}

        @Override
        public void onError(Throwable e) {
            Log.d("TwoActivity", e.getMessage());
        }

        @Override
        public void onNext(List<Pretty> prettyList) {
            for (Pretty pretty : prettyList) {
                Log.d("TwoActivity", pretty.toString());
            }
        }
    };
}
