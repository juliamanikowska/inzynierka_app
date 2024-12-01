package org.example.controller;

import org.example.model.Command;

public class SubmarineController {
    private final CommService communicationService;

    public SubmarineController(String portName) {
        this.communicationService = new CommService(portName);
    }

    public void sendCommand(Command command) {
        String message = command.toProtocolMessage();
        communicationService.sendMessage(message);
    }

    public String receiveStatus() {
        return communicationService.readResponse();
    }

    public void close() {
        communicationService.close();
    }
}
