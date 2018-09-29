package com.lxl.tiger.designpattern.builder;

import java.util.EnumSet;
import java.util.Set;

public abstract class Pizza {
    public enum  Topping{ HAM, MUSHROOM, ONION, PEPPER, SAUSAGE }
     final Set<Topping> toppings;

    public static abstract class Builder<T extends Builder<T>>{
        private final EnumSet<Topping> toppings = EnumSet.noneOf(Topping.class);

        public T add(Topping topping) {
            toppings.add(topping);
            return self();
        }

       protected abstract T self();

        public abstract Pizza build() ;

    }

    public Pizza(Builder<?> builder) {
        this.toppings = builder.toppings.clone();
    }
}
