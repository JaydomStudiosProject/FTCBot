package org.firstinspires.ftc.robotcontroller;

/**
 * Created by jtnunley on 12/16/16.
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drive {
    private DcMotor  lDrive   = null;
    private DcMotor  rDrive  = null;

    private static final String lDriveName = "lDrive";
    private static final String rDriveName = "rDrive";

    public Drive(HardwareMap hwMap) {

        lDrive = hwMap.dcMotor.get(lDriveName);
        rDrive = hwMap.dcMotor.get(rDriveName);

        lDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rDrive.setDirection(DcMotorSimple.Direction.FORWARD);

        setMotorPower(0);

        runMotorsWithoutEncoders();
    }

    // set motors to reverse
    public void reverseLeftMotor() { lDrive.setDirection(DcMotor.Direction.REVERSE);}
    public void reverseRightMotor() { rDrive.setDirection(DcMotor.Direction.REVERSE);}

    // set power for one/both motors
    public void setLeftMotorPower(double power) { lDrive.setPower(power);}
    public void setRightMotorPower(double power) { rDrive.setPower(power);}
    public void setMotorPower(double left, double right) { setLeftMotorPower(left); setRightMotorPower(right);}
    public void setMotorPower(double power) {



            setMotorPower(power, power);

    }


    // set both motors to run without encoders
    public void runLeftMotorWithoutEncoders() { lDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);}
    public void runRightMotorWithoutEncoders() { rDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);}
    public void runMotorsWithoutEncoders() { runLeftMotorWithoutEncoders();runRightMotorWithoutEncoders();}

    // reset one/both of motor's encoders
    public void resetLeftMotorEncoders() {lDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);}
    public void resetRightMotorEncoders() {rDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);}
    public void resetMotorEncoders() { resetLeftMotorEncoders();resetRightMotorEncoders();}

    // run one/both motors with encoders
    public void runLeftMotorWithEncoders() { lDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);}
    public void runRightMotorWithEncoders() { rDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);}
    public void runMotorsWithEncoders() { runLeftMotorWithEncoders();runRightMotorWithEncoders();}

    public int getLeftPosition() { return lDrive.getCurrentPosition();}
    public int getRightPosition() { return rDrive.getCurrentPosition();}

    public void setLeftTarget(int target) { lDrive.setTargetPosition(target);}
    public void setRightTarget(int target) { rDrive.setTargetPosition(target);}

    public void setLeftToRunToPosition() { lDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION); }
    public void setRightToRunToPosition() { rDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION); }
    public void setRunToPosition() { setLeftToRunToPosition();setRightToRunToPosition();}

    public boolean isLeftBusy() { return lDrive.isBusy(); }
    public boolean isRightBusy() { return rDrive.isBusy(); }
}
