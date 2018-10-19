package com.lxl.tiger.designpattern.visitor;

import java.util.ArrayList;
import java.util.List;

public class ObjectStructure {
    private List<Element> elements = new ArrayList<>();


    public void add(Element element) {
        elements.add(element);
    }

    public void action(Visitor visitor) {
        for (Element element : elements) {
            element.accept(visitor);
        }
    }
}
