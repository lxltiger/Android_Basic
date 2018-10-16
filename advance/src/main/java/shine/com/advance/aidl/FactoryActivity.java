package shine.com.advance.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import shine.com.advance.IFactory;
import shine.com.advance.R;
import shine.com.advance.User;

public class FactoryActivity extends AppCompatActivity {

    private boolean bound;
    private IFactory factoryService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, FactoryService.class);
        intent.setAction(IFactory.class.getName());
        bound = bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (bound) {
            unbindService(serviceConnection);
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            factoryService = IFactory.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            factoryService = null;
        }
    };

    public void show(View view) {
        try {
            int uid = factoryService.getUid();
            User user = factoryService.get();
            Toast.makeText(this, "getUid: " + user.getName(), Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();

        }
    }
}
