package org.firstinspires.ftc.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by jtnunley on 12/16/16.
 */

@TeleOp(name="Controlled - 2 Sticks", group="MBot")
public class MBotC1S extends MBotControlled {
    @Override
    protected two_ints func(double lsx, double lsy, double rsx, double rsy) {
        double left = -lsy + lsx;
        double right = -rsy - rsx;
        return new two_ints(left, right);
    }
}
