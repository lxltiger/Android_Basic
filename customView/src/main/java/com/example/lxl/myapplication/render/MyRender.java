package com.example.lxl.myapplication.render;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.lxl.myapplication.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BITS;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.frustumM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static android.opengl.Matrix.translateM;
import static com.example.lxl.myapplication.utils.GLUtils.compileShader;
import static com.example.lxl.myapplication.utils.GLUtils.createProgram;
import static com.example.lxl.myapplication.utils.GLUtils.loadShaderResource;

public class MyRender implements GLSurfaceView.Renderer {
    private static final String TAG = MyRender.class.getSimpleName();
    private Context mContext;
    private FloatBuffer mFloatBuffer;
    private FloatBuffer mFloatBuffer2;
    private FloatBuffer mFloatBuffer3;
    private float[] mViewMatrix = new float[16];
    private float[] modleMatrix = new float[16];
    private float[] projectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];

    final float[] triangle1VerticesData = {
            // X, Y, Z,
            // R, G, B, A
            -0.5f, -0.25f, 0.0f,
            1.0f, 0.0f, 0.0f, 1.0f,

            0.5f, -0.25f, 0.0f,
            0.0f, 0.0f, 1.0f, 1.0f,

            0.0f, 0.559016994f, 0.0f,
            0.0f, 1.0f, 0.0f, 1.0f};
    final float[] triangle1VerticesData2 = {
            // X, Y, Z,
            -0.5f, -0.25f, 0.0f,
            1.0f, 1.0f, 0.0f, 1.0f,

            0.5f, -0.25f, 0.0f,
            0.0f, 1.0f, 1.0f, 1.0f,

            0.0f, 0.559016994f, 0.0f,
            1.0f, 0.0f, 1.0f, 1.0f};

    final float[] triangle1VerticesData3 = {
            // X, Y, Z,
            // R, G, B, A
            -0.5f, -0.25f, 0.0f,
            1.0f, 1.0f, 1.0f, 1.0f,

            0.5f, -0.25f, 0.0f,
            0.5f, 0.5f, 0.5f, 1.0f,

            0.0f, 0.559016994f, 0.0f,
            0.0f, 0.0f, 0.0f, 1.0f};

    private int mMvpHandler;
    private int mProgram;
    final float eyeX = 0.0f;
    final float eyeY = 0.0f;
    final float eyeZ = 1.5f;

    // We are looking toward the distance
    final float lookX = 0.0f;
    final float lookY = 0f;
    final float lookZ = -5.0f;

    // Set our up vector. This is where our head would be pointing were we holding the camera.
    final float upX = 0.0f;
    final float upY = 1.0f;
    final float upZ = 0.0f;
    private int mPositionHandler;
    private int mColorHandler;

    public MyRender(Context context) {
        mContext = context;
        mFloatBuffer = ByteBuffer.allocateDirect(triangle1VerticesData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(triangle1VerticesData);
        mFloatBuffer2 = ByteBuffer.allocateDirect(triangle1VerticesData2.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(triangle1VerticesData2);
        mFloatBuffer3 = ByteBuffer.allocateDirect(triangle1VerticesData3.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(triangle1VerticesData3);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1f, 1f, 1f, 1f);
        // Position the eye behind the origin.

        String vertexSource = loadShaderResource(mContext, R.raw.vertex_shader);
        String fragmentSource = loadShaderResource(mContext, R.raw.fragment_shader);
        int vertexId = compileShader(GL_VERTEX_SHADER, vertexSource);
        int fragmentId = compileShader(GL_FRAGMENT_SHADER, fragmentSource);
        mProgram = createProgram(vertexId, fragmentId);

        mPositionHandler = glGetAttribLocation(mProgram, "a_Position");
        mColorHandler = glGetAttribLocation(mProgram, "a_Color");
        mMvpHandler = glGetUniformLocation(mProgram, "u_mvpMatrix");

        glUseProgram(mProgram);


    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        glViewport(0, 0, width, height);
        float radio = width * 1f / height;
        setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
        frustumM(projectionMatrix, 0, -radio, radio, -1, 1, 1, 10);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BITS);

        long temp = System.currentTimeMillis() % 10000;
        float angel = 360f / 10000 * temp;
        setIdentityM(modleMatrix, 0);
        rotateM(modleMatrix,0,angel,0,0,1);
        drawTriange(mFloatBuffer);

        setIdentityM(modleMatrix, 0);
        translateM(modleMatrix, 0, 0, -1, 0);
        rotateM(modleMatrix,0,90,1,0,0);
        rotateM(modleMatrix, 0, angel, 0, 0, 1);
        drawTriange(mFloatBuffer2);

        setIdentityM(modleMatrix, 0);
        translateM(modleMatrix, 0, 1, 0, 0);
        rotateM(modleMatrix,0,90,0,1,0);
        rotateM(modleMatrix,0,angel,0,0,1);
        drawTriange(mFloatBuffer3);


    }

    private void drawTriange(FloatBuffer floatBuffer) {

        floatBuffer.position(0);
        glVertexAttribPointer(mPositionHandler, 3, GL_FLOAT, false, 7 * 4, floatBuffer);
        glEnableVertexAttribArray(mPositionHandler);

        floatBuffer.position(3);
        glVertexAttribPointer(mColorHandler, 4, GL_FLOAT, false, 7 * 4, floatBuffer);
        glEnableVertexAttribArray(mColorHandler);

        multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, modleMatrix, 0);
        multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, mMVPMatrix, 0);
        glUniformMatrix4fv(mMvpHandler, 1, false, mMVPMatrix, 0);

        glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}