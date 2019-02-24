package shine.com.advance.acivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.util.List;
import java.util.Random;

import shine.com.advance.R;
import shine.com.advance.algor.BubbleSort;
import shine.com.advance.customview.SortView;

public class SortActivity extends AppCompatActivity {

    private SortView sortView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sort);
        sortView = findViewById(R.id.sort_view);


    }

    public void test(View view) {
        sortView.start();
    }
}
