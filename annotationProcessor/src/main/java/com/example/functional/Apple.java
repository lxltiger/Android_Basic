package com.example.functional;

public class Apple {
    private final int weight;
     private final String color;

    private Apple(int weight, String color) {
        this.weight = weight;
        this.color = color;
    }

    public static Apple of(int weight, String color) {
        return new Apple(weight,color);
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
