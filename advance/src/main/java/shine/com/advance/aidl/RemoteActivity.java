package shine.com.advance.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import shine.com.advance.ISecondary;
import shine.com.advance.IService;
import shine.com.advance.IServiceCallBack;
import shine.com.advance.R;

public class RemoteActivity extends AppCompatActivity {

    private IService service;
    private ISecondary secondary;
    private boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote);
    }

    public void startService(View view) {
        startService(new Intent(this, RemoteService.class));
    }

    public void stopService(View view) {
        stopService(new Intent(this, RemoteService.class));

    }


    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = IService.Stub.asInterface(binder);
            if (service != null) {
                try {
                    service.register(callBack);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }
    };

    ServiceConnection secondConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            secondary = ISecondary.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            secondary = null;
        }
    };

    private IServiceCallBack callBack = new IServiceCallBack.Stub() {
        @Override
        public void onChange(int value) throws RemoteException {
            Log.d("RemoteActivity", "value:" + value);

        }
    };

    public void bindService(View view) {
        Intent intent = new Intent(this, RemoteService.class);
        intent.setAction(IService.class.getName());
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        intent.setAction(ISecondary.class.getName());
        bindService(intent, secondConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    public void unbindService(View view) {
        if (isBound) {
            if (service != null) {
                try {
                    service.unregister(callBack);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            unbindService(serviceConnection);
            unbindService(secondConnection);
            isBound = false;
        }
    }

    public void killService(View view) {
        if (secondary!=null) {
            try {
                int pid = secondary.getPid();
                Process.killProcess(pid);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
