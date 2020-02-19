/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import frc.robot.subsystems.PowerCell;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Spinner;
import frc.robot.components.Camera;
import frc.robot.components.IMU;
import frc.robot.subsystems.Auto;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Climber;
import frc.robot.components.CompressorControl;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;
import io.github.oblarg.oblog.annotations.Log.Logs;

public class Robot extends TimedRobot implements Loggable {

  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private static final String kTankDrive = "Tank Drive";
  private static final String kArcadeDrive = "Arcade Drive";
  private static final String kCurvatureDrive = "Curvature Drive";
  private static final String kShuffleDrive = "Shuffle Drive Individual";
  private static final String kShuffleDriveGroups = "Shuffle Drive Control Groups";
  private static final String kScottDrive = "Scott Drive";
  private String m_driveTrain;
  private final SendableChooser<String> m_driveChooser = new SendableChooser<>();

  private ShuffleboardTab driveTrainTab;

  // feature flags booleans
  private boolean camera1Enable = false;
  private boolean camera1Initialized = false;
  private boolean camera2Enable = false;
  private boolean camera2Initialized = false;

  private boolean chassisEnable = false;
  private boolean chassisInitialized = false;

  private boolean ImuEnable = false;
  private boolean ImuInitialized = false;

  private boolean climberEnable = false;
  private boolean climberInitialized = false;

  private boolean compressorEnable = false;
  private boolean compressorInitialized = false;
  
  private boolean colorEnable = false;
  private boolean colorInitialized = false;

  private boolean powerCellEnable = false;
  private boolean powerCellInitialized = false;

  private boolean autoEnable = false;
  private boolean autoInitialized = false;

  //constructors
  public Climber climber = new Climber();
  public PowerCell powerCell = new PowerCell();
  private CompressorControl compressorControl = new CompressorControl();
  public Spinner spinner = new Spinner();
  public Chassis driveTrain = new Chassis();
  public OperatorInterface oi = new OperatorInterface();
  private IMU imu = new IMU();
  private Camera camera1 = new Camera(0);
  private Camera camera2 = new Camera(1);
  private Auto auto = new Auto();
  
  //oblog feature flags
  @Config.ToggleSwitch
  public void Enable_Auto(boolean enable){
  autoEnable = enable;

  }

  @Config.ToggleSwitch(defaultValue = true)
  public void Enable_Compressor(boolean enable){
    compressorEnable = enable;
  }

  @Config.ToggleSwitch(defaultValue = true)
  public void Enable_Climber(boolean enable){
    climberEnable = enable;
  }

  @Config.ToggleSwitch(defaultValue = true) 
  public void Enable_PowerCell(boolean enable){
    powerCellEnable = enable;
  }

  @Config.ToggleSwitch(defaultValue = true)
  public void Enable_Camera1(boolean enable){
    camera1Enable = enable;
  }

  @Config.ToggleSwitch(defaultValue = true) 
  public void Enable_Camera2(boolean enable){
    camera2Enable = enable;
  }

  @Config.ToggleSwitch(defaultValue = true)
  public void Enable_IMU(boolean enable){
    ImuEnable = enable;
  }

  @Config.ToggleSwitch(defaultValue = true)
  public void Enable_Chassis(boolean enable){
    chassisEnable = enable;
    System.out.println("Chassis Enable: " + chassisEnable);
    System.out.println("Enable: " + enable);
  }
  @Config.ToggleSwitch(defaultValue = true)
  public void Enable_Cameras(boolean enable, boolean enable2){
    camera1Enable  = enable;
    camera2Enable = enable2;
  }
  @Config.ToggleSwitch(defaultValue = true)
  public void Enable_Color(boolean enable){
    colorEnable = enable;
  }
  @Log
  private String autoStatice = "Not Initialised";
  @Override
  public void robotInit() {
  Logger.configureLoggingAndConfig(this, false); 
    driveTrainTab = Shuffleboard.getTab("Drive Train"); //The Shuffleboard Tab for all Drive Train related stuff
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    m_driveChooser.setDefaultOption("Tank Drive",kTankDrive); //A Drive Train option
    m_driveChooser.addOption("Arcade Drive", kArcadeDrive); //A Drive Train option
    m_driveChooser.addOption("Curvature Drive", kCurvatureDrive); //A Drive Train option
    m_driveChooser.addOption("Shuffle Drive Individual", kShuffleDrive); //A Drive Train option
    m_driveChooser.addOption("Shuffle Drive Control Groups", kShuffleDriveGroups); //A Drive Train option
    m_driveChooser.addOption("Scott Drive", kScottDrive); //Scott's Drive Train Option
    driveTrainTab.add("Drive Train Choices", m_driveChooser); //Allows you to pick a Drive Train option through Shuffleboard   
}

