package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.SparkMax;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;;

public class Chassis {
    @Log.SpeedController
    private WPI_TalonSRX motorFL;
    @Log.SpeedController
    private WPI_TalonSRX motorFR;
    private Spark spark1; // TBD
    private Spark spark2; // TBD
    private SparkMax sparkMax1; // TBD

    public Chassis(WPI_TalonSRX fLMotor, WPI_TalonSRX fRMotor) {
        motorFL = fLMotor;
        motorFR = fRMotor;
        SpeedControllerGroup leftMotors = new SpeedControllerGroup(motorFL);
        leftMotors.setInverted(true);
        SpeedControllerGroup rightMotors = new SpeedControllerGroup(motorFR);
        DifferentialDrive driveTrain = new DifferentialDrive(leftMotors, rightMotors);
    }
}