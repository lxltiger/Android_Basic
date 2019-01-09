package com.example.stream;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.example.stream.Student.of;


/**
 * Lambda表达式允许你直接以内联的形式为函数式接口的抽象方法提供实现，并把整个表达式作为函数式接口的实例,具体说来，是函数式接口一个具体实现
 * 的实例.函数式接口的抽象方法的签名基本上就是Lambda表达式的签名
 *
 *
 * 流：从支持数据处理操作的源生成的元素序列，侧重于计算
 * 流的流水线背后的理念类似于构建器模式,filter、map等中间操作不会形成计算
 * filter和map等操作是无状态的，它们并不存储任何状态。
 * sorted和distinct等操作也要存储状态，因为它们需要把流中的所有元素缓存起来才能返回一个新的流。这种操作称为有状态操作
 * <p>
 * reduce等操作要存储状态才能计算出一个值，它是一个不可变的归约
 * collect方法特别适合表达可变容器上的归约的原因，更关键的是它适合并行操作，
 * <p>
 * spliterator: splitable iterator 可分割迭代器
 * 用于对数组、集合、IO Channel、generator function 这样的数据源进行遍历和分区，
 * 并行访问：tryAdvance，串行访问foreachremaining
 * 使用Characteristics标记源的特征，如：Collection-》Sized，Set-》Distinct
 */
public class Client {

    public static void main(String[] args) {
        List<Dish> dishes = Dish.testList();
        Client client = new Client();
//        client.demo1(dishes);
        client.demo0();
//        client.demo3(dishes);

//        client.demo4();
//        client.demo5(dishes);
//        client.demo6();
//        client.demo7(dishes);
//        client.demo0();

    }

    private void demo0() {

        List<Student> students = Arrays.asList(of("Tom", 12),
                of("Jack", 13),
                of("Bill", 13),
                of("Jane", 11));

        Map< Integer,String> result = students.stream().collect(Collectors.toMap(Student::getAge, Student::getName,(s1,s2)->s1+s2));
        System.out.println(result);

    }


    private void collect(Supplier<List<String>> supplier, BiConsumer<List<String>, String> biConsumer) {
        List<String> collector = supplier.get();
        biConsumer.accept(collector, "one");
        biConsumer.accept(collector, "two");
        System.out.println(collector);
    }

    /**
     */
    private void demo1(List<Dish> dishes) {

        List<String> dishName = dishes.stream()
                .filter(dish -> {
                    System.out.println("filter " + dish.getName());
                    return dish.getCalories() < 600;
                })
//                .sorted(Comparator.comparing(Dish::getName))
                .map(dish -> {
                    System.out.println("map " + dish.getName());
                    return dish.getName();
                }).collect(Collectors.toList());
//        在本函数的上下文，Collectors.toList()主要生成以下三个函数来初始化Collector
//        提供可变的结果容器
        Supplier<ArrayList> supplier_list = ArrayList::new;
        Supplier<HashSet> supplier_hash = HashSet::new;
        Supplier<StringBuilder> supplier_stringBuilder = StringBuilder::new;
//        收集一个值到容器中
        BiConsumer<List<String>, String> biConsumer = List::add;
        BiFunction<List<String>, String, Boolean> listEBooleanBiFunction = List::add;
        BiConsumer<StringBuilder, Integer> biConsumer1 = StringBuilder::deleteCharAt;
        BiConsumer<HashSet<String>, String> biConsumer2 = HashSet::remove;
        //合并两个部分 未使用到
        BinaryOperator<List<String>> binaryOperator = (s, s2) -> {
            s.addAll(s2);
            return s;
        };

    }


    /**
     * flatMap :element->stream
     */
    private void demo2() {
        List<String> first= Arrays.asList("one", "two", "three", "four");
        List<String> second= Arrays.asList("A", "B", "C", "D");
//        f -> second.stream().map(s -> String.format("%s,%s ", f, s))
        first.stream()
                .flatMap(new Function<String, Stream<String>>() {
                    @Override
                    public Stream<String> apply(String f) {
                        return second.stream().map(new Function<String, String>() {
                            @Override
                            public String apply(String s) {
                                return String.format("%s,%s ", f, s);
                            }
                        });
                    }
                })
                .forEach(System.out::println);
//
//        Stream.of(first,second).flatMap(Collection::stream).forEach(System.out::print);




    }

    private void demo3(List<Dish> dishes) {
        List<String> names= Arrays.asList("one", "two", "three", "four");
        Optional<String> optionalFirst = names.stream().findFirst();
//        optional.ifPresent(System.out::println);


    }

    /**
     * reduce 累加器应用及原理
     */
    private void demo4() {
        List<Integer> origin = Arrays.asList(1, 2, 3, 54, 34, 23, 34);
        //累加器
//        Optional<Integer> reduce = origin.stream().reduce(Integer::sum);
        OptionalInt reduce = IntStream.of(1, 2, 3, 54, 34, 23, 34).reduce(Integer::sum);
        reduce.ifPresent(System.out::print);

        //原理
        BinaryOperator<Integer> fun = (a, b) -> a + b;
        int sum = 0;
        for (Integer integer : origin) {
            sum = fun.apply(sum, integer);
        }

    }

    //原始类型流特化
    private void demo5(List<Dish> dishes) {
        int sum = dishes.stream().mapToInt(Dish::getCalories).sum();
        System.out.println("sum" + sum);

        OptionalInt max = dishes.stream().mapToInt(Dish::getCalories).max();
        max.ifPresent(value -> {
            System.out.println("max" + value);
        });


    }

    /**
     * 无限流
     */
    private void demo6() {
//        斐波纳契序列，使用Point表示元祖
        Stream.iterate(new Point(0, 1), old -> new Point(old.y, old.x + old.y))
                .limit(20)
                .mapToInt(point -> point.x)
                .forEach(System.out::println);

        Stream.generate(Math::random)
                .limit(5)
                .forEach(System.out::println);
    }

    private void demo7(List<Dish> dishes) {
        Optional<Dish> dish = dishes.stream().max(Comparator.comparingInt(Dish::getCalories));
        dish.ifPresent(System.out::println);
    }
}
