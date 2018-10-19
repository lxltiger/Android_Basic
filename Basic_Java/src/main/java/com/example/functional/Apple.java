package com.example.functional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Apple  {
    private final int weight;
     private final String color;

     Apple(int weight, String color) {
        this.weight = weight;
        this.color = color;
    }


    public static Apple of(int weight, String color) {
        return new Apple(weight,color);
    }


    public static Apple red(int weight) {
        return new Apple(weight,"red");
    }



    public static List<Apple> testList() {
        List<Apple> apples = new ArrayList<>();
        apples.add(of(12, "green"));
        apples.add(of(6, "green"));
        apples.add(of(18, "green"));
        apples.add(of(15, "red"));
        return apples;
    }

    public int getWeight() {
        return weight;
    }

    public String getColor() {
        return color;
    }



    @Override
    public String toString() {
        return "Apple{" +
                "weight=" + weight +
                ", color='" + color + '\'' +
                '}';
    }



}
