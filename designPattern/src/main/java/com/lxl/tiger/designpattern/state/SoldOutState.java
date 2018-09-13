package com.lxl.tiger.designpattern.state;

public class SoldOutState implements State {
    private GumballMachine gumballMachine;

    public SoldOutState(GumballMachine gumballMachine) {
        this.gumballMachine = gumballMachine;
    }

    @Override
    public void insertQuarter() {
        System.out.println("sorry sold out");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("sorry sold out");

    }

    @Override
    public void turnCrank() {
        System.out.println("sorry sold out");

    }

    @Override
    public void dispense() {
        System.out.println("sorry sold out");

    }
}
