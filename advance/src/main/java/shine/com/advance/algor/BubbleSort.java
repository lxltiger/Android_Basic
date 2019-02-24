package shine.com.advance.algor;

import android.graphics.Point;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BubbleSort {




    public static Queue<Point> formSortCommand(int[] numbers) {
        int N = numbers.length;
        Queue<Point> queue = new LinkedList<>();
        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                queue.add(new Point(i,j));
            }
        }
        return queue;
    }





}
