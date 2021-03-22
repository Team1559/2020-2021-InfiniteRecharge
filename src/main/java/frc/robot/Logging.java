package frc.robot;
//imports
import frc.robot.subsystems.*;
import frc.robot.components.*;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Logging{
    private RobotContainer robotContainer;
    private IMU imu;
    private CompressorControl compressorControl;
    private Chassis driveTrain;
    private PowerCell powerCell;
    private Climber climber;
    private Spinner spinner;
    private AdvancedAuto advancedAuto;
    private BasicAuto basicAuto;
    
    //Master Booleans
    private boolean printLogs = false; //master boolean disable to kill all print logs
    private boolean smartDasboardLogs = false; //master boolean disable to kill all smart dashboard logs

    //Class pPrint Booleans
    private boolean printCompressorLogs = false;
    private boolean printImuLogs = false;

    //Class Smart Dashboard Logs
    private boolean ChassisSmartdashboardLogs = true;
    private boolean imuSmartDashboardLogs = true;

    public void init(IMU Imu, Chassis DriveTrain, PowerCell powerCelL, Climber climbeR, Spinner spinneR, CompressorControl compressorControL, AdvancedAuto advancedAutO, BasicAuto basicAutO, RobotContainer robotContaineR){
        imu = Imu;
        driveTrain = DriveTrain;
        powerCell = powerCelL;
        climber = climbeR;
        spinner = spinneR;
        compressorControl = compressorControL;
        advancedAuto = advancedAutO;
        basicAuto = basicAutO; 
        robotContainer = robotContaineR;
    }
    public void Log(){
        if(printLogs && printCompressorLogs){
            System.out.println("Compressor Logs");
            System.out.println(compressorControl.isCompressorOn);
            System.out.println();
        }
        if(printLogs && printImuLogs){
            System.out.println("IMU Logs");
            System.out.println("Yaw " + imu.yaw);
            // System.out.println("Pitch " + imu.pitch);
            // System.out.println("Roll " + imu.roll);
            System.out.println();
        }
        if(printLogs && printCompressorLogs){
            for(int i=0; i<4; i++){
                System.out.println("Motor " + (i+1) + " Temp: " + driveTrain.getMotorTemps()[i]);
            }
        }
        if(smartDasboardLogs && imuSmartDashboardLogs){
            // SmartDashboard.putNumber("Imu yaw", imu.yaw);
        }
        if(smartDasboardLogs && ChassisSmartdashboardLogs){
            for(int i=0; i<4; i++){
                // SmartDashboard.putNumber("Motor " + (i+1) + " Temp: ", driveTrain.getMotorTemps()[i]);
                // SmartDashboard.putNumber("Right Encoder velocity ", -driveTrain.rEncoder.getVelocity());
                // SmartDashboard.putNumber("left Encoder velocity ", driveTrain.lEncoder.getVelocity());
                // SmartDashboard.putNumber("left motor voltage ",driveTrain.LeftVolts);
                // SmartDashboard.putNumber("right motor voltage ",driveTrain.RightVolts);
            }
        }

    }
}