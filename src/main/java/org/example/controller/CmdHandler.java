package org.example.controller;

import org.example.model.Command;

public class CmdHandler {
    private final CommService commService;

    public CmdHandler(CommService commService) {
        this.commService = commService;
    }

    public void handleMove(String speed) {
        if(Integer.valueOf(speed) > 100 || Integer.valueOf(speed) < -100 ) {
            System.out.println("Invalid speed");
        }
        else {
            Command command = new Command("1", speed);
            commService.sendMessage(command);
        }
    }

    public void handleDirection(String direction) {
        if(Integer.valueOf(direction) > 40 || Integer.valueOf(direction) < -40 ) {
            System.out.println("Invalid turn angle");
        }
        else {
            Command command = new Command("2", direction);
            commService.sendMessage(command);
        }

    }

    public void handleDiveTime(String dive) {
        if(Integer.valueOf(dive) < 1) {
            System.out.println("Invalid diving duration time");
        }
        else {
            Command command = new Command("3", dive);
            commService.sendMessage(command);
        }
    }

    public void handleDepth(String depth) {
        Command command = new Command("4", depth);
        commService.sendMessage(command);
    }

    public void handlePump(String pump) {
        Command command = new Command("5", pump);
        commService.sendMessage(command);
    }
}
