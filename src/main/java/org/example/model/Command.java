package org.example.model;

public class Command {
    private final String type;
    private final String value;

    public Command(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String toProtocolMessage() {
        return String.format("%s %s\n", type, value);
    }
}
