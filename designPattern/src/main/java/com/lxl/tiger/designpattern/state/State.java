package com.lxl.tiger.designpattern.state;

/**
 * The State Pattern allows an object to alter its behavior
 when its internal state changes. The object will appear to
 change its class.
 */
public interface State {
    void insertQuarter();

    void ejectQuarter();

    void turnCrank();

    void dispense();
}
