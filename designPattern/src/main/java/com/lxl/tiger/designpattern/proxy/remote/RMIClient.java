package com.lxl.tiger.designpattern.proxy.remote;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;

public class RMIClient {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            //            列出注册的所有可用的远程对象。
            String[] list1 = registry.list();
            System.out.println(Arrays.toString(list1));

            MyRemote remote = (MyRemote) registry.lookup("rmi://127.0.0.1:1099/hello");
            String s = remote.sayHello();
            System.out.println(s);
        } catch (NotBoundException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
