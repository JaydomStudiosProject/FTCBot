import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by jtnunley on 9/29/16.
 */

public class MBotProgramMoveStr8 extends LinearOpMode {
    private MBotHardwareControl hardware;

    public MBotProgramMoveStr8() {
        hardware = new MBotHardwareControl();

    }

    private ElapsedTime runtime = new ElapsedTime();

    static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 2.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;

    @Override
    public void runOpMode() throws InterruptedException {
        hardware.init(hardwareMap);
        // Send telemetry message to signify hardware waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        hardware.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hardware.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        idle();

        hardware.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset
        telemetry.addData("Path0",  "Starting at %7d :%7d",
                hardware.leftMotor.getCurrentPosition(),
                hardware.rightMotor.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // Step through each leg of the path,
        // Note: Reverse movement is obtained by setting a negative distance (not speed)
        encoderDrive(DRIVE_SPEED,  48,  48, 5.0);  // S1: Forward 47 Inches with 5 Sec timeout
        encoderDrive(TURN_SPEED,   12, -12, 4.0);  // S2: Turn Right 12 Inches with 4 Sec timeout
        encoderDrive(DRIVE_SPEED, -24, -24, 4.0);  // S3: Reverse 24 Inches with 4 Sec timeout

        hardware.servo.setPosition(1.0);            // S4: Stop and close the claw.
        sleep(1000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();
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
            hardware.leftMotor.setPower(Math.abs(speed));
            hardware.rightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (hardware.leftMotor.isBusy() && hardware.rightMotor.isBusy())) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        hardware.leftMotor.getCurrentPosition(),
                        hardware.rightMotor.getCurrentPosition());
                telemetry.update();

                // Allow time for other processes to run.
                idle();
            }

            // Stop all motion;
            hardware.leftMotor.setPower(0);
            hardware.rightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            hardware.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            hardware.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);   // optional pause after each move
        }
    }
}

