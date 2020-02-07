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
  private String m_driveTrain;
  private final SendableChooser<String> m_driveChooser = new SendableChooser<>();

  private ShuffleboardTab driveTrainTab;

  // feature flags booleans
  private boolean camera1Enable = false;
  private boolean camera2Enable = false;
  private boolean chassisEnable = false;
  private boolean ImuEnable = false;
  private boolean climberEnable = false;
  private boolean compressorEnable = false;
  @Log
  private boolean robotInitialized = false;
  private boolean colorEnable = false;
  private boolean powerCellEnable = false;
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
  
  //oblog feature flags
  @Config.ToggleSwitch 
  public void Enable_Compressor(boolean enable){
    compressorEnable = enable;
  }

  @Config.ToggleSwitch 
  public void Enable_Climber(boolean enable){
    climberEnable = enable;
  }

  @Config.ToggleSwitch 
  public void Enable_PowerCell(boolean enable){
    powerCellEnable = enable;
  }

  @Config.ToggleSwitch 
  public void Enable_Camera1(boolean enable){
    camera1Enable = enable;
  }

  @Config.ToggleSwitch 
  public void Enable_Camera2(boolean enable){
    camera2Enable = enable;
  }

  @Config.ToggleSwitch
  public void Enable_IMU(boolean enable){
    ImuEnable = enable;
  }

  @Config.ToggleSwitch
  public void Enable_Chassis(boolean enable){
    chassisEnable = enable;
    System.out.println("Chassis Enable: " + chassisEnable);
    System.out.println("Enable: " + enable);
  }
  @Config.ToggleSwitch
  public void Enable_Cameras(boolean enable, boolean enable2){
    camera1Enable  = enable;
    camera2Enable = enable2;
  }
  @Config
  public void Enable_Color(boolean enable){
    colorEnable = enable;
  }
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
    if(ImuEnable){
      imu.zeroYaw();
    }
  }

  @Override
  public void autonomousPeriodic()
  {
    if(ImuEnable){
      imu.getvalues();
    }
    //Compressor
    if(compressorEnable && compressorControl.useCompressor){
      compressorControl.enable();
    }
  }
  

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
    if(chassisEnable){    
    driveTrain.DriveSystem(oi.pilot);
    }
    
    if(ImuEnable){
      imu.getvalues();
    }
    if(climberEnable){
      climber.drive();
    }
    if(powerCellEnable){
      powerCell.intake();
      powerCell.shoot();
      powerCell.storage();
      powerCell.feeder();
  }
      //Compressor
     if(compressorEnable && compressorControl.useCompressor){
      compressorControl.enable();
    }
    else if(compressorEnable){
      compressorControl.disable();
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
    if(chassisEnable){
      driveTrain.DriveSystem(oi.pilot,m_driveTrain);
    }
  }

  @Override
  public void disabledInit()
  {
    if(compressorEnable){
      compressorControl.disable();
    }
  }

  @Override
  public void disabledPeriodic()
  {

  }
  
  public void initialize()
  {

    if(ImuEnable && robotInitialized != false)
    {
      imu.init();
    }
  
  if(powerCellEnable && robotInitialized != false){
      powerCell.init(oi);
    }

    System.out.println("Initilied");
    if(chassisEnable && robotInitialized != false)
    {
      driveTrain.Init(oi);
    }
    System.out.println("ChassisEnable: " + chassisEnable);

    if(climberEnable && robotInitialized == false){
      climber.ClimberInit(oi);
    }

    if(camera1Enable && robotInitialized == false){
      camera1.init();
    }
    
    if(camera2Enable && robotInitialized == false){
      camera2.init();
    }
    if(colorEnable && robotInitialized == false)
    {
      spinner.init();
    }
    if(compressorEnable && robotInitialized == false){
      compressorControl.init();
    }
    robotInitialized = true;
  }
}
