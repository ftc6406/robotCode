package roboticschs;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous (name = "Autonomous")
public class Automous {
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
    private int lowJunction = 0; // position of lowest junction
    private int medJunction = 0; // position of medium junction
    private int highJunction = 0; // position of highest junction

    @Override
    public void init() {
        // lets us know that the program has been properly initialized
        Telemetry.addData("Status", "Initialized");

        // assigning the motors and servo variables to the configured
        // name on the driver hub
        frontLeftMotor = HardwareMap.get(DcMotor.class, "FrontLeft");
        frontRightMotor = HardwareMap.get(DcMotor.class, "FrontRight");
        backLeftMotor = HardwareMap.get(DcMotor.class, "BackLeft");
        backRightMotor = HardwareMap.get(DcMotor.class, "BackRight");
        armMotor = HardwareMap.get(DcMotor.class, "arm motor");
        hookServo = HardwareMap.get(Servo.class, "hook");

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

    public void start() {
        // grab cone
        hookServo.setPosition(0.5);
        // raise the arm to lowest junction height
        armMotor.setTargetPosition(); // set position to how much the arm needs to move
        armMotor.setPower(1.0);
        armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // move forward to low junction
        frontRightMotor.setTargetPosition(); // set position to how much the robot needs to move
        backRightMotor.setTargetPosition(); // set position to how much the robot needs to move
        frontLeftMotor.setTargetPosition(); // set position to how much the robot needs to move
        backLeftMotor.setTargetPosition(); // set position to how much the robot needs to move

        power(1.0, 1.0, 1.0, 1.0);
        move();
        // let go of hook
        hookServo.setPosition(1.0);
        // move back to starting position
        frontRightMotor.setTargetPosition(0);
        backRightMotor.setTargetPosition(0);
        frontLeftMotor.setTargetPosition(0);
        backLeftMotor.setTargetPosition(0);

        power(-1.0, -1.0, -1.0, -1.0);
        move();
        // turn right 90 degrees
        frontRightMotor.setTargetPosition(); // set position to how much the robot needs to move
        backRightMotor.setTargetPosition(); // set position to how much the robot needs to move
        frontLeftMotor.setTargetPosition(); // set position to how much the robot needs to move
        backLeftMotor.setTargetPosition(); // set position to how much the robot needs to move

        power(-1.0, -1.0, 1.0, 1.0);
        move();

        // drive forward to substation/terminal
        frontRightMotor.setTargetPosition(); // set position to how much the robot needs to move
        backRightMotor.setTargetPosition(); // set position to how much the robot needs to move
        frontLeftMotor.setTargetPosition(); // set position to how much the robot needs to move
        backLeftMotor.setTargetPosition(); // set position to how much the robot needs to move

        power(1.0, 1.0, 1.0, 1.0);
        move();
    }

    // Purpose: to drive the robot to set position
    public void move(){
        frontRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backLeftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    // Purpose: to set the power of the motors for the wheels
    public void power(double frontRight, double backRight, double frontLeft, double backLeft){
        frontRightMotor.setPower(frontRight);
        backRightMotor.setPower(backRight);
        frontLeftMotor.setPower(frontLeft);
        backLeftMotor.setPower(backLeft);
    }
}
