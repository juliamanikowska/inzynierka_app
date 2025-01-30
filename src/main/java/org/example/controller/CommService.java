package org.example.controller;

import com.fazecast.jSerialComm.SerialPort;
import org.example.model.Command;
import org.example.model.Status;

public class CommService {
    private SerialPort serialPort;

    public void initializePort(String portName) {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
        }

        serialPort = SerialPort.getCommPort(portName);
        serialPort.setBaudRate(9600);

        if (!serialPort.openPort()) {
            throw new IllegalStateException("Nie można otworzyć portu: " + portName);
        } else {
            System.out.println("Połączono z portem: " + portName);
        }
    }

    public void sendMessage(Command command) {
        String message = command.toProtocolMessage();
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.writeBytes(message.getBytes(), message.length());
        } else {
            System.out.println("Port nie jest otwarty. Nie można wysłać wiadomości.");
        }
    }

    public Status readStatus() {
        if (serialPort == null || !serialPort.isOpen()) {
            throw new IllegalStateException("Port is not open.");
        }

        byte[] buffer = new byte[1024];
        int bytesRead = serialPort.readBytes(buffer, buffer.length);
        if (bytesRead > 0) {
            String rawData = new String(buffer, 0, bytesRead).trim();
            return new Status(rawData);
        }

        return null;
    }

    public void closePort() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("Port został zamknięty.");
        }
    }
}
