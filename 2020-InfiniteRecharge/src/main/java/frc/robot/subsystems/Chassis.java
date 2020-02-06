package frc.robot.subsystems;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import frc.robot.OperatorInterface;
import frc.robot.Wiring;
import frc.robot.widgets.MotorWidget;
import frc.robot.widgets.SCGWidget;
import edu.wpi.first.wpilibj.Solenoid;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

public class Chassis implements Loggable{

    @Log
    private String Gear = "Low";
    private CANEncoder lEncoder;
    private CANEncoder rEncoder;
    private CANSparkMax sparkMax1; // TBD
    private CANPIDController sparkMax1PID;
    private CANSparkMax sparkMax2;
    private CANPIDController sparkMax2PID;
    private CANSparkMax sparkMax3;
    private CANPIDController sparkMax3PID;
    private CANSparkMax sparkMax4;
    private CANPIDController sparkMax4PID;
    @Log
    private double shiftUp;
    @Log
    public double ShiftDown;
    private Solenoid gearShifter;
    private OperatorInterface oi;
    private DifferentialDrive driveTrain;

    private ShuffleboardTab tab;

    private MotorWidget widget1;
    private MotorWidget widget2;
    private MotorWidget widget3;
    private MotorWidget widget4;
    private SCGWidget widget5;
    private SCGWidget widget6;

    private SpeedControllerGroup leftMotors;
    private SpeedControllerGroup rightMotors;
    @Log.Graph
    private double leftVelocity;
    @Log.Graph
    private double rightVelocity;
    @Log.Dial(max = 6000, min = 0)
    private double velocity;
    @Log.Graph
    public double motor1current;
    @Log.Graph
    public double motor2current;
    @Log.Graph
    public double motor3current;
    @Log.Graph
    public double motor4current;

    @Log.Dial
    private double motor1Temp;
    @Log.Dial
    private double motor2Temp;
    @Log.Dial
    private double motor3Temp;
    @Log.Dial
    private double motor4Temp;

    private boolean shift = false;

    @Config.ToggleSwitch
    public void Enable_Shifting(boolean enable){
        shift = enable;
    }
    @Config
    public void Shifting_points(double up, double down){
        shiftUp = up;
        ShiftDown = down;
    }
    
    public void Init(OperatorInterface oInterface)
    {
        shiftUp  =5000;
        ShiftDown = 3500;
        oi = oInterface;
        sparkMax1 = new CANSparkMax(11, MotorType.kBrushless); //ID 11
        sparkMax2 = new CANSparkMax(12, MotorType.kBrushless); //ID 12
        sparkMax3 = new CANSparkMax(13, MotorType.kBrushless); //ID 13
        sparkMax4 = new CANSparkMax(14, MotorType.kBrushless); //ID 14
        lEncoder = new CANEncoder(sparkMax1);
        rEncoder = new CANEncoder(sparkMax2);
        sparkMax3.follow(sparkMax1);
        sparkMax4.follow(sparkMax2);

        leftMotors = new SpeedControllerGroup(sparkMax1); // (sparkMax1, sparkMax3) for working code
        rightMotors = new SpeedControllerGroup(sparkMax2); // (sparkMax2, sparkMax4) for working code
        
        widget1 = new MotorWidget(sparkMax1, "Motor 1");
        widget2 = new MotorWidget(sparkMax2, "Motor 2");
        widget3 = new MotorWidget(sparkMax3, "Motor 3");
        widget4 = new MotorWidget(sparkMax4, "Motor 4");
        widget5 = new SCGWidget(leftMotors, "Left Motors");
        widget6 = new SCGWidget(rightMotors, "Right Motors");

        gearShifter = new Solenoid(Wiring.gearShifterSolenoid);
    

        sparkMax1PID = sparkMax1.getPIDController();
        
        sparkMax2PID = sparkMax2.getPIDController();
        
        sparkMax3PID = sparkMax3.getPIDController();
        
        sparkMax4PID = sparkMax4.getPIDController();

        sparkMax1.setOpenLoopRampRate(0.4);
        sparkMax2.setOpenLoopRampRate(0.4);
        sparkMax3.setOpenLoopRampRate(0.4);
        sparkMax4.setOpenLoopRampRate(0.4);

        sparkMax1PID.setP(1);
        sparkMax1PID.setI(0);
        sparkMax1PID.setD(0);

        sparkMax2PID.setP(1);
        sparkMax2PID.setI(0);
        sparkMax2PID.setD(0);

        sparkMax3PID.setP(1);
        sparkMax3PID.setI(0);
        sparkMax3PID.setD(0);

        sparkMax4PID.setP(1);
        sparkMax4PID.setI(0);
        sparkMax4PID.setD(0);

        
        leftMotors.setInverted(true);
        rightMotors.setInverted(true);
        
        driveTrain = new DifferentialDrive(leftMotors, rightMotors);

        tab = Shuffleboard.getTab("Chassis");

        
    }
    public void DriveSystem(Joystick drive)
    {
        DriveSystem(drive, "Arcade Drive");
    }

    public void DriveSystem(Joystick drive, String mode)
    {   
        // graphing the motor parameters
        motor1Temp = sparkMax1.getMotorTemperature();
        motor2Temp = sparkMax2.getMotorTemperature();
        motor3Temp = sparkMax3.getMotorTemperature();
        motor4Temp = sparkMax4.getMotorTemperature();
        motor1Current = sparkMax1.getOutputCurrent();
        motor2Current = sparkMax2.getOutputCurrent();
        motor3Current = sparkMax3.getOutputCurrent();
        motor4Current = sparkMax4.getOutputCurrent();
        if(shift){
        gearShift();        //System.out.println(mode);
        }
        else{gearShifter.set(false);
        }
        switch(mode)
        {
             case "Tank Drive":
            driveTrain.tankDrive(-(oi.getPilotX()),-(oi.pilot.getRawAxis(5)));
            
             break;

             case "Arcade Drive":
             if(drive == null)
             {
                 System.out.println("Your controller is Null dimwit!!!!");
             }
             else
             {
                driveTrain.arcadeDrive(-(oi.getPilotY()), oi.getPilotZ());
                
             }
             
             break;

             case "Curvature Drive":
             driveTrain.curvatureDrive(-(oi.getPilotY()), oi.getPilotZ(), true);
             
             break;

             case "Shuffle Drive Individual":
             widget1.changeOutput();
             widget2.changeOutput();
             widget3.changeOutput();
             widget4.changeOutput();
             break;

             case "Shuffle Drive Control Groups":
             widget5.changeOutput();
             widget6.changeOutput();
             break;
        }
    }

    public void gearShift()
    {
        leftVelocity = lEncoder.getVelocity();
        rightVelocity = -(rEncoder.getVelocity());

        velocity = Math.abs((leftVelocity + rightVelocity) /2);

        if(velocity >= shiftUp){
            gearShifter.set(true);
            Gear = "High";
        }
        else if(velocity <= ShiftDown){
            gearShifter.set(false);
            Gear = "Low";

        }
    }
}
