package shine.com.advance.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import shine.com.advance.IFactory;
import shine.com.advance.User;

public class FactoryService extends Service {
    private static final String TAG = "FactoryService";
    public FactoryService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    private final IFactory.Stub binder =new IFactory.Stub() {
        @Override
        public int getUid() throws RemoteException {

            return android.os.Process.myPid();
        }

        @Override
        public User get() throws RemoteException {
            User user = new User();
            user.setAge(31);
            user.setName("ji");
            return user;
        }

        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            Log.d(TAG, "onTransact() called with: code = [" + code + "], data = [" + data + "], reply = [" + reply + "], flags = [" + flags + "]");
            return super.onTransact(code, data, reply, flags);
        }
    };
}
