package discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Created by 李晓林 on 2016/12/9
 * qq:1220289215
 */

public class DiscardServer {
    public static void main(String[] args) throws InterruptedException {
        ServerBootstrap serverBootstrap=new ServerBootstrap();
        //用来处理客户端的连接，把客户端连接上注册到worker上
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        //用来处理和客户端连接的通信
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            serverBootstrap.group(boss,worker)
                    //使用NioServerSocketChannel实例化一个channel来接受客户端连接
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new DiscardServerChannelInitializer())
                    //option 对应接受连接的NioServerSocketChannel
                    .option(ChannelOption.SO_BACKLOG,128)
                    //childOption对应被父ServerChannel（NioServerSocketChannel）所接受的Channel
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            //邦定端口来接受连接，阻塞式
            ChannelFuture channelFuture = serverBootstrap.bind(9921).sync();
            System.out.println("get connection");
            //等待关闭 阻塞
            channelFuture.channel().closeFuture().sync();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
