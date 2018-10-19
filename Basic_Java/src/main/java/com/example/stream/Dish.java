package com.example.stream;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    private final String name;
    private final boolean vegetarian;
    private final int calories;
    private final Type type;

    public enum Type {MEAT, FISH, OTHER}

    public Dish(String name, boolean vegetarian, int calories, Type type) {
        this.name = name;
        this.vegetarian = vegetarian;
        this.calories = calories;
        this.type = type;
    }


     static List<Dish> testList() {
        List<Dish> apples = new ArrayList<>();
        apples.add(new Dish("pork", false, 800, Dish.Type.MEAT));
        apples.add(new Dish("beef", false, 700, Dish.Type.MEAT));
        apples.add(new Dish("chicken", false, 400, Dish.Type.MEAT));
        apples.add(new Dish("french fries", true, 530, Dish.Type.OTHER));
        apples.add(new Dish("rice", true, 350, Dish.Type.OTHER));
        apples.add(new Dish("season fruit", true, 120, Dish.Type.OTHER));
        apples.add(new Dish("pizza", true, 550, Dish.Type.OTHER));

        return apples;
    }


    public String getName() {
        return name;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public int getCalories() {
        return calories;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "name='" + name + '\'' +
                ", vegetarian=" + vegetarian +
                ", calories=" + calories +
                ", type=" + type +
                '}';
    }
}
