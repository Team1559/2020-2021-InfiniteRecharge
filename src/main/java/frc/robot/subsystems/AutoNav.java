package frc.robot.subsystems;

import frc.robot.subsystems.Chassis;
import frc.robot.components.IMU;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

import java.util.List;

import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;


public class AutoNav{

    //DO NOT RUN THESE YET
    private RobotContainer robotContainer;
    public String pathSelector;
    private IMU imu;
    public Command m_autonomousCommand;
    private Trajectory trajectory;

    

    public void AutoInit(Chassis driveTrain , IMU imU, String pathSelectoR, RobotContainer robotContaineR) {
        imu = imU;
        driveTrain.initOdometry();
        pathSelector = pathSelectoR;
        robotContainer = robotContaineR;
            setTrajectory();
        m_autonomousCommand = robotContainer.getAutonomousCommand();
        if (m_autonomousCommand != null) {
            m_autonomousCommand.schedule();
          }
          robotContainer.setTrajectory(trajectory);

    }
    public void AutoPeriodic(Chassis driveTrain, PowerCell powerCell) {
        CommandScheduler.getInstance().run();
    }
    private void setTrajectory(){
        if(pathSelector == "example"){
            trajectory =
               TrajectoryGenerator.generateTrajectory(
                   // Start at the origin facing the +X direction
                   new Pose2d(0, 0, new Rotation2d(0)),
                   // Pass through these two interior waypoints, making an 's' curve path
                   List.of(new Translation2d((1) * 2.27, (1) * 2.27), new Translation2d((2) * 2.27, (-1) * 2.27)),
                   // End 3 meters straight ahead of where we started, facing forward
                   new Pose2d(3, 0, new Rotation2d(0)),
                   // Pass config
                   robotContainer.config);
           }
           else{
               trajectory =
               TrajectoryGenerator.generateTrajectory(new Pose2d(0, 0, new Rotation2d(0)), 
               List.of(new Translation2d(0, 0), 
               new Translation2d(0, 0)),
                new Pose2d(0, 0, new Rotation2d(0)), 
                robotContainer.config);
           }
    }

}