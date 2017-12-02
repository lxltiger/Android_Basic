package shine.com.basicrxjava.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import shine.com.basicrxjava.R;
import shine.com.basicrxjava.entity.EntityOne;

/**
 * 在不指定线程的情况下， RxJava 遵循的是线程不变的原则，即：在哪个线程调用 subscribe()，
 * 就在哪个线程生产事件；在哪个线程生产事件，就在哪个线程消费事件。如果需要切换线程，就需要用到 Scheduler （
 * Schedulers.immediate() : 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
 * <p/>
 * Schedulers.newThread() :总是启用新线程，并在新线程执行操作.
 * <p/>
 * Schedulers.io():I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，因此多数情况下 io() 比 newThread() 更有效率。不要把计算工作放在 io() 中，可以避免创建不必要的线程。
 * <p/>
 * Schedulers.computation() : 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
 * <p/>
 * 还有RxAndroid里面专门提供了AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
 * subscribeOn(): 指定 subscribe() 所发生的线程，即 Observable.OnSubscribe 被激活时所处的线程。
 * 或者叫做事件产生的线程。 * observeOn(): 指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
 * observeOn() 指定的是它之后的操作所在的线程。因此如果有多次切换线程的需求，只要在每个想要切换线程的位置调用一次 observeOn() 即可。上代码：

 Observable.just(1, 2, 3, 4) // IO 线程，由 subscribeOn() 指定
 .subscribeOn(Schedulers.io())
 .observeOn(Schedulers.newThread())
 .map(mapOperator) // 新线程，由 observeOn() 指定
 .observeOn(Schedulers.io())
 .map(mapOperator2) // IO 线程，由 observeOn() 指定
 .observeOn(AndroidSchedulers.mainThread)
 .subscribe(subscriber);  // Android 主线程，由 observeOn() 指定
 如上，通过 observeOn() 的多次调用，程序实现了线程的多次切换。

 不过，不同于 observeOn() ， subscribeOn() 的位置放在哪里都可以，但它是只能调用一次的。
 */
public class SimpleRxActivity extends AppCompatActivity {
    private static final String TAG = "SimpleRxActivity";
    @Bind(R.id.text)
    TextView mText;
    @Bind(R.id.btn_buffer_test)
    Button mBtnBufferTest;
    private Subscription mSubscription;
    private Subscription mSubscription2;
    private Subscription mSubscription3;

