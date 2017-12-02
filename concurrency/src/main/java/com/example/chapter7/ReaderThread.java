package com.example.chapter7;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

/**
 * Created by 李晓林 on 2016/12/28
 * qq:1220289215
 */

public class ReaderThread extends Thread {
    private final Socket mSocket;
    private final InputStream mInputStream;

    ReaderThread(Socket socket) throws IOException {
        mSocket=socket;
        mInputStream=mSocket.getInputStream();
    }

    @Override
    public void run() {
        byte[] buff = new byte[100];
        while (true) {
            try {
                int count = mInputStream.read(buff);
                if (count > 0) {
                    process(buff,count);
                }else {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void process(byte[] buff, int count) {

    }

    @Override
    public void interrupt() {
        try {
            mSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            super.interrupt();
        }
    }
}
