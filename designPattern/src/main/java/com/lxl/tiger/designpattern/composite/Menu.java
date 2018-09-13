package com.lxl.tiger.designpattern.composite;

import java.util.ArrayList;
import java.util.Iterator;

public class Menu extends MenuComponent {

    ArrayList<MenuComponent> components = new ArrayList<>();
    String name;
    String description;
    Iterator<MenuComponent> iterator;
    public Menu(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }


    @Override
    public void add(MenuComponent component) {
       components.add(component);
    }

    @Override
    public void remove(MenuComponent component) {
        components.remove(component);
    }

    @Override
    public MenuComponent getChild(int i) {
        return components.get(i);
    }

    //当目录含有子目录时，会被重复调用加入栈中，防止子目录元素被遍历二次，只创建一个实例
    @Override
    public Iterator<MenuComponent> createIterator() {
        if (iterator == null) {
           iterator= new RecursionCompositeIterator(components.iterator());
        }
        return iterator;
    }

    @Override
    public void print() {
        System.out.println(getName()+"\t"+getDescription());
        for (MenuComponent next : components) {
            next.print();
        }
    }
}
