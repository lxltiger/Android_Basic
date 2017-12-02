package com.example.chapter7;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;

/**
 * Created by 李晓林 on 2016/12/28
 * qq:1220289215
 */

public abstract class SocketUsingTask<T> implements CancellableTask<T>{
    private Socket mSocket;

    protected synchronized void setSocket(Socket socket) {
        mSocket = socket;

    }
    @Override
    public synchronized void cancel() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RunnableFuture<T> newTask() {
        return new FutureTask<T>(this){
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                try {
                    SocketUsingTask.this.cancel();
                } finally {
                    return super.cancel(mayInterruptIfRunning);
                }
            }
        };
    }

}