    @OnClick(R.id.btn_back)
    public void finish(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_rx);
        ButterKnife.bind(this);
        simpleCreate();
        simpleDefer();
        simpleFrom();
        simpleInterval();
        simpleTime();
        simpleJust();
        simpleRange();
        simpleRepeat();
        simpleRepeatWhen();
        simpleBuffer();
        simpleBuffer2();

    }

    /**
     * create操作符是所有创建型操作符的“根”，也就是说其他创建型操作符最后都是通过create操作符来创建
     * Observable 即被观察者，它决定什么时候触发事件以及触发怎样的事件
     * 这里传入了一个 OnSubscribe 对象作为参数。OnSubscribe 会被存储在返回的 Observable 对象中，它的作用相当于一个计划表，
     * 当 Observable 被订阅的时候，OnSubscribe 的 call() 方法会自动被调用，事件序列就会依照设定依次触发
     */
    private void simpleCreate() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                //最好要在回调的call函数中增加isUnsubscribed的判断，以便在subscriber在取消订阅时不会再执行call函数中相关代码逻辑
                try {
                    if (!subscriber.isUnsubscribed()) {
                        for (int i = 0; i < 5; i++) {
                            subscriber.onNext("tom" + i);
                        }
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted() called with: " + "simpleCreate");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
            }

            @Override
            public void onNext(String o) {
                Log.d(TAG, "onNext() called with: " + Thread.currentThread().getName() + "o = [" + o + "]");
            }
        });
    }

    /**
     * defer 只要在被订阅的时候才会执行，
     * do not create the Observable until the observer subscribes, and create a fresh Observable for each observer
     */
    private void simpleDefer() {
        EntityOne entityOne = new EntityOne();
        Observable<String> observable = entityOne.valueObservable();
        entityOne.setName("lxl");
        observable.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d(TAG, "call() called with: " + "s = [" + s + "]");
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d(TAG, "call() called with: " + "throwable = [" + throwable + "]");
            }
        });
    }

    public Observable<EntityOne> createEntityOne(final String name) {
        return Observable.defer(new Func0<Observable<EntityOne>>() {
            @Override
            public Observable<EntityOne> call() {
                EntityOne entityOne = new EntityOne();
                entityOne.setName(name);
                try {
                    // TODO: 2016/8/9 some io operation  db.writeToDisk(entityone)
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return Observable.error(e);
                }
                return Observable.just(entityOne);
            }
        });
    }

    /**
     * convert some other object or data structure into an Observable
     */
    public void simpleFrom() {
        List<String> list = new ArrayList<>();
        list.add("one");
        list.add("two");
        list.add("three");
        Observable.from(list).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

                Log.d(TAG, "onCompleted() called with: " + "basicFrom");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext() called with: " + "s = [" + s + "]");
            }
        });
    }

    /**
     * create an Observable that emits a sequence of integers spaced by a particular time interval
     * operates by default on the computation Scheduler.
     */

    public void simpleInterval() {
        mSubscription = Observable.interval(10, TimeUnit.SECONDS).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called with: " + "simpleInterval");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        mText.setText(String.format("%s--%s", Thread.currentThread(), aLong.toString()));
                    }
                });
    }

    /**
     * timer 返回一个 Observable , 它在延迟一段给定的时间后发射一个简单的数字0
     * timer 操作符默认在computation调度器上执行，当然也可以用 Scheduler 在定义执行的线程。
     */
    private void simpleTime() {
        Observable.timer(2, TimeUnit.SECONDS).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted() called with: " + "simpleTime");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
            }

            @Override
            public void onNext(Long aLong) {
                Log.d(TAG, "onNext() called with: " + "aLong = [" + aLong + "]");
            }
        });
    }

    /**
     * convert an object or a set of objects into an Observable that emits that or those objects
     */
    private void simpleJust() {
        Observable.just("zhang", "wang", "li").subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted() called with: " + "simple just");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext() called with: " + "s = [" + s + "]");
            }
        });
    }

    /**
     * range 接受的参数为起始值start和长度length
     * length<0  exception
     * length=0  Observable.empty
     * length=1 Obserable.just(start)
     * 因为是整型 start+length-1<Integer.Max
     */
    private void simpleRange() {
        Observable.range(10, 4).subscribe(new Observer<Integer>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted() called with: " + "simple range");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext() called with: " + "integer = [" + integer + "]");
            }
        });
    }

    /**
     * Repeat  It does not initiate an Observable,
     * but operates on an Observable in such a way that it repeats the sequence emitted by the source Observable as its own sequence
     * resubscribes when it receives onCompleted()
     */
    private void simpleRepeat() {
        Observable.just("hello").repeat(3).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted() called with: " + "simpleRepeat");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext() called with: " + "s = [" + s + "]");
            }
        });

    }

    /**
     * repeat when
     * resubscribes when it receives onCompleted().
     */
    private void simpleRepeatWhen() {
        //delay  延时一次，延时完成后，可以连续发射多个数据
        mSubscription2 = Observable.just("repeat when").repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Void> observable) {
                //delay  延时一次，延时完成后，可以连续发射多个数据
                return observable.delay(5, TimeUnit.SECONDS);
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted() called with: " + "repeat when");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext() called with: " + "s = [" + s + "]");
            }
        });
    }

    /**
     * periodically gather items from an Observable into bundles and
     * emit these bundles rather than emitting the items one at a time
     */
    private void simpleBuffer() {
        final String[] messages = new String[]{"from mon", "frm dad", "from brother"};
        final Random random = new Random();
        mSubscription3 = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    while (!subscriber.isUnsubscribed()) {
                        String message = messages[random.nextInt(messages.length)];
                        subscriber.onNext(message);
                        TimeUnit.MILLISECONDS.sleep(1000);
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()).buffer(3, TimeUnit.SECONDS).subscribe(new Subscriber<List<String>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted() called with: " + "simpleBuffer");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: buffer" + "e = [" + e + "]");
            }

            @Override
            public void onNext(List<String> list) {
                Log.d(TAG, "get message" + list.toString());
            }
        });
    }

    //点击按钮2次才响应事件
    private void simpleBuffer2() {
        RxView.clicks(mBtnBufferTest).buffer(2).subscribe(new Observer<List<Void>>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted() called with: " + "");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
            }

            @Override
            public void onNext(List<Void> voids) {
                Toast.makeText(SimpleRxActivity.this, "you have to click twice to get a toast", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        if (mSubscription2 != null) {
            mSubscription2.unsubscribe();
        }
        if (mSubscription3 != null) {
            mSubscription3.unsubscribe();
        }
    }
}
