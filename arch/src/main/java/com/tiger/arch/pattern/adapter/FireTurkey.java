package com.tiger.arch.pattern.adapter;

public class FireTurkey implements Turkey {
    @Override
    public void swim() {
        System.out.println(FireTurkey.class.getSimpleName()+"  --swim");
    }

    @Override
    public void fly() {

        System.out.println(FireTurkey.class.getSimpleName()+"  --fly");

    }
}
