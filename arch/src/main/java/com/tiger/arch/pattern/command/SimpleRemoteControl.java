package com.tiger.arch.pattern.command;

public class SimpleRemoteControl {
    Command slot;


    public void setSlot(Command slot) {
        this.slot = slot;
    }

    public void  buttonWasPressed() {
        slot.execute();
    }
}
