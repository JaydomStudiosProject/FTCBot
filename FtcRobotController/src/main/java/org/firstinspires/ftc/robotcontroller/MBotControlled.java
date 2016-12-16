package org.firstinspires.ftc.robotcontroller;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * Created by jtnunley on 10/22/16.
 */


public abstract class MBotControlled extends LinearOpMode {


    MBotHardwareControl hardware = new MBotHardwareControl();

    double          clawOffset      = 0;// Servo mid position
    double servoMin = -0.5;
    double servoMax = 0.5;
    final double    CLAW_SPEED      = 0.02 ;

    private boolean slow = false;
    private boolean isDown = false;
    private boolean isUp = false;
    private static final int STANDARD_BOOST = 7000;

    public void monitorX() {
        if (gamepad1.x) {
            isUp = true;
           if (!isDown)
           {
               slow = true;
               isDown = false;
           }
        }
        else if (!gamepad1.x) {
            isDown = true;
            if (!isUp) {
                slow = false;
                isUp = false;
            }
        }
    }

    public class two_ints {
        public double i;
        public double j;

        public two_ints(double i, double j) {
            this.i = i;
            this.j = j;
        }
    }

    protected abstract two_ints func(double lsx, double lsy, double rsx, double rsy);

    @Override
    public void runOpMode() throws InterruptedException {
        hardware.init(hardwareMap);
       hardware.getDrive().runMotorsWithoutEncoders();
        hardware.awesomeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
       // try {
            // Wait for the game to start (driver presses PLAY)
            waitForStart();

            // run until the end of the match (driver presses STOP)
            while (opModeIsActive()) {
                //monitorX();

               double left;
               double right;
                double max;
                double speed;

                //double speed;
                speed = 1.0;
                if (gamepad1.x)
                    speed = 0.5;


                // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
                // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
                two_ints ti = func(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, gamepad1.right_stick_y);
                left = ti.i;
                right = ti.i;

                left = -left;
                right = -right;

                // Normalize the values so neither exceed +/- 1.0
               // left = Range.clip(left, 0.0, 1.0);
               // right = Range.clip(right, 0.0,1.0);

                max = Math.max(Math.abs(left), Math.abs(right));
                if (max > 1.0)
                {
                    left /= max;
                    right /= max;
                }

                double aSpeed = gamepad1.left_bumper ? 1 : (gamepad1.left_trigger > 0 ? -1 : 0);
                aSpeed = gamepad1.right_bumper ? 0.25 : (gamepad1.right_trigger > 0 ? -0.25 : aSpeed);
                hardware.awesomeMotor.setPower(aSpeed);

                if (left == 0 && right != 0)
                    left = -right;
                else if (left != 0 && right == 0)
                    right = -left;

                left *= speed;
                right *= speed;

                hardware.getDrive().setLeftMotorPower(((left * speed) - hardware.CompensationLeft) * .65);
                hardware.getDrive().setRightMotorPower(((right * speed) - hardware.CompensationRight) * .65);

                // Use gamepad left & right Bumpers to open and close the claw
                if (gamepad1.right_bumper)
                    clawOffset += CLAW_SPEED;
                else if (gamepad1.left_bumper)
                    clawOffset -= CLAW_SPEED;

                // Move both servos to new position.  Assume servos are mirror image of each other.
  //              clawOffset = Range.clip(clawOffset, servoMin, servoMax);
               /* hardware.servo.setPosition(hardware.MID_SERVO + clawOffset);
                //hardware.rightCla.setPosition(robot.MID_SERVO - clawOffset);*/

                // Use gamepad buttons to move arm up (Y) and down (A)
            /*if (gamepad1.y)
                robot.armMotor.setPower(robot.ARM_UP_POWER);
            else if (gamepad1.a)
                robot.armMotor.setPower(robot.ARM_DOWN_POWER);
            else
                robot.armMotor.setPower(0.0);
            */

            //Send telemetry message to signify robot running;
            //telemetry.addData("claw",  "Offset = %.2f", clawOffset);
            telemetry.addData("left",  "%.2f", left);
            telemetry.addData("right", "%.2f", right);
            telemetry.addData("speed", "%.2f", speed);
            telemetry.update();
                // Pause for metronome tick.  40 mS each cycle = update 25 times a second
                hardware.waitForTick(40);
            }
        /*}
        catch (NullPointerException e) {

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            throw new Exception(sw.toString(),e);
        }*/
    }

    private ElapsedTime runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    static  final double TIMEOUT = 15.0;
}
