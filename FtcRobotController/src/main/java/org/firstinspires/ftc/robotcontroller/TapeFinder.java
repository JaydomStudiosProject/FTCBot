package org.firstinspires.ftc.robotcontroller;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;

/**
 * Created by jtnunley on 12/16/16.
 */

public class TapeFinder {
    private LightSensor sensor;
    private static final String sensorName = "lightSensor";

    static final double WHITE_THRESHOLD = 0.2;  // spans between 0.1 - 0.5 from dark to light
    static final double APPROACH_SPEED = 0.5;

    public TapeFinder(HardwareMap hwMap) {
        sensor = hwMap.lightSensor.get(sensorName);
        DisableLed();
    }

    public void EnableLed() {
        sensor.enableLed(true);
    }

    public void DisableLed() {
        sensor.enableLed(false);
    }

    public double GetLightDetected() {
        return sensor.getLightDetected();
    }

    public boolean AtLine() {
        return !(GetLightDetected() < WHITE_THRESHOLD);
    }
}

