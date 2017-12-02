package beijing.hanhua.bigelephant;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by 李晓林 on 2016/12/27
 * qq:1220289215
 * 关于Channel的官方定义
 * A channel represents an open connection to an entity such as a hardware device, a file, a network socket,
 * or a program component that is capable of performing one or more distinct I/O operations,
 * for example reading or writing.
 * <p>
 * 并发库作者定义Channel:
 * Connections to files, sockets etc that support non-blocking reads
 */

public class Reactor implements Runnable {
    /**
     * 通知channel集合哪一个有io事件
     * 有三种selection keys集合
     * 1.注册在这个selector的channel都有selectionKey，keys方法返回就是这样的key的集合
     * 2 在上一次的选择过程中，如果channel处在其注册的感兴趣的某种状态，会在 selectedKeys方法返回的key集合中
     * 3.cancelled-key
     */
    final Selector mSelector;
    /**
     * A selectable channel for stream-oriented listening sockets.
     * Server-socket channels are safe for use by multiple concurrent threads.
     */
    final ServerSocketChannel mServerSocketChannel;

    public Reactor(int port) throws IOException {
        //使用默认的选择器，可以使用SelectorProvider自定义
        mSelector = Selector.open();
        mServerSocketChannel = ServerSocketChannel.open();
        //绑定端口，如果没邦定就accept会NotYetBoundException
        mServerSocketChannel.socket().bind(new InetSocketAddress(port));
        //设置为非阻塞
        mServerSocketChannel.configureBlocking(false);
        /**
         * 当我们使用 register 注册一个 Channel 时, 会返回一个 SelectionKey 对象, 这个对象包含了如下内容:
         *interest set, 即我们感兴趣的事件集, 即在调用 register 注册 channel 时所设置的 interest set.
         *ready set
         *channel
         *selector
         *attached object, 可选的附加对象
         *并发库作者定义
         * SelectionKey Maintain IO event status and bindings
         */
        SelectionKey selectionKey = mServerSocketChannel.register(mSelector, SelectionKey.OP_ACCEPT);
        selectionKey.attach(new Acceptor());
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
//                Selector.select()方法获取对某件事件准备好了的 Channel,
                mSelector.select();
                Set<SelectionKey> selectionKeys = mSelector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    dispatch(next);
                }
                selectionKeys.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey next) {
        Runnable r = (Runnable) next.attachment();
        if (r != null) {
            r.run();
        }
    }

    class Acceptor implements Runnable {
        @Override
        public void run() {
            try {
                SocketChannel accept = mServerSocketChannel.accept();
                if (accept != null) {
                    new Handler(mSelector, accept);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
