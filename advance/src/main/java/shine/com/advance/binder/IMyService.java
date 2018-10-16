package shine.com.advance.binder;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IMyService extends IInterface {
    String DESCRIPTOR = "shine.com.advance.binder.mysevice";

    void sayHello(String str) throws RemoteException;

    int transaction_say = IBinder.FIRST_CALL_TRANSACTION;

}
