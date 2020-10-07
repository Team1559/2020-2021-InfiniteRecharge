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
    
    //Booleans
    private boolean printVisionlogs = true;
    private boolean printCompressorLogs = true;
    private boolean printImuLogs = true;

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
    public void printLogs(){
        if(printVisionlogs){
            System.out.println("Vision Logs");
            System.out.println("left Side speed is " + vision.leftSide + "right side speed is "+ vision.rightSide);
            System.out.println("Tx is " + vision.tx);
            System.out.println("sterring ajust is" + vision.steering_adjust);
            System.out.println();
        }
        if(printCompressorLogs){
            System.out.println("Compressor Logs");
            System.out.println(compressorControl.isCompressorOn);
            System.out.println();
        }
        if(printCompressorLogs){
            System.out.println("IMU Logs");
            System.out.println(imu.yaw);
            System.out.println();
        }
    }
}