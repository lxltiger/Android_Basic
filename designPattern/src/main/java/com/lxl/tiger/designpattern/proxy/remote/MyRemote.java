package com.lxl.tiger.designpattern.proxy.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Remote是一个标记接口
 *
 * 里面的方法必须抛出RemoteException
 * 方法的参数和返回值必须是基本类型或者序列化了
 */
public interface MyRemote extends Remote {
     String sayHello() throws RemoteException;
}
