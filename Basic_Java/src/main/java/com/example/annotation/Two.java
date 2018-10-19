package com.example.annotation;

@Factory(type = Meal.class,id = "two")
    public class Two implements Meal {
        @Override
        public float getPrice() {
            return 2.2f;
        }
    }