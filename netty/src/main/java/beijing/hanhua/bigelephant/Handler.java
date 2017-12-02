package beijing.hanhua.bigelephant;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by 李晓林 on 2016/12/27
 * qq:1220289215
 */

public final class Handler implements Runnable {
    final SocketChannel mSocketChannel;
    final SelectionKey mSelectionKey;
    static final int READING=0;
    static final int SENDING=1;
    int status=READING;
    ByteBuffer input = ByteBuffer.allocate(100);
    ByteBuffer output = ByteBuffer.allocate(100);

    public Handler(Selector selector, SocketChannel socketChannel) throws IOException {
        mSocketChannel = socketChannel;
        mSocketChannel.configureBlocking(false);
        mSelectionKey=mSocketChannel.register(selector, 0);
        mSelectionKey.attach(this);
        mSelectionKey.interestOps(SelectionKey.OP_READ);
        //?
        selector.wakeup();
    }

    boolean inputIsComplete() {
        return true;
    }
    boolean outputIsComplete() {
        return true;
    }

    void process() {

    }
    @Override
    public void run() {
        try {
            if (status == SENDING) {
                send();
            } else if (status == READING) {
                read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void read() throws IOException {
        mSocketChannel.read(input);
        if (inputIsComplete()) {
            process();
            status=SENDING;
            mSelectionKey.interestOps(SelectionKey.OP_WRITE);

        }
    }

    void send() throws IOException {
        mSocketChannel.write(output);
        if (outputIsComplete()) {
            mSelectionKey.cancel();
        }
    }
}
