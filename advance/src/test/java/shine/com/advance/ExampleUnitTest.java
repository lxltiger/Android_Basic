package shine.com.advance;

import org.junit.Test;

import java.util.Random;

import shine.com.advance.algor.MergeSort;
import shine.com.advance.algor.Quick3Way;
import shine.com.advance.algor.QuickSort;
import shine.com.advance.algor.ShellSort;
import shine.com.advance.algor.SortCollection;
import shine.com.advance.algor.Tools;

import static shine.com.advance.algor.Tools.isSorted;
import static shine.com.advance.algor.Tools.show;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testSelection() {
        Integer[] a = {-1, 1, 4, 2, 45, 234, 20, 43, 23};
        SortCollection.selectionSort(a);
        assert isSorted(a);
//        show(a);

    }

    @Test
    public void testInsertion() {
        Integer[] b = {13, 96, 71, 60, 177, 82, 188, 107, 9, 90};
        SortCollection.insertionSort(b);
        assert isSorted(b);
        show(b);
    }

    @Test
    public void testShellSort() {
        Integer[] c = {55, -1, 1, 4, 2, 45, 234, 20, 43, 23};
        ShellSort.sort(c);
        assert isSorted(c);
//        show(c);
    }

    @Test
    public void testMerge() {
        int N = 1000;
        Random random = new Random();
        Double[] d = new Double[N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                d[j] = random.nextDouble();
            }

        }
        MergeSort.TDSort(d);
        assert isSorted(d);
    }

    @Test
    public void testBUMerge() {
        int N = 999;
        Random random = new Random();
        Double[] d = new Double[N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                d[j] = random.nextDouble();
            }

        }
        MergeSort.BUSort(d);

    }

    @Test
    public void testQuickSort() {
        Integer[] c = {55, -1, 1, 4, 2, 45, 234, 20, 12, 8};
        int N = 9999;
        Random random = new Random();
        Double[] d = new Double[N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                d[j] = random.nextDouble();
            }
        }
        QuickSort.sort(d, 0, N - 1);
        assert isSorted(d);
    }

    @Test
    public void testQuick3WaySort() {
        Integer[] c = {1, 4, 2, 45, 234, 1, 12, 8};
        /*int N=9999;
        Random random = new Random();
        Double[] d = new Double[N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                d[j] = random.nextDouble();
            }
        }
        Quick3Way.selectionSort(d, 0, N - 1);*/
        Quick3Way.sort(c, 0, c.length - 1);
//        Tools.show(c);
        assert isSorted(c);
    }


    /*选择排序和插入排序差异不明显N＊N
     * 希尔排序明显优于前两者N＊LogN*/
    @Test
    public void testCompare() {
        long selection = Tools.compare("selection", 10000, 100);
        long insertion = Tools.compare("insertion", 10000, 100);
        long shell = Tools.compare("shell", 10000, 100);
        System.out.println(selection);
        System.out.println(insertion);
        System.out.println(shell);

    }


}