  @Override
  public void robotPeriodic()
  {
    Logger.updateEntries();
  }

  @Override
  public void autonomousInit()
  {
    
    
    m_autoSelected = m_chooser.getSelected();
    m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    initialize();
    if(ImuEnable && ImuInitialized){
      imu.zeroYaw();
    }
  }

  @Override
  public void autonomousPeriodic()
  {
    if(autoInitialized && autoEnable){
      auto.AutoPeriodic(driveTrain, powerCell);
    }

    if(ImuEnable && ImuInitialized){
      imu.getvalues();
    }
    //Compressor
    if(compressorEnable && compressorInitialized){
      compressorControl.run();
    }
  }
  

  @Override
  public void teleopInit()
  {
    m_driveTrain = m_driveChooser.getSelected();
      initialize();
     
  }
  @Override
  public void teleopPeriodic()
  {
    if(chassisEnable && chassisInitialized){    
    driveTrain.DriveSystem(oi.pilot,m_driveTrain);
    }
    
    if(ImuEnable && ImuInitialized){
      imu.getvalues();
    }
    if(climberEnable && climberInitialized){
      climber.drive();
    }
    if(powerCellEnable && powerCellInitialized){
      powerCell.go();
  }
      //Compressor
     if(compressorEnable && compressorInitialized){
      compressorControl.run();
    }
    //All spinner logic is in Spinner.java
    if(colorEnable && colorInitialized){
      spinner.spin(compressorEnable);
    }
    

  }
 
  @Override
  public void testInit()
  {
    m_driveTrain = m_driveChooser.getSelected();
    initialize();
  }

  @Override
  public void testPeriodic() 
  {
    if(chassisEnable && chassisInitialized){
      driveTrain.DriveSystem(oi.pilot,m_driveTrain);
    }
  }

  @Override
  public void disabledInit()
  {
    if(compressorEnable && compressorInitialized){
      compressorControl.disable();
    }
  }

  @Override
  public void disabledPeriodic()
  {

  }
  
  public void initialize()
  {
    

    if(ImuEnable && ImuInitialized == false)
    {
      imu.init();
      ImuInitialized = true;
    }
  
  if(powerCellEnable && powerCellInitialized == false){
      powerCell.init(oi);
      powerCellInitialized = true;
    }
    if(chassisEnable && chassisInitialized == false)
    {
      driveTrain.Init(oi, imu);
      chassisInitialized = true;
    }
    

    if(climberEnable && climberInitialized == false){
      climber.ClimberInit(oi);
      climberInitialized = true;
    }

    if(camera1Enable && camera1Initialized == false){
      camera1.init();
      camera1Initialized = true;
    }
    
    if(camera2Enable && camera2Initialized == false){
      camera2.init();
      camera2Initialized = true;
    }
    if(colorEnable && colorInitialized == false)
    {
      spinner.init(oi);
      colorInitialized = true;
    }
    if(compressorEnable && compressorInitialized == false){
      compressorControl.init();
      compressorInitialized = true;
    }
    if(autoEnable && autoInitialized == false && ImuInitialized && chassisInitialized){
      auto.AutoInit(driveTrain);
      autoStatice = "Auto initialised";
      autoInitialized = true;
    }
    else if(ImuInitialized == false || chassisInitialized == false){
      autoStatice = "Either the IMU or the drive train aren't initialized";
    }
  }
}