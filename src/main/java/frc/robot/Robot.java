/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2021 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.subsystems.PowerCell;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.subsystems.Spinner;
import frc.robot.components.Camera;
import frc.robot.components.IMU;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.AdvancedAuto;
import frc.robot.subsystems.AutoNav;
import frc.robot.subsystems.BasicAuto;
import frc.robot.components.CompressorControl;
import frc.robot.RobotContainer;
import frc.robot.subsystems.AutoNav;
import frc.robot.Logging;

public class Robot extends TimedRobot{
//these are changable
  private String AutoNavPathSelector = "example";// barrel racing is "barrel" , slolum is "slolum", and bounce path is "bounce" and example is "example" 
  private boolean doReverse = true;
  private String autoSelector = "autoNav"; 
  //in order to switch auto modes change what is in the quotes "basic" for basic auto, "advanced" for advanced auto, "autoNav" for vision auto, and "none" for no auto
 
  // feature flags booleans
  //change these to disable unused subsystems.
  private boolean loggingEnable = false;
  private boolean chassisEnable = true;
  private boolean ImuEnable = true;
  private boolean climberEnable = false;
  private boolean compressorEnable = false;// change this back
  private boolean colorEnable = false;
  private boolean powerCellEnable = true;

  //DON'T TOUCH THESE, they are used to determine if the specifies subsystem has been initialised as to not call it's init method more than once, causiing errors.
  private boolean loggingInitialized = false;
  private boolean chassisInitialized = false;
  private boolean ImuInitialized = false;
  private boolean climberInitialized = false;
  private boolean compressorInitialized = false;
  private boolean colorInitialized = false;
  private boolean powerCellInitialized = false;
  private RobotContainer m_robotContainer;

  //constructors
  private RobotContainer robotContainer = new RobotContainer();
  private AutoNav autoNav = new AutoNav();
  private Logging logging = new Logging();
  public Climber climber = new Climber();
  public PowerCell powerCell = new PowerCell();
  private CompressorControl compressorControl = new CompressorControl();
  public Spinner spinner = new Spinner();
  public Chassis driveTrain = new Chassis();
  public OperatorInterface oi = new OperatorInterface();
  private IMU imu = new IMU();
  private Camera camera1 = new Camera(0);
  private Camera camera2 = new Camera(1);
  private AdvancedAuto advancedAuto = new AdvancedAuto();
  private BasicAuto basicAuto = new BasicAuto();

  


  @Override
  public void robotInit() {
  camera1.init();
  camera2.init();
  m_robotContainer = new RobotContainer();
  }

  @Override
  public void robotPeriodic()
  {

  }

  @Override
  public void autonomousInit()
  {
    initialize();
    //sets the feautre flag boolean for advanced auto
    if(autoSelector == "autoNav"){
      robotContainer.init(driveTrain);
      autoNav.AutoInit(driveTrain, AutoNavPathSelector, robotContainer);
    }
    //sets the feautre flag boolean for advanced auto
    else if(autoSelector =="advanced"){
      advancedAuto.AutoInit(driveTrain, imu, powerCell);
    }

    //sets the feautre flag boolean for basic auto
    else if(autoSelector == "basic"){
      basicAuto.AutoInit(driveTrain);
    }

    //runs the initialize method
    

    //zeros the imu
    if(ImuEnable && ImuInitialized){
      imu.zeroYaw();
    }
  }

  @Override
  public void autonomousPeriodic()
  {
    //logging
    if(loggingEnable && loggingInitialized){
      logging.printLogs();
      logging.smartDashboardLogs();
    }
    
    //autoNav
    // if(autoSelector == "autoNav"){
    //   autoNav.AutoPeriodic(driveTrain, powerCell, doReverse);
    // }

    //advanced auto
    else if(autoSelector == "advanced" && imu.isYawValid()){
      advancedAuto.AutoPeriodic(driveTrain, powerCell, doReverse);
    }

    //basic auto
    else if(autoSelector == "basic" && imu.isYawValid()){
      basicAuto.AutoPeriodic(driveTrain, powerCell, doReverse);
    }
    //autoNav
    else if(autoSelector == "autoNav" && imu.isYawValid()){
      autoNav.AutoPeriodic(driveTrain, powerCell);
    }

    //stop the drivetrain
    else{
      driveTrain.move(0, 0);
    }

    //imu
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

    if (autoNav.m_autonomousCommand != null) {
      autoNav.m_autonomousCommand.cancel();
    }
    //runs the initalize method
    initialize();
     
  }
  @Override
  public void teleopPeriodic()
  {
    //logging
    if(loggingEnable && loggingInitialized){
      logging.smartDashboardLogs();
      logging.printLogs();
    }

    //imu
    if(ImuEnable && ImuInitialized){
      imu.getvalues();
    }

    //climber
    if(climberEnable && climberInitialized){
      climber.drive();
    }

    //powercell
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

    //Chassis code
    else if(chassisEnable && chassisInitialized){    
      driveTrain.DriveSystem(oi.pilot);
    }
  }
 
  @Override
  public void testInit()
  {

  }

  @Override
  public void testPeriodic() 
  {

  }

  @Override
  public void disabledInit()
  {
    //turns off the compresser
    if(compressorEnable && compressorInitialized){
      compressorControl.disable();
    }

    //sets the chassis to coast mode for easier transport
    if(chassisEnable && chassisInitialized){
      driveTrain.disabled();
    }
  }

  @Override
  public void disabledPeriodic()
  {
    
  }
  
  public void initialize()
  {
    
    //imu
    if(ImuEnable && ImuInitialized == false){
      imu.init();
      ImuInitialized = true;
    }

    //powercell
    if(powerCellEnable && powerCellInitialized == false){
      powerCell.init(oi);
      powerCellInitialized = true;
    }

    //chassis
    if(chassisEnable && chassisInitialized == false){
      driveTrain.Init(oi, imu);
      chassisInitialized = true;
    }

    //climber
    if(climberEnable && climberInitialized == false){
      climber.ClimberInit(oi);
      climberInitialized = true;
    }
    
    //spinner
    if(colorEnable && colorInitialized == false){
      spinner.init(oi);
      colorInitialized = true;
    }
    
    //compressor
    if(compressorEnable && compressorInitialized == false){
      compressorControl.init();
      compressorInitialized = true;
    }
    
    //logging
    if(loggingEnable && loggingInitialized == false){
      logging.init(imu, driveTrain, powerCell, climber, spinner, compressorControl, advancedAuto, basicAuto, autoNav, robotContainer);
      loggingInitialized = true;
    }
  }
}