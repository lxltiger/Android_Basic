package com.lxl.tiger.designpattern.builder;

public interface Item {
    String getName();

    float getPrice();

    Packing packing();
}
