package telnet;

import java.util.Date;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by 李晓林 on 2016/12/16
 * qq:1220289215
 */
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String>{

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.write("welcome\n");
        ctx.write("it;s"+new Date()+"today\n");
        ctx.flush();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        String response;
        boolean disconnect=false;
        if (request.length()==0) {
            response = "type something\r\n";
        } else if ("bye".equals(request)) {
            disconnect=true;
            response = "welcome next time\r\n";
        }else{
            response = "did you say " + request + "?\r\n";
        }
        ChannelFuture channelFuture = ctx.write(response);
        if (disconnect) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
