package beijing.hanhua.bigelephant;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Random;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;
import io.netty.util.CharsetUtil;

import static io.netty.util.CharsetUtil.UTF_8;

/**
 * Created by Administrator on 2016/7/14.
 * ByteBuf 的基本用法
 */
public class Demo {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int i1 = calendar.get(Calendar.HOUR_OF_DAY);
        System.out.println(i1);
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hellosdfsfsd", UTF_8);
        for (int i = 0; i < byteBuf.capacity(); i++) {
            byte aByte = byteBuf.getByte(i);
            System.out.print((char)aByte);
        }
        //discardReadBytes() 可以用来清空 ByteBuf 中已读取的数据，从而使 ByteBuf 有多余的空间容纳新的数据，
        // 但是discardReadBytes() 可能会涉及内存复制， 因为它需要移动 ByteBuf 中可读的字节到开始位置，
        // 这样的操作会影响性能，一般在需要马上释放内存的时候使用收益会比较大。
        byteBuf.discardReadBytes();
        Random random = new Random();

        while (byteBuf.writableBytes() > 4) {
            byteBuf.writeInt(random.nextInt());
        }
        //遍历缓冲区可用字节
        while (byteBuf.isReadable()) {
            System.out.println(byteBuf.readByte());
        }
        int i = byteBuf.forEachByte(ByteProcessor.FIND_NON_LINEAR_WHITESPACE);
        System.out.println("white space at"+i);
        //与原buff是内存共享的
        ByteBuf sliceBuf = byteBuf.slice(0, 5);
        System.out.println(sliceBuf.toString(CharsetUtil.UTF_8));
        sliceBuf.setByte(0,(byte)'D');
        boolean compare= byteBuf.getByte(0) == sliceBuf.getByte(0);
        System.out.println("result"+compare);

        ByteBuf copyBuffer = byteBuf.copy(0, 10);
        System.out.println(copyBuffer.toString(CharsetUtil.UTF_8));
        byteBuf.setByte(0, (byte) 'f');
        boolean result = byteBuf.getByte(0) == copyBuffer.getByte(0);
        System.out.println("result"+result+byteBuf.readerIndex()+"--write indexe"+byteBuf.writerIndex());
        if (byteBuf.hasArray()) {

            String s = ByteBufUtil.hexDump(byteBuf.array());
            System.out.println(s);
        }
        //直接缓冲区不支持数组访问，但可以间接访问
        ByteBuf directByteBuf = Unpooled.directBuffer(16);
        if (!directByteBuf.hasArray()) {
            int length = directByteBuf.readableBytes();
            byte[] bytes = new byte[length];
            directByteBuf.getBytes(0, bytes);
        }
        //综合
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        ByteBuf one = Unpooled.buffer(8);
        ByteBuf two = Unpooled.directBuffer(16);
        compositeByteBuf.addComponents(one, two);
        compositeByteBuf.removeComponent(0);

        Iterator<ByteBuf> iterator = compositeByteBuf.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next().toString());
        }



    }


}
