package frc.robot.subsystems;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import frc.robot.components.DevilDifferential;
import frc.robot.Buttons;
import frc.robot.OperatorInterface;
import frc.robot.components.IMU;
import frc.robot.Wiring;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;

public class Chassis{

    
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
    private double inputSpeed = 1;
    public double robotSpeed = 0;
    
    
    private Solenoid gearShifter;
    private OperatorInterface oi;
    private DevilDifferential driveTrain;
    private IMU imu;
    private DifferentialDriveOdometry m_odometry;

    public double rampRate = 0.6;

    private SpeedControllerGroup leftMotors;
    private SpeedControllerGroup rightMotors;
    private double leftVelocity;
    private double rightVelocity;



    private double kP = 0.0002; //0.0001
    private double kI = 0.000000; //0.0 (We don't really need I for now, maybe later)
    private double kD = 0.0000;
    private double kF = 0.00018; //0.000125
    private double deadband = 0.01;
    private double forwardSpeed = 0;
    private double backwardSpeed = 0;
    private double sideSpeed = 0;
    private double currentRampRate = 0;
    private boolean highGear = false;

    public void setRampRate(double rr){
        if(rr != currentRampRate){
            currentRampRate = rr;
            sparkMax1.setClosedLoopRampRate(rr);
            sparkMax2.setClosedLoopRampRate(rr);
            sparkMax3.setClosedLoopRampRate(rr);
            sparkMax4.setClosedLoopRampRate(rr);
        }
    }
   
    public double distance(){
        return (Math.abs(lEncoder.getPosition()) + Math.abs(rEncoder.getPosition())) / 2;
    }

    public void LogEncoders() {
        System.out.println(lEncoder.getPosition() + " " + rEncoder.getPosition());
    }
 

    private boolean isSquaredInputs = true;

    
    public void Init(OperatorInterface oInterface, IMU Imu)
    {
        imu = Imu;
 
        oi = oInterface;
        sparkMax1 = new CANSparkMax(11, MotorType.kBrushless); //ID 11
        sparkMax2 = new CANSparkMax(12, MotorType.kBrushless); //ID 12
        sparkMax3 = new CANSparkMax(13, MotorType.kBrushless); //ID 13
        sparkMax4 = new CANSparkMax(14, MotorType.kBrushless); //ID 14
        sparkMax1.restoreFactoryDefaults();
        sparkMax2.restoreFactoryDefaults();
        sparkMax3.restoreFactoryDefaults();
        sparkMax4.restoreFactoryDefaults();
        lEncoder = new CANEncoder(sparkMax1);
        rEncoder = new CANEncoder(sparkMax2);
        sparkMax3.follow(sparkMax1);
        sparkMax4.follow(sparkMax2);

        leftMotors = new SpeedControllerGroup(sparkMax1); // (sparkMax1, sparkMax3) for working code
        rightMotors = new SpeedControllerGroup(sparkMax2); // (sparkMax2, sparkMax4) for working code

        gearShifter = new Solenoid(Wiring.gearShifterSolenoid);
    

        sparkMax1PID = sparkMax1.getPIDController();
        sparkMax1PID.setReference(0, ControlType.kVelocity);
        sparkMax2PID = sparkMax2.getPIDController();
        sparkMax2PID.setReference(0, ControlType.kVelocity);
        sparkMax3PID = sparkMax3.getPIDController();
        sparkMax3PID.setReference(0, ControlType.kVelocity);
        sparkMax4PID = sparkMax4.getPIDController();
        sparkMax4PID.setReference(0, ControlType.kVelocity);

       setRampRate(0.6);

        sparkMax1PID.setP(kP);
        sparkMax1PID.setI(kI);
        sparkMax1PID.setD(kD);
        sparkMax1PID.setFF(kF);
        sparkMax1PID.setIZone(10, 0);
        sparkMax1PID.setIMaxAccum(20, 0);
        sparkMax1PID.setIAccum(0); //may need to be set\\

        sparkMax2PID.setP(kP);
        sparkMax2PID.setI(kI);
        sparkMax2PID.setD(kD);
        sparkMax2PID.setFF(kF);
        sparkMax2PID.setIZone(10, 0);
        sparkMax2PID.setIMaxAccum(20, 0);

        sparkMax3PID.setP(kP);
        sparkMax3PID.setI(kI);
        sparkMax3PID.setD(kD);
        sparkMax3PID.setFF(kF);
        sparkMax3PID.setIZone(10, 0);
        sparkMax3PID.setIMaxAccum(20, 0);

        sparkMax4PID.setP(kP);
        sparkMax4PID.setI(kI);
        sparkMax4PID.setD(kD);
        sparkMax4PID.setFF(kF);
        sparkMax4PID.setIZone(10, 0);
        sparkMax4PID.setIMaxAccum(20, 0);
        
        sparkMax1.setIdleMode(IdleMode.kBrake);
        sparkMax2.setIdleMode(IdleMode.kBrake);
        sparkMax3.setIdleMode(IdleMode.kBrake);
        sparkMax4.setIdleMode(IdleMode.kBrake);



        leftMotors.setInverted(true);
        rightMotors.setInverted(true);

        sparkMax1.setSmartCurrentLimit(40);
        sparkMax2.setSmartCurrentLimit(40);
        sparkMax3.setSmartCurrentLimit(40);
        sparkMax4.setSmartCurrentLimit(40);

        
        driveTrain = new DevilDifferential(sparkMax1PID, sparkMax2PID);
        driveTrain.setMaxOutput(5600); //NEO free speed 5700 RPM
        driveTrain.setExpiration(2.0);
    }

