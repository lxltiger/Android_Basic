package com.example.stream;

import java.awt.Point;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.ToIntBiFunction;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;



/**
 * 流：从支持数据处理操作的源生成的元素序列，侧重于计算
 * 流的流水线背后的理念类似于构建器模式,filter、map等中间操作不会形成计算
 * filter和map等操作是无状态的，它们并不存储任何状态。
 * sorted和distinct等操作也要存储状态，因为它们需要把流中的所有元素缓存起来才能返回一个新的流。这种操作称为有状态操作
 *
 * reduce等操作要存储状态才能计算出一个值，它是一个不可变的归约
 * 是collect方法特别适合表达可变容器上的归约的原因，更关键的是它适合并行操作，
 */
public class Client {

    public static void main(String[] args) {
        List<Dish> dishes = Dish.testList();
        Client client = new Client();
//        client.demo1(dishes);
//        client.demo2();
//        client.demo3(dishes);

//        client.demo4();
//        client.demo5(dishes);
//        client.demo6();
        client.demo7(dishes);
    }


    private void demo1(List<Dish> dishes) {
        List<String> dishName = dishes.stream()
                .filter(dish -> {
                            System.out.println("filter "+dish.getName());
                            return dish.getCalories() < 600;})
//                .sorted(Comparator.comparing(Dish::getName))
                .map(dish -> {
                    System.out.println("map "+dish.getName());
                    return dish.getName();
                }).collect(Collectors.toList());
//        System.out.println(dishName);
    }

    /**
     * flatMap :element->stream
     */
    private void demo2() {
        List<Integer> first = Arrays.asList(1, 2, 3);
        List<Integer> second = Arrays.asList(3, 4);

        List<String> collect = first
                .stream()
                .flatMap(first1 ->second.stream().map(second1 -> String.format("%s,%s", first1, second1)))
                .collect(Collectors.toList());
        System.out.println(collect.toString());
    }

    private void demo3(List<Dish> dishes) {
        boolean anyMatch = dishes.stream().anyMatch(Dish::isVegetarian);
        boolean lowCalories = dishes.stream().allMatch(dish -> dish.getCalories() < 1000);

        Optional<Dish> optional = dishes.stream().filter(Dish::isVegetarian).findAny();
        optional.ifPresent(System.out::println);
    }

    /**
     * reduce 累加器应用及原理
     */
    private void demo4() {
        List<Integer> origin = Arrays.asList(1, 2, 3,54,34,23,34);
        //累加器
        Optional<Integer> reduce = origin.stream().reduce(Integer::sum);
        reduce.ifPresent(System.out::print);

        //原理
        BinaryOperator<Integer> fun=(a,b)->a+b;
        int sum=0;
        for (Integer integer : origin) {
            sum= fun.apply(sum, integer);
        }

    }

    //原始类型流特化
    private void demo5(List<Dish> dishes) {
        int sum = dishes.stream().mapToInt(Dish::getCalories).sum();
        System.out.println("sum" + sum);

        OptionalInt max = dishes.stream().mapToInt(Dish::getCalories).max();
        max.ifPresent(value->{
            System.out.println("max" + value);
        });
    }

    /**
     *无限流
     */
    private void demo6() {
//        斐波纳契序列，使用Point表示元祖
        Stream.iterate(new Point(0,1), old -> new Point(old.y,old.x+old.y))
                .limit(20)
                .mapToInt(point -> point.x)
                .forEach(System.out::println);

        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);
    }

    private void demo7(List<Dish> dishes) {
        Optional<Dish> dish = dishes.stream().collect(Collectors.maxBy(Comparator.comparingInt(Dish::getCalories)));
        dish.ifPresent(System.out::println);
    }
}
