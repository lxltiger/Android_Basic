package shine.com.basicrxjava.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import shine.com.basicrxjava.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Bind(R.id.tv)
    TextView mTv;
    @Bind(R.id.btn1)
    Button mBtn1;
    @Bind(R.id.btn2)
    Button mBtn2;
    @Bind(R.id.btn3)
    Button mBtn3;
    @Bind(R.id.image)
    ImageView mImage;
    @Bind(R.id.image2)
    ImageView mImage2;

    @OnClick(R.id.image2)
    public void OnClick() {
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        RxJava 还提供了一些方法用来快捷创建事件队列
        Observable<String> observable = Observable.just("one", "two", "three");
        String[] word = new String[]{"one", "two", "three"};
        //from(T[]) / from(Iterable<? extends T>) : 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。
        Observable<String> observable2 = Observable.from(word);
        //subscriber() 做了3件事,
        // 调用 Subscriber.onStart() 。是一个可选的准备方法,可以用于做一些准备工作。
        //调用 Observable 中的 OnSubscribe.call(Subscriber) 。在这里，事件发送的逻辑开始运行。
        // 在 RxJava 中， Observable 并不是在创建的时候就立即开始发送事件，而是在它被订阅的时候,即当 subscribe() 方法执行的时候。
        // 将传入的 Subscriber 作为 Subscription 返回。这是为了方便 unsubscribe().
        //subscribe() 还支持不完整定义的回调，RxJava 会自动根据定义创建出 Subscriber
        Action1<String> onNext = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, s);
            }
        };
        Action1<Throwable> OnError = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d(TAG, "throwable:" + throwable);
            }
        };
        Action0 OnComplete = new Action0() {
            @Override
            public void call() {
                Log.d(TAG, "completed");
            }
        };
        observable.subscribe(onNext);
        observable2.subscribe(onNext, OnError);
        observable2.subscribe(onNext, OnError, OnComplete);

        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Drawable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "something wrong");
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        mImage.setImageDrawable(drawable);
                    }
                });

        Observable.just(R.mipmap.ic_launcher)
                .map(new Func1<Integer, Bitmap>() {
                    @Override
                    public Bitmap call(Integer integer) {
                        return BitmapFactory.decodeResource(getResources(), integer);
                    }
                }).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                mImage2.setImageBitmap(bitmap);
            }
        });

        RxView.clicks(mBtn1)
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(MainActivity.this, ThreeActivity.class);
                        MainActivity.this.startActivity(intent);
                        Toast.makeText(MainActivity.this, "you click ", Toast.LENGTH_SHORT).show();
                    }
                });
        RxView.clicks(mBtn3).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent(MainActivity.this, RxLifeActivity.class);
                        MainActivity.this.startActivity(intent);
                    }
                });

        RxView.clicks(mBtn2).buffer(2).subscribe(new Observer<List<Void>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted() called with: " + "");
            }

            @Override
            public void onError( Throwable e) {
                Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
            }

            @Override
            public void onNext(List<Void> voids) {
                Intent intent = new Intent(MainActivity.this, BasicTransformActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });
    }


}
