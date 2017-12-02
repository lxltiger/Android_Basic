package discard;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by 李晓林 on 2016/12/9
 * qq:1220289215
 */

public class DiscardClientChannelInitializer extends ChannelInitializer<SocketChannel>{

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new NumberDecoder());
        pipeline.addLast(new NumberEncode());
        pipeline.addLast(new DiscardClientHandler());
    }
}
