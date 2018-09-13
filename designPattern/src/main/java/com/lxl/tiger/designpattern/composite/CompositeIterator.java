package com.lxl.tiger.designpattern.composite;

import java.util.Iterator;
import java.util.Stack;

/**
 * 类似遍历文件夹，把一个文件夹下文件都平铺放到栈中,也可以放到队列
 */
public class CompositeIterator implements Iterator<MenuComponent> {
    private Stack<MenuComponent> stack=new Stack<>();

//    顶级目录的iterator
    public CompositeIterator(Iterator<MenuComponent> iterator) {
        flatComponent(iterator);
    }

    private void flatComponent(Iterator<MenuComponent> iterator) {
        while (iterator.hasNext()) {
            MenuComponent component = iterator.next();
            stack.push(component);
            if (component instanceof Menu) {
                flatComponent(component.createIterator());
            }
        }
    }



    @Override
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    @Override
    public MenuComponent next() {
        return stack.pop();
    }
}
