package shine.com.basicrxjava.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import shine.com.basicrxjava.api.NetWork;
import shine.com.basicrxjava.R;
import shine.com.basicrxjava.entity.Login;
import shine.com.basicrxjava.entity.Movie;
import shine.com.basicrxjava.entity.User;

public class OneActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "OneActivity";
    private ImageView imageview;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        imageview = (ImageView) findViewById(R.id.imageview);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1:
//                getMovie();
                login();
                break;
            case R.id.btn2:
                logout();
                break;
        }
    }


    private void getMovie() {
        mSubscription = NetWork.getMovieService().getTopMovie(0, 10).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);
    }

    private void login() {
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        User user = new User("18217612547", "123456", "", "1");
        String s = new Gson().toJson(user);
        Log.d(TAG, "login: " + s);
        RequestBody requestBody = RequestBody.create(mediaType, s);
        NetWork.kimService().login(requestBody).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful()) {
                    String string = response.body().toString();
                    Log.d(TAG, "body " + string);

                } else {
                    Log.d(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.d(TAG, t.getMessage().toString());
            }
        });
    }

    private void logout() {
        NetWork.kimService().logout("4e1ce3a2ee32457a89d11e16872db585").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.isSuccessful()) {
                    String body = "";
                    try {
                        body = response.body().string();
                        Log.d(TAG, body);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null && !mSubscriber.isUnsubscribed()) {
            mSubscriber.unsubscribe();
        }
    }

    Subscriber<Movie> mSubscriber = new Subscriber<Movie>() {
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
