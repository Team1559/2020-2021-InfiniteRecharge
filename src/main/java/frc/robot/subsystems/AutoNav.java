package frc.robot.subsystems;

import frc.robot.subsystems.Chassis;
import frc.robot.components.IMU;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;


public class AutoNav{

    //DO NOT RUN THESE YET
    
    private int pathSelector;
    private IMU imu;

    

    public void AutoInit(Chassis driveTrain , IMU imU, int pathSelectoR) {
        imu = imU;
        driveTrain.initOdometry();
        pathSelector = pathSelectoR;

    }
    public void AutoPeriodic(Chassis driveTrain, PowerCell powerCell, boolean doReverse) {
        
    }
    


}