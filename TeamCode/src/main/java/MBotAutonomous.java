import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.lasarobotics.vision.android.Cameras;
import org.lasarobotics.vision.ftc.resq.Beacon;
import org.lasarobotics.vision.opmode.LinearVisionOpMode;
import org.lasarobotics.vision.opmode.extensions.CameraControlExtension;
import org.lasarobotics.vision.util.ScreenOrientation;
import org.opencv.core.Mat;
import org.opencv.core.Size;

/**
 * Created by jtnunley on 10/13/16.
 */

@Autonomous
public class MBotAutonomous extends LinearVisionOpMode {

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

    private void initCamera() throws InterruptedException {
        this.waitForVisionStart();

        this.setCamera(Cameras.PRIMARY);

        this.setFrameSize(new Size(450,450));

        enableExtension(Extensions.BEACON);
        enableExtension(Extensions.ROTATION);
        enableExtension(Extensions.CAMERA_CONTROL);

        beacon.setAnalysisMethod(Beacon.AnalysisMethod.FAST);

        beacon.setColorToleranceBlue(0);
        beacon.setColorToleranceRed(0);

        rotation.setIsUsingSecondaryCamera(false);
        rotation.disableAutoRotate();
        rotation.setActivityOrientationFixed(ScreenOrientation.PORTRAIT);

        cameraControl.setColorTemperature(CameraControlExtension.ColorTemperature.AUTO);
        cameraControl.setAutoExposureCompensation();

    }

    @Override
    public void runOpMode() throws InterruptedException {
        hardware.init(this.hardwareMap);
        initCamera();

        hardware.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        hardware.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        waitForStart();

        if (hasNewFrame()) {
            //Get the frame
            Mat rgba = getFrameRgba();
            Mat gray = getFrameGray();

            //Discard the current frame to allow for the next one to render
            discardFrame();

            //Do all of your custom frame processing here
            //For this demo, let's just add to a frame counter
            frameCount++;
        }

        //Wait for a hardware cycle to allow other processes to run
        waitOneFullHardwareCycle();

        encoderDrive(TURN_SPEED, AUTO_TURN, 0, 10);

        // turn right ~90 degrees
        encoderTurn(TURN_SPEED, degreesToInches(90), false, TIMEOUT);
        // press beacon
        pushBeacon();
        // turn 90 degress left and go straight for 4 ft
        encoderTurn(TURN_SPEED, degreesToInches(90), true, TIMEOUT);
        encoderDrive(DRIVE_SPEED, feetToInches(4), TIMEOUT*2);
        // turn 90 degrees right and press beacon
        encoderTurn(TURN_SPEED, degreesToInches(90), false, TIMEOUT);
        pushBeacon();
        // turn 90 degrees left and go straight for 8 ft
        encoderTurn(TURN_SPEED, degreesToInches(90), true, TIMEOUT);
        encoderDrive(DRIVE_SPEED, feetToInches(8), TIMEOUT*4);
    }
/*
    @Override
    public void runOpMode() throws InterruptedException {





    }
*/
    // pingas was here
    // pingas was here

    public static double degreesToInches(double degrees) {
        double factor = 12/52;
        return degrees * factor;
    }

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

    static final boolean isImplemented = false;

    // these are placeholders, change when accurate values are found
    static final double moveDistance = 3;
    static final double extendedServo = 100;
    static final double unextendedServo = 0;

    public  void pushBeacon() throws InterruptedException {
        //if (!isImplemented)
        //    return;

        /*Camera camera = null;
        try {
            camera = new Camera(Cameras.PRIMARY);
            ColorRGBA red = new ColorRGBA(255,0,0);
            ColorBlobDetector detector = new ColorBlobDetector(red);*/



            boolean detectedColor = true;

        /*if (hasNewFrame()) {
            //Get the frame
            Mat rgba = getFrameRgba();
            Mat gray = getFrameGray();

            //Discard the current frame to allow for the next one to render
            discardFrame();

            //Do all of your custom frame processing here
            //For this demo, let's just add to a frame counter
            frameCount++;
            if (rgba.)
        }*/

        String beaconColor = beacon.getAnalysis().getColorString().toLowerCase();
        if (beaconColor != null) {
            if (beaconColor != colorToFind)
                detectedColor = false;
        }

            if (!detectedColor) {
                encoderTurn(TURN_SPEED, degreesToInches(90), false, TIMEOUT);
                encoderDrive(DRIVE_SPEED, moveDistance, TIMEOUT);
                encoderTurn(TURN_SPEED, degreesToInches(90), true, TIMEOUT);
            }

            // extend servo
        hardware.servo.setDirection(Servo.Direction.FORWARD);
        hardware.servo.setPosition(extendedServo);
        wait(250);
        hardware.servo.setPosition(unextendedServo);

       /* }
        finally {
            camera.release();
        }*/


    }
}
