package discard;

import java.math.BigInteger;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;

/**
 * Created by 李晓林 on 2016/12/15
 * qq:1220289215
 */

public class NumberDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 5) {
            return;
        }
        in.markReaderIndex();
        byte magicNumber= in.readByte();
        if ('F' != magicNumber) {
            in.resetReaderIndex();
            throw new CorruptedFrameException("Invalid magic number: " + magicNumber);

        }
        //真正数据的长度
        int len = in.readInt();
        //必须等到数据全了
        if (in.readableBytes() < len) {
            in.resetReaderIndex();
            return;
        }
        byte[] buffer = new byte[len];
        in.readBytes(buffer);
        out.add(new BigInteger(buffer));
    }
}
