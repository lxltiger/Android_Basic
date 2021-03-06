package com.lxl.tiger.designpattern.command;

public class GarageDoorUpCommand implements Command {
    GrabgeDoor grabgeDoor;

    public GarageDoorUpCommand(GrabgeDoor grabgeDoor) {
        this.grabgeDoor = grabgeDoor;
    }

    @Override
    public void execute() {
        grabgeDoor.up();
    }

    @Override
    public void undo() {
        grabgeDoor.down();
    }
}
