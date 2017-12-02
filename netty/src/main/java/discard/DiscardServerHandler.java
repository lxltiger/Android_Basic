package discard;

import java.math.BigInteger;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created by 李晓林 on 2016/12/9
 * qq:1220289215
 * 处理接收到的用户数据
 */

public class DiscardServerHandler extends SimpleChannelInboundHandler<BigInteger> {
    private BigInteger last = new BigInteger("1");
    private BigInteger factorial = new BigInteger("1");
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
       /* final ByteBuf buffer = ctx.alloc().buffer(4);
        buffer.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
        ChannelFuture channelFuture = ctx.writeAndFlush(buffer);*/
        System.out.println("server channel active");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelInactive");
        System.out.printf("the factorial of %d if %d",last,factorial);

    }

    /*@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead");
//        ByteBuf buf = (ByteBuf) msg;
       *//* try {
            System.out.println(buf.toString(Charset.defaultCharset()));
        } finally {
            ReferenceCountUtil.release(msg);
        }*//*
        //将数据写入到缓冲
//        ctx.write(buf);
        //将数据刷到网络，此时会自动release buf，不需要调用ReferenceCountUtil.release(msg)
//        ctx.flush();

    }*/

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BigInteger msg) throws Exception {
        last = msg;
        factorial = factorial.multiply(msg);
        ctx.writeAndFlush(factorial);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印错误信息
        cause.printStackTrace();
        ctx.close();
    }
}
