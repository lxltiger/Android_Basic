package com.lxl.tiger.designpattern.compound;

import java.util.ArrayList;

public class Flock implements Quackable {
    ArrayList<Quackable> quackables = new ArrayList<>();

    public void add(Quackable quackable) {
        quackables.add(quackable);
    }
    @Override
    public void quack() {
        for (Quackable quackable : quackables) {
            quackable.quack();
        }
    }


      @Override
    public void register(Observer observer) {
          for (Quackable quackable : quackables) {
              quackable.register(observer);
          }
    }

    @Override
    public void notifyObserver() {
       /* for (Quackable quackable : quackables) {
            quackable.notifyObserver();
        }*/
    }
}
