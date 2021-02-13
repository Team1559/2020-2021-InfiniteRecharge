package frc.robot.subsystems;

import frc.robot.subsystems.Chassis;
import frc.robot.components.IMU;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;


public class AutoNav{

    //DO NOT RUN THESE YET
    

    private IMU imu;

    

    public void AutoInit(Chassis driveTrain , IMU imU) {
        imu = imU;
        driveTrain.initOdometry();

    }
    public void AutoPeriodic(Chassis driveTrain, PowerCell powerCell, boolean doReverse) {
        
    }
    


}