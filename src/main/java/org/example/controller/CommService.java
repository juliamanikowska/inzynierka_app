package org.example.controller;

import com.fazecast.jSerialComm.SerialPort;

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

    public void sendMessage(String message) {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.writeBytes(message.getBytes(), message.length());
        } else {
            System.out.println("Port nie jest otwarty. Nie można wysłać wiadomości.");
        }
    }

    public String readResponse() {
        if (serialPort == null || !serialPort.isOpen()) {
            throw new IllegalStateException("Port nie jest otwarty.");
        }

        byte[] buffer = new byte[1024];
        int bytesRead = serialPort.readBytes(buffer, buffer.length);
        return (bytesRead > 0) ? new String(buffer, 0, bytesRead).trim() : "";
    }

    public void closePort() {
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            System.out.println("Port został zamknięty.");
        }
    }
}
