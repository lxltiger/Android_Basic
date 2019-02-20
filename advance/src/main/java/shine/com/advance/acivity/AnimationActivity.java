package shine.com.advance.acivity;

import android.app.ActivityManager;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import shine.com.advance.R;
import shine.com.advance.render.MyRender;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.frustumM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.setLookAtM;
import static shine.com.advance.GLUtils.compileShader;
import static shine.com.advance.GLUtils.createProgram;
import static shine.com.advance.GLUtils.loadShaderResource;


public class AnimationActivity extends AppCompatActivity {
    private static final String TAG = "AnimationActivity";
    private GLSurfaceView mGlSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlSurfaceView = new GLSurfaceView(this);
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        boolean support = activityManager.getDeviceConfigurationInfo().reqGlEsVersion >= 0x2000;
        if (support) {
            mGlSurfaceView.setEGLContextClientVersion(2);
            mGlSurfaceView.setRenderer(new MyRender(this));
//            mGlSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        } else {
            return;
        }
        setContentView(mGlSurfaceView);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mGlSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGlSurfaceView.onResume();
    }


    private final static class MyCircleRender implements GLSurfaceView.Renderer {
        private final float[] mVertex;
        private final static int FLOAT_PER_VERTEX = 3;
        private final static int NUMBER_OF_VERTEX = 60;
        private final FloatBuffer mFloatBuffer;
        private float[] mProjectionMatrix = new float[16];
        private float[] mModelMatrix = new float[16];
        private float[] mViewMatrix = new float[16];
        private float[] mMVPMatrix = new float[16];

        private Context mContext;
        private int mU_mvpMatrix;
        private int mColorHandler;

        public MyCircleRender(Context context) {
            mContext = context;
            mVertex = new float[(NUMBER_OF_VERTEX + 2)  * FLOAT_PER_VERTEX+(NUMBER_OF_VERTEX + 1)*2*FLOAT_PER_VERTEX];
            int count = 0;
            mVertex[count++] = 0;
            mVertex[count++] = 0.8f;
            mVertex[count++] = 0;
//            圆柱顶面
            for (int i = 0; i <= NUMBER_OF_VERTEX; i++) {
                float radius = ((float) i / NUMBER_OF_VERTEX) * (float) (2 * Math.PI);
                mVertex[count++] = (float) Math.cos(radius);
                mVertex[count++] = 0.8f;
                mVertex[count++] = (float) Math.sin(radius);

            }
//            圆柱侧边
            for (int i = 0; i <= NUMBER_OF_VERTEX; i++) {
                float radius = ((float) i / NUMBER_OF_VERTEX) * (float) (2 * Math.PI);
                mVertex[count++] = (float) Math.cos(radius);
                mVertex[count++] = 0.8f;
                mVertex[count++] = (float) Math.sin(radius);

                mVertex[count++] = (float) Math.cos(radius);
                mVertex[count++] = 0f;
                mVertex[count++] = (float) Math.sin(radius);

            }


            mFloatBuffer = ByteBuffer.allocateDirect(mVertex.length * 4)
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer()
                    .put(mVertex);

        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            glClearColor(0f, 0f, 0f, 1f);
            String vertexSource = loadShaderResource(mContext, R.raw.vertex_shader);
            String fragmentSource = loadShaderResource(mContext, R.raw.fragment_shader);
            int vertexId = compileShader(GL_VERTEX_SHADER, vertexSource);
            int fragmentId = compileShader(GL_FRAGMENT_SHADER, fragmentSource);
            int program = createProgram(vertexId, fragmentId);

            glUseProgram(program);

            int positionHandler = glGetAttribLocation(program, "a_Position");
            mFloatBuffer.position(0);
            glVertexAttribPointer(positionHandler, 3, GL_FLOAT, false, 0, mFloatBuffer);
            glEnableVertexAttribArray(positionHandler);

            mColorHandler = glGetUniformLocation(program, "u_Color");
            mU_mvpMatrix = glGetUniformLocation(program, "u_mvpMatrix");
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            glViewport(0, 0, width, height);
            float radio = width * 1f / height;
            setLookAtM(mViewMatrix, 0, 0, 1.2f, 2.2f, 0, 0, 0, 0, 1, 0);
            frustumM(mProjectionMatrix, 0, -radio, radio, -1, 1, 1, 10);
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            glClear(GL_COLOR_BUFFER_BIT);
            setIdentityM(mModelMatrix, 0);
            multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
            multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

            glUniformMatrix4fv(mU_mvpMatrix, 1, false, mMVPMatrix, 0);
            glUniform4f(mColorHandler, 1f, 1f, 1f, 1f);
            glDrawArrays(GL_TRIANGLE_STRIP, NUMBER_OF_VERTEX + 2, 2 * (NUMBER_OF_VERTEX + 1));

            glUniform4f(mColorHandler, 0.5f, 0.6f, 0.8f, 1f);
            glDrawArrays(GL_TRIANGLE_FAN, 0, NUMBER_OF_VERTEX + 2);
        }
    }

}

