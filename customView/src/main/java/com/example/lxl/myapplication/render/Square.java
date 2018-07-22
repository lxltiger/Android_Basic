package com.example.lxl.myapplication.render;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by lixiaolin on 17/8/12.
 */

public class Square {
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f }; // top right
    private final FloatBuffer mVertexBuffer;
    private final ShortBuffer mNumbers;

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    public Square() {

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(squareCoords.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put(squareCoords);
        mVertexBuffer.position(0);

        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(drawOrder.length * 2);
        byteBuffer1.order(ByteOrder.nativeOrder());
        mNumbers = byteBuffer1.asShortBuffer();
        mNumbers.put(drawOrder);
        mNumbers.position(0);



    }
}
