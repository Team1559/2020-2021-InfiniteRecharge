/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.components.IMU;
import frc.robot.components.Camera;
import frc.robot.subsystems.PowerCell;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Climber;
import io.github.oblarg.oblog.*;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private Chassis driveTrain;
  public static OperatorInterface oi;

  private static final String kTankDrive = "Tank Drive";
  private static final String kArcadeDrive = "Arcade Drive";
  private static final String kCurvatureDrive = "Curvature Drive";
  private static final String kShuffleDrive = "Shuffle Drive Individual";
  private static final String kShuffleDriveGroups = "Shuffle Drive Control Groups";
  private String m_driveTrain;
  private final SendableChooser<String> m_driveChooser = new SendableChooser<>();

  private ShuffleboardTab driveTrainTab;
  private IMU imu;
  private Camera camera1;
  private Camera camera2;
  private boolean camera1Enable = false;
  private boolean camera2Enable = false;
  private boolean chassisEnable = false;
  private boolean ImuEnable = false;
  private Climber climber;

  @Log
  private boolean robotInitialized = false;
  private boolean powerCellEnable = false;
  private PowerCell powerCell;
  
  @Config 
  public void Enable_PowerCell(boolean enable){
    powerCellEnable = enable;
  }

  @Config 
  public void Enable_Camera1(boolean enable){
    camera1Enable = enable;
  }

  @Config 
  public void Enable_Camera2(boolean enable){
    camera2Enable = enable;
  }

  @Config
  public void Enable_IMU(boolean enable){
    ImuEnable = enable;
  }

  @Config.ToggleButton
  public void Enable_Chassis(boolean enable){
    chassisEnable = enable;
    System.out.println("Chassis Enable: " + chassisEnable);
    System.out.println("Enable: " + enable);
  }

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
  Logger.configureLoggingAndConfig(this, false);

    
    oi = new OperatorInterface();
    powerCell = new PowerCell();
    driveTrain = new Chassis();
    driveTrainTab = Shuffleboard.getTab("Drive Train"); //The Shuffleboard Tab for all Drive Train related stuff

    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);


    

    

    m_driveChooser.setDefaultOption("Tank Drive",kTankDrive); //A Drive Train option
    m_driveChooser.addOption("Arcade Drive", kArcadeDrive); //A Drive Train option
    m_driveChooser.addOption("Curvature Drive", kCurvatureDrive); //A Drive Train option
    m_driveChooser.addOption("Shuffle Drive Individual", kShuffleDrive); //A Drive Train option
    m_driveChooser.addOption("Shuffle Drive Control Groups", kShuffleDriveGroups); //A Drive Train option
    driveTrainTab.add("Drive Train Choices", m_driveChooser); //Allows you to pick a Drive Train option through Shuffleboard

    imu = new IMU();
    camera1 = new Camera(0);
    camera2 = new Camera(1);
    climber = new Climber();
    
}


  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    Logger.updateEntries();
    
  }
    
  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit()
  {
    m_autoSelected = m_chooser.getSelected();
    m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    initialize();
    
    
    
    if(ImuEnable){
        imu.zeroYaw();
    }
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic()
  {
    if(ImuEnable){
      imu.getvalues();
    }
  }
    
    
  
  /**
   * This function is called periodically during operator control.
   */
  
  @Override
  public void teleopInit()
  {
    if(robotInitialized == false){
      initialize();
    }
  }
  @Override
  public void teleopPeriodic()
  {
    driveTrain.DriveSystem(oi.pilot);
    if(ImuEnable){
      imu.getvalues();
    }

    
    
    if(powerCellEnable){
      powerCell.intake();
      powerCell.shoot();
      powerCell.storage();//for testing only will be changed
    }
  }



  
  /**
   * This function is called periodically during test mode.
   */

  @Override
  public void testInit()
  {
    m_driveTrain = m_driveChooser.getSelected();
    initialize();
  }

  @Override
  public void testPeriodic() 
  {
    if(chassisEnable){
      driveTrain.DriveSystem(oi.pilot,m_driveTrain);
    }
  }

  @Override
  public void disabledInit()
  {

  }
  @Override
  public void disabledPeriodic()
  {

  }
  public void initialize()
  {
    robotInitialized = true;
    if(ImuEnable){
      imu.init();
    }
  
  if(powerCellEnable){
      powerCell.init();
    }

    System.out.println("Initilied");
    if(chassisEnable)
    {
      driveTrain.Init();
    }
    System.out.println("ChassisEnable: " + chassisEnable);

    if(camera1Enable){
      camera1.init();
    }
    if(camera2Enable){
      camera2.init();
    }

  }
}
