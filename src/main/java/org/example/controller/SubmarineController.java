package org.example.controller;

import org.example.model.Command;

public class SubmarineController {
    private final CommService communicationService;

    public SubmarineController() {
        this.communicationService = new CommService();
    }

    public void sendCommand(Command command) {
        String message = command.toProtocolMessage();
        communicationService.sendMessage(message);
    }

    public String receiveStatus() {
        return communicationService.readResponse();
    }

    public void initializePort(String portName) {
        communicationService.initializePort(portName);
    }

    public void close() {
        communicationService.closePort();
    }
}
