package com.lxl.tiger.designpattern.builder;

public class BuilderDriver {

    public static void main(String[] args) {
        NutritionFacts milk = new NutritionFacts.Builder(123, 21).sodium(23).build();

        NyPizza pizza=new NyPizza.Builder(NyPizza.Size.LARGE).add(Pizza.Topping.HAM).build();

        Calzone calzone=new Calzone.Builder().sauceInsize().add(Pizza.Topping.MUSHROOM).build();
    }
}
