package com.tiger.arch;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testCollection() {
        List<Number> list = new ArrayList<>();
        List<Integer> one = Arrays.asList(1, 3, 4);
        List<Double> two = Arrays.asList(1.0, 3.0, 4.0);
        //addAll的参数是Collection（? extends T）
        // List<Integer>和List<Double>都是Collection（? extends Number）的子类型
        list.addAll(one);
        list.addAll(two);

        //List<Integer> is a subtype of List<? extends Number>),
        List<? extends Number> numbers = one;
        //numbers.add(3); 不能添加，因为不能确定元素类型
        Number number = numbers.get(0);//只能获取
        System.out.println(list.toString());

        List<Object> objs = new ArrayList<>();
        objs.add(1);
        objs.add("two");
        List<? super Integer> ints = objs;
        ints.add(3);

    }


    @Test
    public void testArray() {
        Integer[] integers=new Integer[]{1,2,3};
        //如果Integer是Number的子类型，那么Integer[]也是Number[]的子类型
        Number[] numbers=integers;
        numbers[2]=23.23f;//编译正常，运行错误
        System.out.println(Arrays.toString(numbers));



    }
    static int numElementsInCommon(Set<?> s1, Set<?> s2) {
        int result = 0;
        for (Object o1 : s1)
            if (s2.contains(o1))
                result++;
        return result;
    }
}