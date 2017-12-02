package uptime;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by 李晓林 on 2016/12/19
 * qq:1220289215
 */
@ChannelHandler.Sharable
 class ServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final String TAG = "ServerHandler";
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.printf("the mothod %s in %s is called \n","channelActive",TAG);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.printf("the mothod %s in %s is called \n","channelRead0",TAG);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.printf("the mothod %s in %s is called \n","exceptionCaught",TAG);
        cause.printStackTrace();
        ctx.close();
    }
}
