package roboticschs;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="Driver Control")
public class DriverControl extends OpMode {
    // motors for the wheels
    private DcMotor frontLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor armMotor; // motor to move arm up and down
    private Servo hookServo; // servo for hook
    private double x; // the x position of the left joystick (used to move robot left and right)
    private double y; // the y position of the left joystick (use to move robot forward and back)
    private double pivot; // the x position of the right joystick (use to rotate robot)

    // init runs once at the beginning when "INIT" is pressed
    // this is where we set left motors to reverse, assign
    // our configured motors and servos to variables, and reset
    // variables that will later intake data
    @Override
    public void init() {
        // lets us know that the program has been properly initialized
        telemetry.addData("Status", "Initialized");

        // assigning the motors and servo variables to the configured
        // name on the driver hub
        frontLeftMotor = hardwareMap.get(DcMotor.class, "FrontLeft");
        frontRightMotor = hardwareMap.get(DcMotor.class, "FrontRight");
        backLeftMotor = hardwareMap.get(DcMotor.class, "BackLeft");
        backRightMotor = hardwareMap.get(DcMotor.class, "BackRight");
        armMotor = hardwareMap.get(DcMotor.class, "arm motor");
        hookServo = hardwareMap.get(Servo.class, "hook");

        // setting the directions of the motors because the motors for the left wheels
        // are in opposite direction of the motors for the right wheels. The motor for
        // the arm is also facing a opposite direction
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        armMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // setting hook servo position to 1, which is upright, aka not pressing the hook
        hookServo.setPosition(1.0);

        // setting x and y positions of left joystick to 0
        x = 0;
        y = 0;
        // setting x value of right joystick to 0
        pivot = 0;
    }

    // will run continuously after start is pressed until the program is stopped
    @Override
    public void loop() {
        telemetry.update();
        // update x and y values of left joystick and pivot from right joystick
        x = gamepad1.left_stick_x;
        y = gamepad1.left_stick_y;
        pivot = gamepad1.right_stick_x;

        // calling movement, arm, and hook related methods continuously
        movement();
        moveArm();
        moveHook();

    }

    // Purpose: to move the robot forward, backward, left, right, and diagonal
    // How to use: gamePad1 controls robot movement. Movement is controlled by the
    // left joystick and rotation is controlled by the right joystick
    public void movement(){
        // powers each motor according to the position the left and
        // right joysticks are at
        frontLeftMotor.setPower(y - x - pivot);
        frontRightMotor.setPower(y + x + pivot);
        backLeftMotor.setPower(y + x - pivot);
        backRightMotor.setPower(y - x + pivot);
    }

    // Purpose: to move the arm up and down
    // How to use: gamePad2 controls arm movement. Pressing dPadUp moves the
    // arm up and pressing dPadDown moves the arm down.
    // Note: if the arm is that is maximum height, it will not move up any more.
    // if the arm is at its minimum height, it will not move down any more.
    public void moveArm(){
        // if dpad up is pressed and the arm has not reached its max height
        if(gamepad2.dpad_up && armMotor.getCurrentPosition() <=100000){
            // set target position to 100 higher
            armMotor.setTargetPosition(armMotor.getCurrentPosition() + 100);
            armMotor.setPower(1.0); // move at max speed
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); // and move to that position
        }
        // if dpad down is pressed and the arm has not reached its minimum height
        if(gamepad2.dpad_down && armMotor.getCurrentPosition() >= 0){
            // set target position to 100 less than its current position
            armMotor.setTargetPosition(armMotor.getCurrentPosition() - 100);
            armMotor.setPower(-1.0); // move at max speed
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION); // and move to that position
        }
    }

    // Purpose: to open and close the hook
    // A is used to open the hook
    // Y is used to close the hook
    public void moveHook(){
        // if A is pressed
        if(gamepad2.a){
            // move the servo to position 0.5, which is pressing down on the hook
            // this makes the hook open up
            hookServo.setPosition(0.5);
        }
        // if Y is pressed
        if(gamepad2.y){
            // move the servo to position 1.0, which lets go of the hook
            // this makes it close
            hookServo.setPosition(1.0);
        }
    }

}
