package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;;

public class Chassis {
    @Log.SpeedController
    private WPI_TalonSRX motorFL;
    @Log.SpeedController
    private WPI_TalonSRX motorFR;
    private CANSparkMax sparkMax1; // TBD
    private CANSparkMax sparkMax2;
    private DifferentialDrive driveTrain;


    public Chassis(CANSparkMax fLMotor, CANSparkMax fRMotor) {
        sparkMax1 = fLMotor;
        sparkMax2 = fRMotor;
        SpeedControllerGroup leftMotors = new SpeedControllerGroup(motorFL);
        leftMotors.setInverted(true);
        SpeedControllerGroup rightMotors = new SpeedControllerGroup(motorFR);
        driveTrain = new DifferentialDrive(leftMotors, rightMotors);
    }

    public void DriveSystem(Joystick x, Joystick y)
    {
        driveTrain.tankDrive(x.getX(),y.getY());
    }
}