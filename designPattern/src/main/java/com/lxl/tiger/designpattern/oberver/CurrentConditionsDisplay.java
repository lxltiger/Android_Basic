package com.lxl.tiger.designpattern.oberver;

public class CurrentConditionsDisplay implements Observer ,DisplayElement{

    Subject subject;
    Data data;
    public CurrentConditionsDisplay(Subject subject) {
        this.subject = subject;
        subject.register(this);
    }

    @Override
    public void display() {
        System.out.println( data.toString());
    }

    @Override
    public void update(Data data) {
        this.data = data;
        display();
    }
}
