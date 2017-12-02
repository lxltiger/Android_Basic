package shine.com.basicrxjava.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.exceptions.OnErrorThrowable;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import shine.com.basicrxjava.R;

public class ThreeActivity extends AppCompatActivity {

    private static final String TAG = "ThreeActivity";
    @Bind(R.id.imageview)
    ImageView mImageview;
    private Looper mLooper;
    @Bind(R.id.editText)
    EditText mEditText;

    @Bind(R.id.button)
    Button mButton;
    @Bind(R.id.button2)
    Button mButton2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three);
        ButterKnife.bind(this);
        HandlerThread mHandlerThread = new HandlerThread("work");
        mHandlerThread.start();
        mLooper = mHandlerThread.getLooper();

        Subscription subscribe1 = RxView.clicks(mButton).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Toast.makeText(ThreeActivity.this, "you click here ", Toast.LENGTH_SHORT).show();
                        clickButton();
                    }
                });
        Subscription subscribe = RxTextView.textChanges(mEditText).subscribe(new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
                Log.d(TAG, "charSequence:" + charSequence);
            }
        });
        //多个订阅响应一个点击
        CompositeSubscription composit = new CompositeSubscription();
        Observable<Void> clickObservale = RxView.clicks(mButton2).share();
        Subscription subscribe2 = clickObservale.subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Log.d(TAG, "button2 click one");
            }
        });
        composit.add(subscribe2);
        Subscription subscribe3 = clickObservale.subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Log.d(TAG, "button2 click tow");
            }
        });
        composit.add(subscribe3);

    }

    @OnClick(R.id.button3)
    void getImage(View view) {
        getBitmapObservable("http://scimg.jb51.net/allimg/160618/77-16061Q44U6444.jpg")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called with: " + "");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        mImageview.setImageBitmap(bitmap);
                    }
                });
    }

    private void clickButton() {
        getObserveable().subscribeOn(AndroidSchedulers.from(mLooper))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError()", e);
                    }

                    @Override
                    public void onNext(String string) {
                        Log.d(TAG, "onNext(" + string + ")");

                    }
                });
    }

    Observable<String> getObserveable() {
        return Observable.defer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    throw OnErrorThrowable.from(e);
                }
                return Observable.just("ondfsfe", "sfsd", "thrsfdfdfee");
            }
        });
    }

    Observable<Bitmap> getBitmapObservable(final String path) {
        return Observable.defer(new Func0<Observable<Bitmap>>() {
            @Override
            public Observable<Bitmap> call() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    if (inputStream != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        return Observable.just(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw OnErrorThrowable.from(e);
                }
                return Observable.empty();
            }
        });
    }

}