    public void DriveSystem(Joystick drive)
    {   
       
        if(oi.pilot.getRawButton(Buttons.X)){
            inputSpeed = 0.5;
            leftVelocity = lEncoder.getVelocity();
            rightVelocity = -(rEncoder.getVelocity());
            robotSpeed = Math.max(Math.abs(leftVelocity),Math.abs(rightVelocity));
            if(robotSpeed < 0.3 * 5600)
            {
            highGear = true;
            setRampRate(0.1);
            }
        }
        else{
            inputSpeed = 1;
            highGear = false;
            setRampRate(rampRate);
            gearShift();
        }
        
        
        if(highGear){
            gearShifter.set(true);
        }
        else{
            gearShifter.set(false);
        }
            //Axis 0 for left joystick left to right
            //Axis 2 for left Trigger
            //Axis 3 for right Trigger
            forwardSpeed = oi.pilot.getRawAxis(Buttons.rightTrigger) * inputSpeed;
            backwardSpeed = oi.pilot.getRawAxis(Buttons.leftTrigger) * inputSpeed;
            sideSpeed = -oi.getPilotX() *inputSpeed;
            if(forwardSpeed <= deadband)
            {
                forwardSpeed = 0;
            }
            if(backwardSpeed <= deadband)
            {
                backwardSpeed = 0;
            }
            if(sideSpeed >= -deadband && sideSpeed <= deadband)
            {
                sideSpeed = 0;
            }
            driveTrain.arcadeDrive(forwardSpeed-backwardSpeed, sideSpeed, isSquaredInputs);
    }


    public void gearShift()
    {
        if(oi.pilot.getRawAxis(Buttons.rightJoystick_y) >= 0.5) {
            highGear = true;
        }
        else{
            highGear = false;        }
    }

    public void move(double speed, double rotation)
    {
            driveTrain.arcadeDrive(speed,rotation,false);
    }

    public void initOdometry()
    {
        lEncoder.setPosition(0);
        rEncoder.setPosition(0);
        imu.zeroYaw();
        Rotation2d yaw = new Rotation2d(imu.getYaw());
        m_odometry = new DifferentialDriveOdometry(yaw);
        //Neccessary for advanced auto new Pose2d(0,0 new Rotation2d())
    }

    public double R2M(double rotations)             //gear ratio: 15.2:1
    {                                               //wheel diameter 5.8in
        double out = rotations/15.2*5.8*Math.PI/39.37;
        return out;
    }

    public Pose2d updateOdometry()
    {
        return m_odometry.update(new Rotation2d(imu.getYaw()), R2M(lEncoder.getPosition()), R2M(rEncoder.getPosition()));
    }

    public void disabled()
    {
        sparkMax1.setIdleMode(IdleMode.kCoast);
        sparkMax2.setIdleMode(IdleMode.kCoast);
        sparkMax3.setIdleMode(IdleMode.kCoast);
        sparkMax4.setIdleMode(IdleMode.kCoast);
    }
}
