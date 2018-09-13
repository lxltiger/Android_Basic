package com.lxl.tiger.designpattern.decorator;

/**
 * The Decorator Pattern attaches additional
 responsibilities to an object dynamically.
 Decorators provide a fl exible alternative to
 subclassing for extending functionality
 */
public abstract class Beverage {
    String description = "Unknown Beverage";

    public String getDescription() {
        return description;
    }


    public abstract float cost();
}
