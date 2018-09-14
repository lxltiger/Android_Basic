package com.lxl.tiger.designpattern.proxy.remote;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * 在server端只需要做两件事：
 * 创建并导出远程对象
 * 用Java RMI registry 注册远程对象
 */
public class RMIServer {
    public static void main(String[] args) {
        try {
            MyRemote remote = new MyRemoteImpl();
//            导出存根
            MyRemote stub = (MyRemote) UnicastRemoteObject.exportObject(remote, 9998);
            Registry registry = LocateRegistry.createRegistry(1099);
//            绑定存根到一个名称，供客户端获取，

            registry.bind("rmi://127.0.0.1:1099/hello", stub);
            System.out.println("success");
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
