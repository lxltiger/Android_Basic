package practise;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by 李晓林 on 2016/12/19
 * qq:1220289215
 * BIO测试
 */

 class Client {
    private static final ClientHandler clientHandler=new ClientHandler();
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        configBootstrap(bootstrap,nioEventLoopGroup).connect();

    }

    static Bootstrap configBootstrap(Bootstrap b, EventLoopGroup nioEventLoopGroup) {
        b.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .remoteAddress("172.168.2.156", 5020)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new IdleStateHandler(10,0,0),clientHandler);
                    }
                });

        return b;
    }

    static void connect(Bootstrap bootstrap) {
        bootstrap.connect().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.cause() != null) {
                    clientHandler.startTime=-1;
                    System.out.println("fail to connecet server "+future.cause());
                }
            }
        });
    }
}
