package com.lxl.tiger.designpattern.visitor;

public class Client {
    public static void main(String[] args) {
        ObjectStructure objectStructure = new ObjectStructure();

        objectStructure.add(new ElementA());
        objectStructure.add(new ElementB());

        objectStructure.action(new Visitor());
    }
}
