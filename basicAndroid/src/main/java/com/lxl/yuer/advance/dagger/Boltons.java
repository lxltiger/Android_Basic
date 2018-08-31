package com.lxl.yuer.advance.dagger;

import javax.inject.Inject;

public class Boltons implements House {

    @Inject
    public Boltons(){
    }
    @Override
    public void prepareForWar() {
        //do something
        System.out.println(this.getClass().getSimpleName()+" prepared for war");
    }

    @Override
    public void reportForWar() {
        //do something
        System.out.println(this.getClass().getSimpleName()+" reporting..");
    }
}