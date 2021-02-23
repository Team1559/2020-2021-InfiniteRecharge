package frc.robot.subsystems;

import frc.robot.subsystems.Chassis;
import frc.robot.components.IMU;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import frc.robot.RobotContainer;


public class AutoNav{

    //DO NOT RUN THESE YET
    private RobotContainer robotContainer;
    public String pathSelector;
    private IMU imu;

    

    public void AutoInit(Chassis driveTrain , IMU imU, String pathSelectoR, RobotContainer robotContaineR) {
        imu = imU;
        driveTrain.initOdometry();
        pathSelector = pathSelectoR;
        robotContainer = robotContaineR;

    }
    public void AutoPeriodic(Chassis driveTrain, PowerCell powerCell) {
        robotContainer.getAutonomousCommand(pathSelector);//temporary
    }
    


}