package com.lxl.tiger.designpattern.command;

public class GarageDoorDownCommand implements Command {
    GrabgeDoor grabgeDoor;

    public GarageDoorDownCommand(GrabgeDoor grabgeDoor) {
        this.grabgeDoor = grabgeDoor;
    }

    @Override
    public void execute() {
        grabgeDoor.down();
    }

    @Override
    public void undo() {
        grabgeDoor.up();
    }
}
