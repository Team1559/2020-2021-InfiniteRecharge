package frc.robot.subsystems;

import frc.robot.subsystems.Chassis;
import frc.robot.components.IMU;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;


public class AutoNav{

    //DO NOT RUN THESE YET
    private RobotContainer robotContainer;
    public String pathSelector;
    private IMU imu;
    public Command m_autonomousCommand;

    

    public void AutoInit(Chassis driveTrain , IMU imU, String pathSelectoR, RobotContainer robotContaineR) {
        imu = imU;
        driveTrain.initOdometry();
        pathSelector = pathSelectoR;
        robotContainer = robotContaineR;
        robotContainer.setpath(pathSelector);
        m_autonomousCommand = robotContainer.getAutonomousCommand();
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
          }

    }
    public void AutoPeriodic(Chassis driveTrain, PowerCell powerCell) {
        CommandScheduler.getInstance().run();
    }
    


}