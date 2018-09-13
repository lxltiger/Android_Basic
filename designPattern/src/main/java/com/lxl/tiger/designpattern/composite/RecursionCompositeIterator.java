package com.lxl.tiger.designpattern.composite;

import java.util.Iterator;
import java.util.Stack;

/**
 * 采用递归+栈的方式进行遍历
 */
public class RecursionCompositeIterator implements Iterator<MenuComponent> {
    private Stack<Iterator<MenuComponent>> stack=new Stack<>();

//    顶级目录的iterator
    public RecursionCompositeIterator(Iterator<MenuComponent> iterator) {
        stack.push(iterator);
    }

    @Override
    public boolean hasNext() {
        if (stack.empty()) {
            return false;
        }else {
//            查看栈顶的迭代器判断是否还有元素，不要取出
            Iterator<MenuComponent> iterator = stack.peek();
            if (iterator.hasNext()) {
                return true;
            }else {
//                如果栈顶迭代结束将其弹出，再次递归
                stack.pop();
                return hasNext();
            }
        }
    }

    @Override
    public MenuComponent next() {
        if (hasNext()) {
            Iterator<MenuComponent> iterator = stack.peek();
//            取出下一个元素，如果是目录 添加其迭代器到栈中
            MenuComponent component = iterator.next();
            if (component instanceof Menu) {
                stack.push(component.createIterator());
            }
            return component;
        }else {
            return null;
        }
    }
}
