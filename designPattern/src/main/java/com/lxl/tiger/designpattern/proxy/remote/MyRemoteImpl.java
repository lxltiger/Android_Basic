package com.lxl.tiger.designpattern.proxy.remote;

import java.rmi.RemoteException;

public class MyRemoteImpl  implements MyRemote {

    protected MyRemoteImpl() throws RemoteException {
    }


    public String sayHello()  {
        return "server say hello";
    }
}
