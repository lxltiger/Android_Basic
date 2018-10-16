package shine.com.advance.binder;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyService extends Binder implements IMyService{

    public MyService() {
        attachInterface(this,DESCRIPTOR);
    }

    public static IMyService asInterface(Binder obj) {
        if (obj == null) {
            return null;
        }
        IInterface iInterface = obj.queryLocalInterface(DESCRIPTOR);
        if (iInterface instanceof IMyService) {
            return (IMyService) iInterface;
        }

        return null;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;
            case IMyService.transaction_say:
                data.enforceInterface(DESCRIPTOR);
                String s = data.readString();
                sayHello(s);
                reply.writeNoException();
                return  true;
        }

        return super.onTransact(code, data, reply, flags);
    }

    @Override
    public void sayHello(String str) throws RemoteException {
        Log.d("MyService", "get from client"+str);

    }

    @Override
    public IBinder asBinder() {
        return this;
    }
}
