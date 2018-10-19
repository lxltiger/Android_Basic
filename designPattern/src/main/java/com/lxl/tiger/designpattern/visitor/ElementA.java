package com.lxl.tiger.designpattern.visitor;

public class ElementA implements Element {
    @Override
    public void accept(AbstractVisitor vistor) {
        vistor.visit(this);
    }

    public String operateA() {
        return ElementA.class.getSimpleName();
    }
}
