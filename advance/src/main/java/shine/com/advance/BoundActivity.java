package shine.com.advance;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/*演示绑定服务的页面
* 当通过绑定启动的服务没有绑定时会销毁*/
public class BoundActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BoundActivity";
    private BoundService mBoundService;
    private boolean isBound=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bound);

        findViewById(R.id.btn_bind).setOnClickListener(this);
        findViewById(R.id.btn_unbind).setOnClickListener(this);
        findViewById(R.id.btn_show).setOnClickListener(this);
    }

    private ServiceConnection mServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BoundService.LocalBinder localBinder= (BoundService.LocalBinder) service;
            mBoundService=localBinder.getService();
            isBound=true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound=false;
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_bind:
                Intent intent = new Intent(this, BoundService.class);
                bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
                break;
            case R.id.btn_unbind:
                if (isBound) {
                    unbindService(mServiceConnection);
                    isBound=false;
                }
                break;
            case R.id.btn_show:
                if (isBound) {
                    int number=mBoundService.generateInt();
                    Toast.makeText(this, "number:" + number, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isBound) {
            unbindService(mServiceConnection);
            isBound=false;
        }
    }
}
