package telnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by 李晓林 on 2016/12/16
 * qq:1220289215
 */

public class Client {
    public static void main(String[] args) throws InterruptedException, IOException {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap=new Bootstrap();
        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ClientChannelInitializer());
            Channel channel = bootstrap.connect("127.0.0.1", 9211).sync().channel();
            ChannelFuture lastWriteFuture=null;
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            for(;;) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                lastWriteFuture = channel.writeAndFlush(line + "\r\n");
                if ("bye".equals(line.toLowerCase())) {
                    channel.closeFuture().sync();
                    break;
                }

            }
            //如果不为空，把所有内容都刷到网络再关闭
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }

        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
