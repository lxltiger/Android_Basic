package com.lxl.tiger.designpattern.visitor;

public class Visitor extends AbstractVisitor {
    @Override
    public void visit(ElementA elementA) {
        System.out.println(elementA.operateA());
    }

    @Override
    public void visit(ElementB elementB) {
        System.out.println(elementB.operateB());
    }
}
