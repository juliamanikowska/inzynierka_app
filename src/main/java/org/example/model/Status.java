package org.example.model;

import lombok.Getter;

@Getter
public class Status {
    private String shipName;
    private double pressure, depth, temperature;
    private double latitude, longitude;
    private int pitch, roll, yaw, battery, engine, heading;

    public Status(String rawData) {
        parseData(rawData);
    }

    private void parseData(String rawData) {
        String[] lines = rawData.split("\n");

        if (lines.length < 6) {
            throw new IllegalArgumentException("Invalid data format");
        }

        shipName = lines[0].trim();

        String[] presDepthTemp = lines[1].split(" ");
        pressure = Double.parseDouble(presDepthTemp[1]);
        depth = Double.parseDouble(presDepthTemp[3]);
        temperature = Double.parseDouble(presDepthTemp[5]);

        String[] latLon = lines[2].split(" ");
        latitude = Double.parseDouble(latLon[1]);
        longitude = Double.parseDouble(latLon[3]);

        String[] pitchRollYaw = lines[3].split(" ");
        pitch = Integer.parseInt(pitchRollYaw[1]);
        roll = Integer.parseInt(pitchRollYaw[3]);
        yaw = Integer.parseInt(pitchRollYaw[5]);

        battery = Integer.parseInt(lines[4].split(" ")[1]);
        String[] engineHeading = lines[5].split(" ");
        engine = Integer.parseInt(engineHeading[1]);
        heading = Integer.parseInt(engineHeading[3]);
    }

}
