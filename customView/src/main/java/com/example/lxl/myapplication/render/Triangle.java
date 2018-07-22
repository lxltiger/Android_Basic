package com.example.lxl.myapplication.render;


import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glLinkProgram;


/**
 * Created by lixiaolin on 17/8/12.
 * 绘制三角形的参数
 */

public class Triangle {

    //
    static int COORDS_PER_VERTEX = 3;
    static float[] COORDS = {
            0.0f, 0.622008459f, 0.0f, // top
            -0.5f, -0.311004243f, 0.0f, // bottom left
            0.5f, -0.311004243f, 0.0f
    };
    private final int mProgram;
    // Set color with red, green, blue and alpha (opacity) values
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    //顶点数量
    private final int vertexCount = COORDS.length / COORDS_PER_VERTEX;

    public static final int vertexStride = COORDS_PER_VERTEX * 4;

    private final FloatBuffer mVertexBuffer;


    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private int mVPositionHander;
    private int mVColorHandler;
    private int mUMVPMatrixHandler;


    public Triangle() {
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(COORDS.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        mVertexBuffer = byteBuffer.asFloatBuffer();
        mVertexBuffer.put(COORDS);

        mVertexBuffer.position(0);

        int vertextShader = MyGLRender.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = MyGLRender.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();

        glAttachShader(mProgram, vertextShader);
        glAttachShader(mProgram, fragmentShader);
        glLinkProgram(mProgram);

    }

    public void draw(float[] MVPMatrix) {
        GLES20.glUseProgram(mProgram);

        //        获取属性的位置，有个这个位置OPenGl就知道去哪儿找对应的数据
        mVPositionHander = GLES20.glGetAttribLocation(mProgram, "vPosition");
        GLES20.glEnableVertexAttribArray(mVPositionHander);
        GLES20.glVertexAttribPointer(mVPositionHander, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, mVertexBuffer);

        //获取在着色器中定义的uniform的位置，位置在一个程序对象中是唯一的，用来个着色器发送数据
        mVColorHandler = GLES20.glGetUniformLocation(mProgram, "vColor");
        GLES20.glUniform4fv(mVColorHandler, 1, color, 0);

        mUMVPMatrixHandler = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        MyGLRender.checkGlError("glGetUniformLocation");

        GLES20.glUniformMatrix4fv(mUMVPMatrixHandler, 1, false, MVPMatrix, 0);
        MyGLRender.checkGlError("glUniformMatrix4fv");

        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        GLES20.glDisableVertexAttribArray(mVPositionHander);
    }

}
