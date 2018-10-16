package shine.com.advance.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import shine.com.advance.R;
import shine.com.advance.service.ArgumentService;
import shine.com.advance.service.SimpleService;

public class StartServiceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "StartServiceActivity";
    private Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_service);
        findViewById(R.id.btn_start_service).setOnClickListener(this);
        findViewById(R.id.btn_local_service).setOnClickListener(this);
        findViewById(R.id.btn_stop_local_service).setOnClickListener(this);
        findViewById(R.id.btn_bind_local_service).setOnClickListener(this);
        findViewById(R.id.btn_unbind_local_service).setOnClickListener(this);
        findViewById(R.id.btn_start_argument_service).setOnClickListener(this);
        findViewById(R.id.btn_start_argument_service_redeliver).setOnClickListener(this);
        findViewById(R.id.btn_start_fail).setOnClickListener(this);
        findViewById(R.id.btn_kill_process).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //启动服务
            case R.id.btn_start_service:
                mIntent = new Intent(this, SimpleService.class);
                startService(mIntent);
                break;

            case R.id.btn_start_argument_service:
                //不需重启的
                mIntent = new Intent(this, ArgumentService.class);
                mIntent.putExtra("name", "lxl");
                startService(mIntent);
                break;

            case R.id.btn_start_argument_service_redeliver:
                mIntent = new Intent(this, ArgumentService.class);
                mIntent.putExtra("name", "lxldfff");
                //程序被杀死后重处理intent
                mIntent.putExtra("redeliver",true);
                startService(mIntent);
                break;
            case R.id.btn_start_fail:
                mIntent = new Intent(this, ArgumentService.class);
                mIntent.putExtra("name", "lxlghgdfff");
                //程序被杀死后重处理intent
                mIntent.putExtra("fail",true);
                startService(mIntent);
                break;
            case R.id.btn_kill_process:
                Process.killProcess(Process.myPid());
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
