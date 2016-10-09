package org.firstinspires.ftc.robotcontroller;

import android.text.method.Touch;

import com.qualcomm.robotcore.hardware.AnalogInputController;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.io.PrintStream;

/**
 * Created by jtnunley on 9/23/16.
 */

public class MBotHardwareControl {
    /*
    The directions of the motors are dependent on the front.
    However, due to the names, they are interchangable. for
    example:

                   FRONT
      LDrive --> ||-----|| <-- RDrive

    If the front were to switch, RDrive would become LDrive,
    and vice versa.
    Servos are not direction dependent.
     */

    // left motor drive
    private DcMotor lDrive;

    // right motor drive
    private DcMotor rDrive;

    // servo - may be more later
    private Servo servo;

    // get the drive controller and servo controller as well; functions are useful
    private DcMotorController driveController;
    private ServoController servoController;

    // touch sensor allocation
    private TouchSensor touchSensor;
    private AnalogInputController analogInputController;

    // names of the drives; the advantage of doing so is so
    // it is easier to rename the drives at will
    private static final String lDriveName = "lDrive";
    private static final String rDriveName = "rDrive";
    private static final String servoName = "servo";
    private  static final String driveControllerName = "driveController";
    private static final String servoControllerName = "barry ";
    private  static final String touchSensorName = "touchSensor";

    // hardware map - may be in use later
    private HardwareMap hMap;
    private Telemetry te;
    // constructor
    public MBotHardwareControl(HardwareMap map, Telemetry t) {
        try {
            // set hardware map
            hMap = map;

            // assign hardware
            lDrive = hMap.dcMotor.get(lDriveName);
            rDrive = hMap.dcMotor.get(rDriveName);
            servo = hMap.servo.get(servoName);
            driveController = hMap.dcMotorController.get(driveControllerName);
            servoController = hMap.servoController.get(servoControllerName);
            //touchSensor = hMap.touchSensor.get(touchSensorName);

            String offender = "";
            // oh crud
            if (lDrive == null)
                offender += lDriveName;
            if (rDrive == null)
                offender += rDriveName;
            if (servo == null)
                offender += servoName;
            if (driveController == null)
                offender += driveControllerName;
            if (servoController == null)
                offender += servoControllerName;
            if (hMap == null)
                offender += "hMap";

            if (offender != "")
            {
                t.addData("off",offender);
                return;
            }

            // do this, so left motor won't run backwards
            reverseLeftMotor();

            // set power for both motors to zero
            setMotorPower(0);

            // make sure both motors are running without encoders
            runMotorsWithoutEncoders();

            //te = t;
        }
        catch (NullPointerException e)
        {
            err(t,e);
        }
        te = t;
    }

    public void err(Telemetry t, Exception e)
    {


        t.addData("msg",e.getMessage());
    }

    // set motors to reverse
    public void reverseLeftMotor() { lDrive.setDirection(DcMotor.Direction.REVERSE);}
    public void reverseRightMotor() { rDrive.setDirection(DcMotor.Direction.REVERSE);}

    // set power for one/both motors
    public void setLeftMotorPower(double power) { lDrive.setPower(power);}
    public void setRightMotorPower(double power) { rDrive.setPower(power);}
    public void setMotorPower(double left, double right) { setLeftMotorPower(left); setRightMotorPower(right);}
    public void setMotorPower(double power) {
        try {


            setMotorPower(power, power);
        }
        catch (NullPointerException e) {
            err(te, e);
        }
    }


    // set both motors to run without encoders
    public void runLeftMotorWithoutEncoders() { lDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);}
    public void runRightMotorWithoutEncoders() { rDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODERS);}
    public void runMotorsWithoutEncoders() { runLeftMotorWithoutEncoders();runRightMotorWithoutEncoders();}

    // reset one/both of motor's encoders
    public void resetLeftMotorEncoders() {lDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);}
    public void resetRightMotorEncoders() {rDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);}
    public void resetMotorEncoders() { resetLeftMotorEncoders();resetRightMotorEncoders();}

    // run one/both motors with encoders
    public void runLeftMotorWithEncoders() { lDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);}
    public void runRightMotorWithEncoders() { rDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODERS);}
    public void runMotorsWithEncoders() { runLeftMotorWithEncoders();runRightMotorWithoutEncoders();}


}
