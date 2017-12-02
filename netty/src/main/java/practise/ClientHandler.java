package practise;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by 李晓林 on 2016/12/19
 * qq:1220289215
 */
@ChannelHandler.Sharable
 class ClientHandler extends SimpleChannelInboundHandler<Object>{
    long startTime=-1;
    private static final String TAG = "ClientHandler";
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (startTime < 0) {
            startTime=System.currentTimeMillis();
        }
        System.out.printf("the mothod %s in %s is called \n","channelActive",TAG);
        print("connect to "+ctx.channel().remoteAddress().toString());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.printf("the mothod %s in %s is called\n ","channelRead0",TAG);

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (!(evt instanceof IdleStateEvent)) {
            return;
        }
        IdleStateEvent idleStateEvent= (IdleStateEvent) evt;
        if (idleStateEvent.state() == IdleState.READER_IDLE) {
            print("disconnect due to no inbound traffic");
            ctx.close();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        print("disconnect from " + ctx.channel().remoteAddress());

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        print("sleep for 5 s");
         final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                print("reconnect to server");
                Client.connect(Client.configBootstrap(new Bootstrap(), eventLoop));
            }
        },5, TimeUnit.SECONDS);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.printf("the mothod %s in %s is called \n","exceptionCaught",TAG);
        cause.printStackTrace();
        ctx.close();
    }

   private  void print(String msg) {
        if (startTime > 0) {
            System.err.format("[uptime %5ds] %s%n", (System.currentTimeMillis() - startTime) / 1000, msg);
        }else{
            System.err.format("[server is down] %s%n", msg);
        }
    }
}
