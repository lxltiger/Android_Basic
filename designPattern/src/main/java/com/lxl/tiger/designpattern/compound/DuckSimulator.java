package com.lxl.tiger.designpattern.compound;

public class DuckSimulator {

    public static void main(String[] args) {
        DuckSimulator duckSimulator = new DuckSimulator();
        AbstractDuckFactory duckFactory = new CountDuckFactory();
        duckSimulator.simulate(duckFactory);
    }


    void simulate(AbstractDuckFactory duckFactory) {
//        使用抽象工厂模式
        Quackable redheadDuck = duckFactory.createRedheadDuck();
        Quackable duckCall = duckFactory.createDuckCall();
        Quackable rubberDuck = duckFactory.createRubberDuck();
        Quackable gooseDuck = new GooseAdapter(new Goose());
        //使用Composite模式
        Flock flockOfDucks = new Flock();
        flockOfDucks.add(redheadDuck);
        flockOfDucks.add(duckCall);
        flockOfDucks.add(rubberDuck);
        flockOfDucks.add(gooseDuck);

        Flock flockOfMallards = new Flock();
        Quackable mallardOne = duckFactory.createMallardDuck();
        Quackable mallardTwo = duckFactory.createMallardDuck();
        Quackable mallardThree = duckFactory.createMallardDuck();
        Quackable mallardFour = duckFactory.createMallardDuck();
        flockOfMallards.add(mallardOne);
        flockOfMallards.add(mallardTwo);
        flockOfMallards.add(mallardThree);
        flockOfMallards.add(mallardFour);

        flockOfDucks.add(flockOfMallards);

//        观察者模式
        Quackologist quackologist = new Quackologist();
        flockOfDucks.register(quackologist);
        simulate(flockOfDucks);

        System.out.println("\nThe ducks quacked " +
                QuackCount.getCount() +
                " times");
    }

    void simulate(Quackable duck) {
        duck.quack();
    }
}
