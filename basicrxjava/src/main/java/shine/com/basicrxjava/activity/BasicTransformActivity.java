package shine.com.basicrxjava.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import shine.com.basicrxjava.R;
import shine.com.basicrxjava.entity.Student;

public class BasicTransformActivity extends AppCompatActivity {
    private static final String TAG = "BasicTransformActivity";
    Student[] students = new Student[3];

    @OnClick(R.id.btn_back)
    public void onClick() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_transform);
        ButterKnife.bind(this);
        students[0] = new Student("zhang1", 122);
        students[1] = new Student("zhang2", 12);
        students[2] = new Student("zhang3", 132);
        simpleMap();
        simpleFlatMap();
        simpleFlatMap2();
        simpleFlatMap3();
        simpleFlatMap4();
    }

    /**
     * transform the items emitted by an Observable by applying a function to each item
     */
    private void simpleMap() {
        Observable
                .from(students)
                .subscribeOn(Schedulers.io())
                .map(new Func1<Student, String>() {
                    @Override
                    public String call(Student student) {
                        return student.getName();
                    }
                })
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted() called with: " + "simpleMap");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "onNext() called with: simple map" + "s = [" + s + "]");
                    }
                });
    }

    /**
     * transform the items emitted by an Observable into Observables,
     * then flatten the emissions from those into a single Observable
     */
    private void simpleFlatMap() {
        Observable
                .from(students)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<Student, Observable<Student.Course>>() {
                    @Override
                    public Observable<Student.Course> call(Student student) {
                        Log.d(TAG, student.getName());
                        return Observable.from(student.getCourses());
                    }
                }).subscribe(new Subscriber<Student.Course>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted() called with: " + "simpleFlatMap");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError() called with: " + "e = [" + e + "]");
            }

            @Override
            public void onNext(Student.Course course) {
                Log.d(TAG, course.getName());
            }
        });
    }

    /**
     * print number in order
     */
    private void simpleFlatMap2() {
        Observable.range(2, 10).flatMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                Log.d(TAG, "integer one:" + integer);
                return Observable.range(integer, 2);
            }
        }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "integer two:" + integer);
            }
        });
    }

    private void simpleFlatMap3() {
        Observable.range(2, 10).flatMap(new Func1<Integer, Observable<Integer>>() {
            @Override
            public Observable<Integer> call(Integer integer) {
                return Observable.just(integer).delay(11 - integer, TimeUnit.SECONDS);
            }
        }).toBlocking().subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "integer:3" + integer);
            }
        });
    }

    private void simpleFlatMap4() {
        Observable.range(2,10)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<Integer, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Integer integer) {
                        if (integer > 6) {
                            //抛出异常后不再执行
                            return Observable.error(new IOException("hei"));
                        }
                        return Observable.just(integer*integer);
                    }
                }).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                Log.d(TAG, "call() called with: simpleFlatMap4" + "integer = [" + integer + "]");
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.d(TAG, throwable.getMessage());
            }
        });
    }
}
