package com.tiger.arch;

import com.tiger.arch.pattern.adapter.Duck;
import com.tiger.arch.pattern.adapter.FireTurkey;
import com.tiger.arch.pattern.adapter.TurkeyAdapter;
import com.tiger.arch.pattern.command.Command;
import com.tiger.arch.pattern.command.GarageDoorDownCommand;
import com.tiger.arch.pattern.command.GarageDoorUpCommand;
import com.tiger.arch.pattern.command.GrabgeDoor;
import com.tiger.arch.pattern.command.Light;
import com.tiger.arch.pattern.command.LightOffCommand;
import com.tiger.arch.pattern.command.LightOnCommand;
import com.tiger.arch.pattern.command.MarcoCommand;
import com.tiger.arch.pattern.command.RemoteControl;
import com.tiger.arch.pattern.composite.Menu;
import com.tiger.arch.pattern.composite.MenuComponent;
import com.tiger.arch.pattern.composite.MenuItem;
import com.tiger.arch.pattern.decorator.Beverage;
import com.tiger.arch.pattern.decorator.DarkRoast;
import com.tiger.arch.pattern.decorator.Espresso;
import com.tiger.arch.pattern.decorator.Mocha;
import com.tiger.arch.pattern.decorator.Whip;
import com.tiger.arch.pattern.factory.ChicagoPisaStore;
import com.tiger.arch.pattern.factory.NYPizzaStore;
import com.tiger.arch.pattern.factory.PisaStore;

import org.junit.Test;

import java.util.Iterator;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void testPisa() {
        PisaStore chicagoPisaStore = new ChicagoPisaStore();
        PisaStore nyPizzaStore = new NYPizzaStore();

        chicagoPisaStore.orderPisa("cheese");
        nyPizzaStore.orderPisa("cheese");

    }

    @Test
    public void testCoffee() {
        Beverage beverage = new Espresso();
        System.out.format("%s:$%S", beverage.getDescription(), beverage.cost());

        Beverage beverage2 = new DarkRoast();
        beverage2 = new Mocha(beverage2);
        beverage2 = new Whip(beverage2);
        System.out.format("%s:$%S", beverage2.getDescription(), beverage2.cost());

    }

    @Test
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


    @Test
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
    @Test
    public void testComposite() {
        MenuComponent topMenu = populate();
        topMenu.print();

    }

    @Test
    public void testIterator() {

        MenuComponent topMenu = populate();
        Iterator<MenuComponent> iterator = topMenu.createIterator();
        while (iterator.hasNext()) {
            MenuComponent component = iterator.next();
            System.out.println(component.getName()+"---"+component.getDescription());
        }

    }


}