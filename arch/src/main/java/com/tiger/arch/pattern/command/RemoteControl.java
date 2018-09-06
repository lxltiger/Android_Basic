package com.tiger.arch.pattern.command;

public class RemoteControl {
    Command[] commandsOn;
    Command[] commandsOff;
    Command undoCommand;
    public RemoteControl() {
        commandsOn = new Command[7];
        commandsOff = new Command[7];
        NOCommand noCommand=new NOCommand();
        for (int i = 0; i < 7; i++) {
            commandsOn[i] = noCommand;
            commandsOff[i] = noCommand;
        }
        undoCommand = noCommand;
    }

    public void setCommand(Command commandOn,Command commandOff, int slot) {
        commandsOn[slot] = commandOn;
        commandsOff[slot] = commandOff;
    }

    public void buttonOnPressed(int slot) {
        if (commandsOn[slot] != null) {
            commandsOn[slot].execute();
            undoCommand = commandsOn[slot];
        }
    }

    public void buttonOFFPressed(int slot) {
        if (commandsOff[slot] != null) {
            commandsOff[slot].execute();
            undoCommand = commandsOff[slot];
        }
    }

    public void buttonUndoPressed() {
        undoCommand.undo();
    }
}
