package com.example.lxl.myapplication.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramInfoLog;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderInfoLog;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;

/**
 * author:
 * 时间:2017/11/4
 * qq:1220289215
 * 类描述：
 */

public class GLUtils {
    private static final String TAG = "GLUtils";
    public static String loadShaderResource(Context context, int resId) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = context.getResources().openRawResource(resId);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return stringBuilder.toString();
    }

    public static int compileShader(int type, String shaderSource) {
        int shaderId = glCreateShader(type);
        if (shaderId == 0) {
            Log.e(TAG, "compileShader: fail to create shader");
            return 0;
        }
        glShaderSource(shaderId, shaderSource);
        glCompileShader(shaderId);
        final int[] result = new int[1];
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, result, 0);
        Log.d(TAG, shaderSource + "\n compile result " + glGetShaderInfoLog(shaderId));
        if (result[0] == 0) {
            glDeleteShader(shaderId);
            Log.e(TAG, "compileShader: fail to compile");
            return 0;
        }
        return shaderId;
    }

    public static int createProgram(int vertexId, int fragmentId) {
        int program = glCreateProgram();
        if (program == 0) {
            Log.e(TAG, "createProgram: fail to create program");
            return 0;
        }

        glAttachShader(program, vertexId);
        glAttachShader(program, fragmentId);
        glLinkProgram(program);
        final int[] result = new int[1];
        glGetProgramiv(program, GL_LINK_STATUS, result, 0);
        Log.d(TAG, "link program result " + glGetProgramInfoLog(program));
        if (result[0] == 0) {
            glDeleteProgram(program);
            Log.e(TAG, "createProgram: fail to link program");
            return 0;
        }
        return program;
    }
}
