package frc.robot;

import frc.robot.subsystems.PowerCell;
import frc.robot.subsystems.Spinner;
import frc.robot.components.Camera;
import frc.robot.components.IMU;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.AdvancedAuto;
import frc.robot.subsystems.BasicAuto;
import frc.robot.subsystems.Vision;
import frc.robot.components.CompressorControl;
import frc.robot.components.Limelight;
import frc.robot.components.DistSensor;
import edu.wpi.first.wpilibj.AnalogInput;

public class Logging{
    private Vision vision;
    private IMU imu;
    private Chassis driveTrain;
    private Limelight limeLight;
    private DistSensor distSensor;
    private PowerCell powerCell;
    private Climber climber;
    private Spinner spinner;
    private CompressorControl compressorControl;
    private AdvancedAuto advancedAuto;
    private BasicAuto basicAuto;

    public void init(Vision visioN, IMU Imu, Chassis DriveTrain, Limelight limeLighT, DistSensor distSensoR,PowerCell powerCelL, Climber climbeR, Spinner spinneR, CompressorControl compressorControL, AdvancedAuto advancedAutO, BasicAuto basicAutO){
        vision = visioN;
        imu = Imu;
        driveTrain = DriveTrain;
        limeLight = limeLighT;
        distSensor = distSensoR;
        powerCell = powerCelL;
        climber = climbeR;
        spinner = spinneR;
        compressorControl = compressorControL;
        advancedAuto = advancedAutO;
        basicAuto = basicAutO;

         
    }

}