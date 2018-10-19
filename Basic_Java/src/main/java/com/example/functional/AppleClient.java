package com.example.functional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 函数式接口：只有一个抽象方法
 * Lambda表达式允许你用内联的方式为函数式接口提供实现，并把整个表达式作为实例
 * lambda 可以访问外层作用域定义的变量，但必须是事实上的final，因为成员变量分配在堆上，局部变量在栈上，可能被回收
 *
 * @param <T>
 */
public class AppleClient<T> {

    public static void main(String[] args) {
        AppleClient<Apple> client = new AppleClient<>();
        List<Apple> list = Apple.testList();

        list.sort(Comparator.comparingInt(Apple::getWeight));
        client.printApple(list, apple -> String.format(Locale.CANADA, "%s %dg", apple.getColor(), apple.getWeight()));

        //筛选重量大于12
        Predicate<Apple> bigger = apple -> apple.getWeight() > 12;
        List<Apple> apples = client.filter(list, bigger);
        System.out.println(apples.toString());

//        复合谓词，筛选重量小于等于指定数值的苹果 除了negate表示非，还有and 和or
        Predicate<Apple> smaller = bigger.negate();
        List<Apple> smallApples = client.filter(list, smaller);
        System.out.println(smallApples.toString());


//        使用构造函数引用指向对应的函数式接口
        BiFunction<Integer, String, Apple> appleSupply = Apple::new;
        Apple grey = appleSupply.apply(12, "grey");

        //使用抽象方法构造函数式接口
        BiConsumer<List<Apple>, Apple> biConsumer = List::add;
        biConsumer.accept(list, grey);


        List<Integer> weights = Arrays.asList(12, 23, 34, 45);
        List<Apple> redApples = client.map(weights, Apple::red);
        System.out.println(redApples.toString());


    }



    /**
     * @param apples
     * @param appleInterface 有输入输出的函数
     * @param <T>
     * @param <R>
     */
    private <T, R> void printApple(List<T> apples, Function<T, R> appleInterface) {
        for (T apple : apples) {
            R description = appleInterface.apply(apple);
            System.out.println(description);
        }
    }

    /**
     * 对不同的筛选行为参数化，抽象筛选方法
     *
     * @param items
     * @param predicate 谓词，返回boolean值的一个函数
     * @return
     */
    private List<T> filter(List<T> items, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : items) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }


    private <I, T> List<T> map(List<I> weights, Function<I, T> function) {
        List<T> result = new ArrayList<>();
        for (I weight : weights) {
            T t = function.apply(weight);
            result.add(t);
        }
        return result;
    }


}
