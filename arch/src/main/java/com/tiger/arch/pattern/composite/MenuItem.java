package com.tiger.arch.pattern.composite;

import java.util.Iterator;

public class MenuItem extends MenuComponent {

    String name;
    String description;
    double price;

    public MenuItem(String name,
                    String description,
                    double price) {
        this.name = name;
        this.description = description;
        this.price = price;

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "\tMenuItem{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public Iterator<MenuComponent> createIterator() {
        return new NullIterator();
    }

    @Override
    public void print() {
        System.out.println(toString());
    }
}
