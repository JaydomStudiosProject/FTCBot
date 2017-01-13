package org.firstinspires.ftc.robotcontroller;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LightSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by jtnunley on 12/16/16.
 */

public class TapeFinder {
    private OpticalDistanceSensor sensor;
    private static final String sensorName = "lightSensor";

    static final double WHITE_THRESHOLD = 0.5;  // spans between 0.1 - 0.5 from dark to light
    static final double APPROACH_SPEED = 0.5;

    public TapeFinder(HardwareMap hwMap) {
        sensor = hwMap.opticalDistanceSensor.get(sensorName);
        EnableLed();
    }

    public void DisableLed() { sensor.enableLed(false); }
    public void EnableLed() { sensor.enableLed(true); }

    public double GetLightDetected() { return sensor.getLightDetected(); }

    public boolean AtLine() { return !(GetLightDetected() < WHITE_THRESHOLD); }
}

