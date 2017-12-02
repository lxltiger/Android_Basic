package shine.com.basicrxjava.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import shine.com.basicrxjava.NetWork;
import shine.com.basicrxjava.R;
import shine.com.basicrxjava.entity.Movie;

public class OneActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "OneActivity";
    private ImageView imageview;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        findViewById(R.id.btn1).setOnClickListener(this);
        imageview = (ImageView) findViewById(R.id.imageview);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
                getMovie();
                break;
        }
    }


    private void getMovie() {
        mSubscription = NetWork.getMovieService().getTopMovie(0, 10).subscribeOn(Schedulers.io())
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

    Subscriber<Movie> mSubscriber=new Subscriber<Movie>() {
        @Override
        public void onCompleted() {
            Log.d(TAG, "onCompleted() called with: " + "");

        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, e.getMessage());

        }

        @Override
        public void onNext(Movie movie) {
            Log.d(TAG, movie.toString());
        }
    };

}
