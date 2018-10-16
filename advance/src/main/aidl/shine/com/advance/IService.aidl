// IService.aidl
package shine.com.advance;
import shine.com.advance.IServiceCallBack;

// Declare any non-default types here with import statements

interface IService {
     /**
         * Often you want to allow a service to call back to its clients.
         * This shows how to do so, by registering a callback interface with
         * the service.
         */
 void register(IServiceCallBack cb);
 void unregister(IServiceCallBack cb);
}
