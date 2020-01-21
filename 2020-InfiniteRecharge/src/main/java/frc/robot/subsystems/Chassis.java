package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;;

public class Chassis {
    // @Log.SpeedController
    // private WPI_TalonSRX motorFL;
    // @Log.SpeedController
    // private WPI_TalonSRX motorFR;
    private CANSparkMax sparkMax1; // TBD
    private CANPIDController sparkMax1PID;
    private CANSparkMax sparkMax2;
    private CANPIDController sparkMax2PID;
    private CANSparkMax sparkMax3;
    private CANPIDController sparkMax3PID;
    private CANSparkMax sparkMax4;
    private CANPIDController sparkMax4PID;
    private DifferentialDrive driveTrain;


    public Chassis() {
        sparkMax1 = new CANSparkMax(11, MotorType.kBrushless);
        sparkMax1PID = sparkMax1.getPIDController();
        sparkMax2 = new CANSparkMax(12, MotorType.kBrushless);
        sparkMax2PID = sparkMax2.getPIDController();
        sparkMax3 = new CANSparkMax(13, MotorType.kBrushless);
        sparkMax3PID = sparkMax3.getPIDController();
        sparkMax4 = new CANSparkMax(14, MotorType.kBrushless);
        sparkMax4PID = sparkMax4.getPIDController();

        sparkMax1PID.setP(1);
        sparkMax1PID.setI(0);
        sparkMax1PID.setD(0);
        sparkMax1PID.setReference(10, ControlType.kVelocity);

        sparkMax2PID.setP(1);
        sparkMax2PID.setI(0);
        sparkMax2PID.setD(0);
        sparkMax2PID.setReference(10, ControlType.kVelocity);

        sparkMax3PID.setP(1);
        sparkMax3PID.setI(0);
        sparkMax3PID.setD(0);
        sparkMax3PID.setReference(10, ControlType.kVelocity);

        sparkMax4PID.setP(1);
        sparkMax4PID.setI(0);
        sparkMax4PID.setD(0);
        sparkMax4PID.setReference(10, ControlType.kVelocity);

        SpeedControllerGroup leftMotors = new SpeedControllerGroup(sparkMax1, sparkMax3);
        leftMotors.setInverted(true);
        SpeedControllerGroup rightMotors = new SpeedControllerGroup(sparkMax2, sparkMax4);
        driveTrain = new DifferentialDrive(leftMotors, rightMotors);
    }
    public void DriveSystem(Joystick drive)
    {
        DriveSystem(drive, "Tank Drive");
    }

    public void DriveSystem(Joystick drive, String mode)
    {
        System.out.println(mode);
        switch(mode)
        {
             case "Tank Drive":
             driveTrain.tankDrive(-(drive.getRawAxis(1)),-(drive.getRawAxis(5)));
             break;

             case "Arcade Drive":
             driveTrain.arcadeDrive(-(drive.getRawAxis(1)), drive.getRawAxis(2));
             break;

             case "Curvature Drive":
             driveTrain.curvatureDrive(-(drive.getRawAxis(1)), drive.getRawAxis(2), true);
             break;
        }
        
    }
}