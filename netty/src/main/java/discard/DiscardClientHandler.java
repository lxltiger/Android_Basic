package discard;

import java.math.BigInteger;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by 李晓林 on 2016/12/9
 * qq:1220289215
 */

public class DiscardClientHandler extends SimpleChannelInboundHandler<BigInteger> {
    private ChannelHandlerContext mChannelHandlerContext;
    final BlockingQueue<BigInteger> mQueue = new LinkedBlockingQueue<>();
    private int next=1;
    private int receive;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        mChannelHandlerContext = ctx;
        System.out.println("client channel active");
//        ctx.writeAndFlush(Unpooled.wrappedBuffer("hello".getBytes()));
        sendNumber();
    }
    private void sendNumber() {
        ChannelFuture channelFuture=null;
        for (int i = 0; i < 4096&&next<=DiscardClient.COUNT; i++) {
            channelFuture = mChannelHandlerContext.write(Integer.valueOf(next));
            next++;
        }
        if (next <= DiscardClient.COUNT) {
            assert channelFuture!=null;
            channelFuture.addListener(mNumberSenderListener);
        }
        mChannelHandlerContext.flush();
    }

    private final ChannelFutureListener mNumberSenderListener=new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()) {
                sendNumber();
            }else{
                future.cause().printStackTrace();
                future.channel().close();
            }
        }
    };
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BigInteger msg) throws Exception {
        receive++;
        if (receive == DiscardClient.COUNT) {
            ctx.channel().close().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    boolean offer = mQueue.offer(msg);
                    assert offer;
                }
            });
        }
    }

   /* @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead");
       *//* ByteBuf buf= (ByteBuf) msg;
        try {
            System.out.println("read from server"+buf.toString(Charset.defaultCharset()));
        } finally {
            ReferenceCountUtil.release(buf);
        }*//*
    }*/

    public BigInteger getFactorial() {
        boolean interrupted=false;
        for (; ; ) {
            try {
               return mQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
                interrupted=true;
            }finally {
                if (interrupted) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
   /* @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("read complete");
    }*/

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
