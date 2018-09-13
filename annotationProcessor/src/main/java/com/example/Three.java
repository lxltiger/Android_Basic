package com.example;

import java.rmi.Remote;

@Factory(type = Meal.class,id = "three")
    public class Three  implements Meal {
        @Override
        public float getPrice() {
            return 3.2f;
        }
    }