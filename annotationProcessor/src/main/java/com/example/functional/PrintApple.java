package com.example.functional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class PrintApple {

    public static void main(String[] args) {
        List<Apple> apples = new ArrayList<>();
        apples.add(Apple.of(12, "green"));
        apples.add(Apple.of(6, "green"));
        apples.add(Apple.of(18, "green"));
        apples.add(Apple.of(15, "red"));
        printApple(apples,apple -> String.format("%d g",apple.getWeight()));
        apples.sort(Comparator.comparingInt(Apple::getWeight));
        System.out.println(apples.toString());
        BiFunction<Integer, String, Apple> of = Apple::of;
        Apple grey = of.apply(12, "grey");


    }

    private static <T,R> void printApple(List<T> apples, Function<T,R> appleInterface) {
        for (T apple : apples) {
            R description = appleInterface.apply(apple);
            System.out.println(description);
        }
    }



}
