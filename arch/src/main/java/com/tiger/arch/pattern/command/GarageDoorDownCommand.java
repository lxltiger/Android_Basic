package com.tiger.arch.pattern.command;

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
