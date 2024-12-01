package org.example.controller;

import com.fazecast.jSerialComm.SerialPort;

public class CommService {
    private final SerialPort serialPort;

    public CommService(String portName) {
        serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(9600);
        if (!serialPort.openPort()) {
            throw new IllegalStateException("Nie można otworzyć portu: " + portName);
        }
    }

    public void sendMessage(String message) {
        serialPort.writeBytes(message.getBytes(), message.length());
    }

    public String readResponse() {
        byte[] buffer = new byte[1024];
        int bytesRead = serialPort.readBytes(buffer, buffer.length);
        if (bytesRead > 0) {
            return new String(buffer, 0, bytesRead).trim();
        } else {
            return "";
        }
    }

    public void close() {
        serialPort.closePort();
    }
}