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
    // @Log.SpeedController
    // private WPI_TalonSRX motorFL;
    // @Log.SpeedController
    // private WPI_TalonSRX motorFR;
    private CANSparkMax sparkMax1; // TBD
    private CANSparkMax sparkMax2;
    private DifferentialDrive driveTrain;


    public Chassis() {
        sparkMax1 = new CANSparkMax(11, MotorType.kBrushless);
        sparkMax2 = new CANSparkMax(12, MotorType.kBrushless);
        SpeedControllerGroup leftMotors = new SpeedControllerGroup(sparkMax1);
        leftMotors.setInverted(true);
        SpeedControllerGroup rightMotors = new SpeedControllerGroup(sparkMax2);
        driveTrain = new DifferentialDrive(leftMotors, rightMotors);
    }

    public void DriveSystem(Joystick drive)
    {
        int mode = 1;
        if(drive.getRawButton(1))
            mode++;
        if(mode > 3)
            mode = 1;
        System.out.println(mode);
        // if(drive != null)
        //     System.out.println("****************Drive isn't NULL dimwit!****************");
        // else
        //     System.out.println("****************Drive is NULL dimwit!****************");
        // if(driveTrain == null)
        //     System.out.println("****************driveTrain is NULL dimwit!****************");
        // else
        //     System.out.println("****************driveTrain isn't NULL dimwit!****************");
        //driveTrain.tankDrive((-(drive.getRawAxis(1))),(-(drive.getRawAxis(5))));
        //driveTrain.tankDrive(0.5,-0.5);
        switch(mode)
        {
             case 1:
             driveTrain.tankDrive(-(drive.getRawAxis(1)),-(drive.getRawAxis(5)));
             break;

             case 2:
             driveTrain.arcadeDrive(-(drive.getRawAxis(1)), drive.getRawAxis(2));
             break;

             case 3:
             driveTrain.curvatureDrive(-(drive.getRawAxis(1)), drive.getRawAxis(2), true);
             break;
        }
        
    }
}