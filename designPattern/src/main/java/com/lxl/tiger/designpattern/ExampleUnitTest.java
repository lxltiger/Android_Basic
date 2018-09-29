package com.lxl.tiger.designpattern;

import com.lxl.tiger.designpattern.adapter.Duck;
import com.lxl.tiger.designpattern.adapter.FireTurkey;
import com.lxl.tiger.designpattern.adapter.TurkeyAdapter;
import com.lxl.tiger.designpattern.command.Command;
import com.lxl.tiger.designpattern.command.GarageDoorDownCommand;
import com.lxl.tiger.designpattern.command.GarageDoorUpCommand;
import com.lxl.tiger.designpattern.command.GrabgeDoor;
import com.lxl.tiger.designpattern.command.Light;
import com.lxl.tiger.designpattern.command.LightOffCommand;
import com.lxl.tiger.designpattern.command.LightOnCommand;
import com.lxl.tiger.designpattern.command.MarcoCommand;
import com.lxl.tiger.designpattern.command.RemoteControl;
import com.lxl.tiger.designpattern.composite.Menu;
import com.lxl.tiger.designpattern.composite.MenuComponent;
import com.lxl.tiger.designpattern.composite.MenuItem;
import com.lxl.tiger.designpattern.decorator.Beverage;
import com.lxl.tiger.designpattern.decorator.DarkRoast;
import com.lxl.tiger.designpattern.decorator.Espresso;
import com.lxl.tiger.designpattern.decorator.Mocha;
import com.lxl.tiger.designpattern.decorator.Whip;
import com.lxl.tiger.designpattern.factory.ChicagoPisaStore;
import com.lxl.tiger.designpattern.factory.NYPizzaStore;
import com.lxl.tiger.designpattern.factory.PisaStore;
import com.lxl.tiger.designpattern.state.GumballMachine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    public void testPisa() {
        PisaStore chicagoPisaStore = new ChicagoPisaStore();
        PisaStore nyPizzaStore = new NYPizzaStore();

        chicagoPisaStore.orderPisa("cheese");
        nyPizzaStore.orderPisa("cheese");

    }

    public void testCoffee() {
        Beverage beverage = new Espresso();
        System.out.format("%s:$%S", beverage.getDescription(), beverage.cost());

        Beverage beverage2 = new DarkRoast();
        beverage2 = new Mocha(beverage2);
        beverage2 = new Whip(beverage2);
        System.out.format("%s:$%S", beverage2.getDescription(), beverage2.cost());

    }

    public void testCommand() {
        RemoteControl remoteControl = new RemoteControl();
        Light light = new Light();
        LightOnCommand lightOnCommand = new LightOnCommand(light);
        LightOffCommand lightOffCommand = new LightOffCommand(light);
        remoteControl.setCommand(lightOnCommand, lightOffCommand, 0);
        remoteControl.buttonOnPressed(0);


        GrabgeDoor grabgeDoor = new GrabgeDoor();
        GarageDoorUpCommand upCommand = new GarageDoorUpCommand(grabgeDoor);
        GarageDoorDownCommand downCommand = new GarageDoorDownCommand(grabgeDoor);

        Command[] on = {lightOnCommand, upCommand};
        Command[] off = {lightOffCommand, downCommand};
        MarcoCommand marcoOnCommand = new MarcoCommand(on);
        MarcoCommand marcoOffCommand = new MarcoCommand(off);

        remoteControl.setCommand(marcoOnCommand, marcoOffCommand, 1);

        remoteControl.buttonOnPressed(1);
        remoteControl.buttonOFFPressed(1);
    }


    public void testAdapter() {
        Duck duck = new TurkeyAdapter(new FireTurkey());
        duck.swim();
        duck.fly();
    }

    private MenuComponent populate() {
        //        总目录
        MenuComponent allMenus = new Menu("ALL MENUS", "All menus combined");

        MenuComponent pancakeHouseMenu =
                new Menu("PANCAKE HOUSE MENU", "Breakfast");

        MenuComponent dinerMenu =
                new Menu("DINER MENU", "Lunch");
        dinerMenu.add(new MenuItem(
                "Pasta",
                "Spaghetti with Marinara Sauce, and a slice of sourdough bread",
                3.89));
        //二级目录
        MenuComponent dessertMenu =
                new Menu("DESSERT MENU", "Dessert of course!");
        dessertMenu.add(new MenuItem(
                "Apple Pie",
                "Apple pie with a flakey crust, topped with vanilla icecream",
                1.59));
        dessertMenu.add(new MenuItem(
                "Orange Pie",
                "Orange pie with a flakey crust, topped with vanilla icecream",
                1.69));
        // 甜点为晚餐的子目录
        dinerMenu.add(dessertMenu);

        MenuComponent cafeMenu =
                new Menu("CAFE MENU", "Dinner");


        allMenus.add(pancakeHouseMenu);
        allMenus.add(dinerMenu);
        allMenus.add(cafeMenu);

        return allMenus;
    }
    public void testComposite() {
        MenuComponent topMenu = populate();
        topMenu.print();

    }

    public void testIterator() {

        MenuComponent topMenu = populate();
        Iterator<MenuComponent> iterator = topMenu.createIterator();
        while (iterator.hasNext()) {
            MenuComponent component = iterator.next();
            System.out.println(component.getName()+"---"+component.getDescription());
        }

    }

    public void testState() {
        GumballMachine gumballMachine = new GumballMachine(5);
        System.out.println(gumballMachine);

        gumballMachine.insertQuarter();
        gumballMachine.turnCrank();
        System.out.println(gumballMachine);

        gumballMachine.insertQuarter();
        gumballMachine.turnCrank();
        gumballMachine.insertQuarter();
        gumballMachine.turnCrank();
        System.out.println(gumballMachine);



    }


}