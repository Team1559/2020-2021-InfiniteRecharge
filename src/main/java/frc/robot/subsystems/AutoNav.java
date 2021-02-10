package frc.robot.subsystems;

import frc.robot.subsystems.Chassis;
import frc.robot.components.IMU;


public class AutoNav{

    //DO NOT RUN THESE YET
    public static final double ksVolts = 0;
    public static final double kvVoltSecondsPerMeter = 0;
    public static final double kaVoltSecondsSquaredPerMeter = 0;
    public static final double kPDriveVel = 0;


    public static final double kTrackwidthMeters = 0;
    public static final DifferentialDriveKinematics kDriveKinematics = new DifferentialDriveKinematics(kTrackwidthMeters);

    public static final double kMaxSpeedMetersPerSecond = 0;
    public static final double kMaxAccelerationMetersPerSecondSquared = 0;

        // Reasonable baseline values for a RAMSETE follower in units of meters and seconds
    public static final double kRamseteB = 2;
    public static final double kRamseteZeta = 0.7;

    private IMU imu;

    

    public void AutoInit(Chassis driveTrain , IMU imU) {
        imu = imU;
        driveTrain.initOdometry();

    }
    


}