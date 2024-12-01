package org.example.controller;

import org.example.model.Command;

public class CmdHandler {
    private final SubmarineController submarineController;

    public CmdHandler(SubmarineController submarineController) {
        this.submarineController = submarineController;
    }

    public void handleMove(String direction) {
        Command command = new Command("MOVE", direction);
        submarineController.sendCommand(command);
    }

    public void handleSetSpeed(int speed) {
        Command command = new Command("SET_SPEED", String.valueOf(speed));
        submarineController.sendCommand(command);
    }

    public void handleDive(boolean dive) {
        Command command = new Command("DIVE", dive ? "1" : "0");
        submarineController.sendCommand(command);
    }
}
