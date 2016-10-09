package org.firstinspires.ftc.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by jtnunley on 9/29/16.
 */

public class MBotProgramMoveStr8 extends OpMode {
    private MBotHardwareControl hardware;

    public MBotProgramMoveStr8() {
        hardware = new MBotHardwareControl(this.hardwareMap, this.telemetry);

    }

    @Override
    public void init() {
    }

    @Override
    public void init_loop() {

        hardware.setMotorPower(20);
    }

    @Override
    public void loop() {

    }
}
