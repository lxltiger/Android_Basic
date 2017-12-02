package discard;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by 李晓林 on 2016/12/9
 * qq:1220289215
 */

public class DiscardClient {
    public static final int COUNT=10;
    public static void main(String[] args) throws InterruptedException {
        Bootstrap bootstrap=new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(EpollChannelOption.TCP_NODELAY,true)
                    .handler(new DiscardClientChannelInitializer());
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9921).sync();
            System.out.println("connect server");
            DiscardClientHandler handler = (DiscardClientHandler) channelFuture.channel().pipeline().last();
            System.out.printf("the factorial of %d is %d",COUNT,handler.getFactorial());
            //等待连接关闭
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }
}
