package discard;

import java.math.BigInteger;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created by 李晓林 on 2016/12/15
 * qq:1220289215
 */

public class NumberEncode extends MessageToByteEncoder<Number>{
    @Override
    protected void encode(ChannelHandlerContext ctx, Number msg, ByteBuf out) throws Exception {
        BigInteger bigInteger;
        if (msg instanceof BigInteger) {
            bigInteger= (BigInteger) msg;
        }else{
            bigInteger=new BigInteger(String.valueOf(msg));
        }
        byte[] bytes = bigInteger.toByteArray();
        out.writeByte((byte)'F');
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
