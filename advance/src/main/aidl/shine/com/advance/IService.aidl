// IService.aidl
package shine.com.advance;
import shine.com.advance.IServiceCallBack;

// Declare any non-default types here with import statements

interface IService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
 void register(IServiceCallBack cb);
 void unregister(IServiceCallBack cb);
}
