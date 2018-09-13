package com.lxl.tiger.designpattern.proxy;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote是一个标记接口
 *
 * 里面的方法
 */
public interface MyRemote extends Remote {
     String sayHello() throws RemoteException;
}
