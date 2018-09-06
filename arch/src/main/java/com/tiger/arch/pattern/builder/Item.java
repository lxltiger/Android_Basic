package com.tiger.arch.pattern.builder;

public interface Item {
    String getName();

    float getPrice();

    Packing packing();
}
