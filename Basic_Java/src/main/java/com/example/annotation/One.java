package com.example.annotation;

@Factory(type = One.class,id = "one")
    public class One implements Meal {
        @Override
        public float getPrice() {
            return 1.2f;
        }
    }