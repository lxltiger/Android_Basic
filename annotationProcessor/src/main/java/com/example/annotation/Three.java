package com.example.annotation;

@Factory(type = Meal.class,id = "three")
    public class Three  implements Meal {
        @Override
        public float getPrice() {
            return 3.2f;
        }
    }