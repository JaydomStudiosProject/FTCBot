package org.firstinspires.ftc.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by jtnunley on 11/10/16.
 */
/*
public class MBotAutonomousStable extends LinearOpMode {
    MBotHardwareControl hardware = new MBotHardwareControl();
    private ElapsedTime runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    static final String colorToFind = "red";

    static final double AUTO_TURN = degreesToInches(51);

    static  final double TIMEOUT = 15.0;

    int frameCount = 0;

    boolean hasEncoders = false;

    @Override
    public void runOpMode() throws InterruptedException {
        hardware.init(this.hardwareMap);
        if (hasEncoders) {
            hardware.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            hardware.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            waitForStart();

        //Wait for a hardware cycle to allow other processes to run
            waitOneFullHardwareCycle();


            encoderDrive(DRIVE_SPEED, feetToInches(4), TIMEOUT);
        }
        else {
            hardware.leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            hardware.rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            noEncoderDrive(DRIVE_SPEED, 2);
        }
    }

    public void noEncoderDrive(double speed, int seconds) {
          ElapsedTime time = new ElapsedTime();
          hardware.leftMotor.setPower(speed * .65);
         hardware.rightMotor.setPower(speed * .65);
          while (time.milliseconds() <= (seconds * 1000)) { }
        hardware.leftMotor.setPower(0);
        hardware.rightMotor.setPower(0);
    }
/*
    @Override
    public void runOpMode() throws InterruptedException {





    }

    // pingas was here
    // pingas was here
    public double feetToInches(double feet) {
        return feet * 12;
    }

    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) throws InterruptedException {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = hardware.leftMotor.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget =  hardware.rightMotor.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            hardware.leftMotor.setTargetPosition(newLeftTarget);
            hardware.rightMotor.setTargetPosition(newRightTarget);

            // Turn On RUN_TO_POSITION
            hardware.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            hardware.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            hardware.leftMotor.setPower(Math.abs(speed) - hardware.CompensationLeft);
            hardware.rightMotor.setPower(Math.abs(speed) - hardware.CompensationRight);

            // keep looping while we are still active, and there is time left, and both motors are running.
            //        while (opModeIsActive() &&
            //              (runtime.seconds() < timeoutS) &&
            //            (hardware.leftMotor.isBusy() && hardware.rightMotor.isBusy())) {

            // Display it for the driver.
            //telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
            //telemetry.addData("Path2",  "Running at %7d :%7d",
            // hardware.leftMotor.getCurrentPosition(),
            //hardware.rightMotor.getCurrentPosition());
            //telemetry.update();

            // Allow time for other processes to run.
            //              idle();
//            }

            // Stop all motion;
            hardware.leftMotor.setPower(0);
            hardware.rightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            hardware.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            hardware.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // optional pause after each move
        }
    }

    public void encoderTurn(double speed, double inches, boolean left, double timeout) throws InterruptedException {
        encoderDrive(speed, (left) ? (inches) : (0), (left) ? (0) : (inches), timeout);
    }

    public  void encoderDrive(double speed, double inches, double timeout) throws InterruptedException {

        encoderDrive(speed, inches, inches, timeout);
    }

}
*/

@Autonomous(name="Autonomous - Light Sensor", group="MBot")
public class MBotAutonomousUnstable extends LinearOpMode {

    /* Declare OpMode members. */
    MBotHardwareControl         robot   = new MBotHardwareControl();   // Use a Pushbot's hardware
    private ElapsedTime     runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);

    static final double     FORWARD_SPEED = 0.6;
    static final double     TURN_SPEED    = 0.5;



    @Override
    public void runOpMode() throws InterruptedException {

        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        robot.getDrive().resetMotorEncoders();
        idle();
        robot.getDrive().runMotorsWithEncoders();
        robot.awesomeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        while (!isStarted()) {

            // Display the light level while we are waiting to start
            telemetry.addData("Light Level", robot.getTapeFinder().GetLightDetected());
            telemetry.update();
            idle();
        }

/*
        encoderDrive(FORWARD_SPEED, -5,-5,15);
        encoderDrive(TURN_SPEED, 8,-8,15);
        encoderDrive(FORWARD_SPEED, -12,-12,15);
        encoderDrive(TURN_SPEED, -8,8,15);
        RunUntilLine();
        encoderDrive(TURN_SPEED, -8,8,15);
        encoderDrive(FORWARD_SPEED, -5,-5,15);
        */

        encoderDrive(FORWARD_SPEED, 4*12,4*12,15);
        encoderDrive(TURN_SPEED,-8,8,15);
        encoderDrive(FORWARD_SPEED, 5*12, 5*12, 30);
}

    /*public void drive(double time, boolean backwards) throws InterruptedException {

        double speed = backwards ? -FORWARD_SPEED : FORWARD_SPEED;
        robot.leftMotor.setPower(speed);
        robot.rightMotor.setPower(speed);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < time)) {
            telemetry.addData("IsBackwards", backwards);
            telemetry.addData("LeftSpeed", speed);
            telemetry.addData("RightSpeed", speed);
            telemetry.addData("Time", time);
            telemetry.update();
            idle();
        }
    }

    public void turn(double time, boolean goRight) throws InterruptedException {
        double lSpeed = goRight ? -TURN_SPEED : TURN_SPEED;
        double rSpeed = goRight ? TURN_SPEED : -TURN_SPEED;
        robot.leftMotor.setPower(lSpeed);
        robot.leftMotor.setPower(rSpeed);
        runtime.reset();
        while (opModeIsActive() && (runtime.seconds() < time)) {
            telemetry.addData("TurnDirection", goRight ? "Right" : "Left");
            telemetry.addData("LeftSpeed", lSpeed);
            telemetry.addData("RightSpeed", rSpeed);
            telemetry.addData("Time", time);
            idle();
        }
    }*/

    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) throws InterruptedException {
        int newLeftTarget;
        int newRightTarget;

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            newLeftTarget = robot.getDrive().getLeftPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newRightTarget = robot.getDrive().getRightPosition() + (int)(rightInches * COUNTS_PER_INCH);
            robot.getDrive().setLeftTarget(newLeftTarget);
            robot.getDrive().setRightTarget(newRightTarget);

            // Turn On RUN_TO_POSITION
            robot.getDrive().setRunToPosition();

            // reset the timeout time and start motion.
            runtime.reset();
            robot.getDrive().setMotorPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.getDrive().isLeftBusy() && robot.getDrive().isRightBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.getDrive().getLeftPosition(),
                        robot.getDrive().getRightPosition());
                telemetry.update();

                // Allow time for other processes to run.
                idle();
            }

            // Stop all motion;
            robot.getDrive().setMotorPower(0);

            // Turn off RUN_TO_POSITION
            robot.getDrive().resetMotorEncoders();

            //  sleep(250);   // optional pause after each move
        }
    }

    public void RunUntilLine() throws InterruptedException {
        robot.getDrive().setMotorPower(FORWARD_SPEED);
        while (!(opModeIsActive() && !(robot.getTapeFinder().AtLine()))) {
            telemetry.addData("Light Level", robot.getTapeFinder().GetLightDetected());
            telemetry.update();
            idle();
        }
        robot.getDrive().setMotorPower(0);
    }
}