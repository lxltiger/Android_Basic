package com.shine.cross_walk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.xwalk.core.XWalkView;

/**
 * 使用CrossWalk代替WebView(不能使用WebSocket)
 *
 */
public class MainActivity extends AppCompatActivity {

    private XWalkView mXWalkView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mXWalkView = findViewById(R.id.walkView);
        mXWalkView.loadUrl("http://172.168.1.69:8081/rtaserver/");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mXWalkView != null) {
            mXWalkView.pauseTimers();
            mXWalkView.onHide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mXWalkView != null) {
            mXWalkView.resumeTimers();
            mXWalkView.onShow();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mXWalkView != null) {
            mXWalkView.onDestroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        if (mXWalkView != null) {
            mXWalkView.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (mXWalkView != null) {
            mXWalkView.onNewIntent(intent);
        }
    }
}
