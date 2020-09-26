package main.java.frc.robot.subsystems;
//Imports
import frc.robot.components.limelight;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.PowerCell;
import frc.robot.components.IMU;
import frc.robot.OperatorInterface;
public class Vision{
//Creation of variables
private OperatorInterface oi;
private IMU imu;
private double yaw = 0; 

public void init(OperatorInterface operatorInterface, IMU inertialMessurmentUnit ){
    imu = inertialMessurmentUnit;
    oi = operatorInterface;
}

    

}