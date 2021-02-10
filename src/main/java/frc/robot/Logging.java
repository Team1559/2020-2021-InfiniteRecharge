package frc.robot;
//imports
import frc.robot.subsystems.PowerCell;
import frc.robot.subsystems.Spinner;
import frc.robot.components.IMU;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.AdvancedAuto;
import frc.robot.subsystems.AutoNav;
import frc.robot.subsystems.BasicAuto;
import frc.robot.components.CompressorControl;
import frc.robot.subsystems.AutoNav;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Logging{
    private AutoNav autoNav;
    private IMU imu;
    private CompressorControl compressorControl;
    private Chassis driveTrain;
    private PowerCell powerCell;
    private Climber climber;
    private Spinner spinner;
    private AdvancedAuto advancedAuto;
    private BasicAuto basicAuto;
    
    //Master Booleans
    private boolean printLogs = true; //master boolean disable to kill all print logs
    private boolean smartDasboardLogs = false; //master boolean disable to kill all smart dashboard logs

    //Class Booleans
    private boolean printVisionLogs = false;
    private boolean printCompressorLogs = false;
    private boolean printImuLogs = false;
    private boolean printVisionAutoLogs = true;
    private boolean smartDashboardImuLogs = false;

    public void init(IMU Imu, Chassis DriveTrain, PowerCell powerCelL, Climber climbeR, Spinner spinneR, CompressorControl compressorControL, AdvancedAuto advancedAutO, BasicAuto basicAutO, AutoNav autoNaV){
        autoNav = autoNaV;
        imu = Imu;
        driveTrain = DriveTrain;
        powerCell = powerCelL;
        climber = climbeR;
        spinner = spinneR;
        compressorControl = compressorControL;
        advancedAuto = advancedAutO;
        basicAuto = basicAutO; 
    }
    public void printLogs(){
        if(printLogs && printCompressorLogs){
            System.out.println("Compressor Logs");
            System.out.println(compressorControl.isCompressorOn);
            System.out.println();
        }
        if(printLogs && printImuLogs){
            System.out.println("IMU Logs");
            System.out.println("Yaw" + imu.yaw);
            System.out.println("Pitch" + imu.pitch);
            System.out.println("Roll" + imu.roll);
            System.out.println();
        }
        if(printLogs && printVisionAutoLogs){
            
        }
    }
    public void smartDashboardLogs(){
        if(smartDasboardLogs && smartDashboardImuLogs){
            SmartDashboard.putNumber("Imu yaw", imu.yaw);
        }
    }
}