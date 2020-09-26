package main.java.frc.robot.subsystems;
//Imports
import frc.robot.components.Limelight;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.PowerCell;
import frc.robot.components.IMU;
import frc.robot.OperatorInterface;
import frc.robot.subsystems.Chassis;
import frc.robot.Buttons;
import frc.robot.components.DevilDifferential;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Vision{
    //Creation of variables
    private DevilDifferential devilDrive;
    private OperatorInterface oi;
    private Limelight limeLight;
    private IMU imu;
    private Chassis drivetrain;
    private double kP= -0.1f;  // Proportional control constant
    private double min_command = 0.05f;
    private double leftSide = 0;
    private double rightSide = 0;
    

    public void init(OperatorInterface operatorInterface, IMU inertialMessurmentUnit, Chassis ChassiS, Limelight limelighT){
        imu = inertialMessurmentUnit;
        oi = operatorInterface;
        drivetrain = ChassiS;
        limeLight = limelighT;
    }

    

    public void go(){

            double tx = limeLight.getTx();
            double heading_error = -tx;
            double steering_adjust = 0.0f;
            if (tx > 1.0)
            {
                steering_adjust = kP*heading_error - min_command;
            }
            else if (tx < 1.0)
            {
                steering_adjust = kP*heading_error + min_command;
            }
            leftSide += steering_adjust;
            rightSide -= steering_adjust;
            drivetrain.driveTrain.tankDrive(leftSide, rightSide);
        
        }
    

}