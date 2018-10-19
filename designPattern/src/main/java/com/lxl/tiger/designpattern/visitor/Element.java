package com.lxl.tiger.designpattern.visitor;

public interface Element {
    void accept(AbstractVisitor vistor);
}
