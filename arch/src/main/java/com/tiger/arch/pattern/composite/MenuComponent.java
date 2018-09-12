package com.tiger.arch.pattern.composite;

import java.util.Iterator;

/**
 * 既可以作为Node，也可以作为leaves
 */
public abstract class MenuComponent {
    public void add(MenuComponent component) {
        throw new UnsupportedOperationException();

    }

    public void remove(MenuComponent component) {
        throw new UnsupportedOperationException();

    }

    public MenuComponent getChild(int i) {
        throw new UnsupportedOperationException();
    }

    public String getName() {
        throw new UnsupportedOperationException();
    }

    public String getDescription() {
        throw new UnsupportedOperationException();
    }

    public double getPrice() {
        throw new UnsupportedOperationException();
    }

    public void print() {
        throw new UnsupportedOperationException();

    }

    public abstract Iterator<MenuComponent> createIterator();
}
