package com.lxl.tiger.designpattern;

import java.awt.Point;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class Effective {


    public static void main(String[] args) throws FileNotFoundException {

        Point[] points = new Point[]{
                new Point(1, 2), new Point(2, 3)};
        //谨慎使用clone，最好提供静态工厂Copy方法
        Point[] ck = points.clone();
        ck[0].setLocation(10, 10);
        System.out.println(Arrays.toString(points));
    }


    /**
     * 避免使用try finally  容易掩盖错误的真相
     * Java 7以后使用try（resource）resource必须实现 AutoCloseable，这样能正确关闭资源
     * 如果抛出异常，你能看到异常的源头，
     *
     * @param src
     * @param dst
     * @throws IOException
     */
    static void copy(String src, String dst) throws IOException {
        try (InputStream in = new FileInputStream(src); OutputStream out = new FileOutputStream(dst)) {
            byte[] buf = new byte[20];
            int n;
            while ((n = in.read(buf)) >= 0)
                out.write(buf, 0, n);
        }
    }


}
