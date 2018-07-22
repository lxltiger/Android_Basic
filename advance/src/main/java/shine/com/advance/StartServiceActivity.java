package shine.com.advance;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

/*启动SimpleService*/
public class StartServiceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "StartServiceActivity";
    private Intent mIntent;

    private boolean isbounded=false;
    private LocalService mLocalService;

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

    private ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLocalService = ((LocalService.LocalBinder) service).getService();
            Log.d(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLocalService=null;
            Log.d(TAG, "crash");
        }
    };
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //启动服务
            case R.id.btn_start_service:
                mIntent = new Intent(this, SimpleService.class);
                startService(mIntent);
                break;
            case R.id.btn_local_service:
                mIntent = new Intent(this, LocalService.class);
                startService(mIntent);
                break;
            case R.id.btn_stop_local_service:
                mIntent = new Intent(this, LocalService.class);
                stopService(mIntent);
                //TODO implement
                break;
            case R.id.btn_bind_local_service:
                bindService(new Intent(this,LocalService.class), mServiceConnection, Context.BIND_AUTO_CREATE);
                isbounded=true;
                break;
            case R.id.btn_unbind_local_service:
                if (isbounded) {
                    unbindService(mServiceConnection);
                    isbounded=false;
                }
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
        if (isbounded) {
            unbindService(mServiceConnection);
            isbounded=false;
        }
    }
}
