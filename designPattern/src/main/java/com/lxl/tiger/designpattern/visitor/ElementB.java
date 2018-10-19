package com.lxl.tiger.designpattern.visitor;

public class ElementB implements Element {
    @Override
    public void accept(AbstractVisitor vistor) {
        vistor.visit(this);
    }

    public String operateB() {
        return ElementB.class.getSimpleName();
    }
}
