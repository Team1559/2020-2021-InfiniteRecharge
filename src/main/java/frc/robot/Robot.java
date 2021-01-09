/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2020 FIRST. All Rights Reserved.                        */
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
import frc.robot.subsystems.BasicAuto;
import frc.robot.subsystems.Vision;
import frc.robot.subsystems.VisionAuto;
import frc.robot.components.CompressorControl;
import frc.robot.components.Limelight;
import frc.robot.components.MotionProfiling;
import frc.robot.components.DistSensor;
import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.Logging;

public class Robot extends TimedRobot{

  // feature flags booleans
  private String autoSelector = "advanced";//change this "basic" for basic auto, "advanced" for advanced auto, "vision" for vision auto, and "none" for no auto
  //                                                      -----                   --------                      ------                        ----
  private boolean loggingEnable = true;
  private boolean loggingInitialized = false;
  
  private boolean chassisEnable = true;
  private boolean chassisInitialized = false;

  private boolean ImuEnable = true;
  private boolean ImuInitialized = false;

  private boolean climberEnable = true;
  private boolean climberInitialized = false;

  private boolean compressorEnable = true;
  private boolean compressorInitialized = false;
  
  private boolean colorEnable = true;
  private boolean colorInitialized = false;
  
  private boolean advancedAutoEnable = false;
  private boolean advancedAutoInitialized = false;

  private boolean basicAutoEnable = false;
  private boolean basicAutoInitialize = false;

  private boolean powerCellEnable = true;
  private boolean powerCellInitialized = false;

  private boolean visionEnable = true;
  private boolean visionInitialized = false;
  
  private boolean visionAutoEnable = true;
  private boolean visionAutoInitialized = false;

  //constructors
  private Logging logging = new Logging();
  private AnalogInput ai = new AnalogInput(Wiring.distSensorPort);
  public Limelight limeLight = new Limelight();
  public Vision vision = new Vision();
  public Climber climber = new Climber();
  public PowerCell powerCell = new PowerCell();
  private CompressorControl compressorControl = new CompressorControl();
  public Spinner spinner = new Spinner();
  public Chassis driveTrain = new Chassis();
  public OperatorInterface oi = new OperatorInterface();
  private IMU imu = new IMU();
  private Camera camera1 = new Camera(0);
  private Camera camera2 = new Camera(1);
  public DistSensor distSensor = new DistSensor();
  private AdvancedAuto advancedAuto = new AdvancedAuto();
  private BasicAuto basicAuto = new BasicAuto();
  private VisionAuto visionAuto = new VisionAuto();
  private MotionProfiling motionProfiling = new MotionProfiling();
  
  private double targetDistance = 3;
  private boolean doReverse = true;

  @Override
  public void robotInit() {
     camera1.init();
     camera2.init();
  }

  @Override
  public void robotPeriodic()
  {

  }

  @Override
  public void autonomousInit()
  {
    //sets the feautre flag boolean for advanced auto
    if(autoSelector == "vision"){
      visionEnable = true;
      basicAutoEnable = false;
      advancedAutoEnable = false;
    }
    //sets the feautre flag boolean for advanced auto
    else if(autoSelector =="advanced"){
      visionEnable = false;
      basicAutoEnable = false;
      advancedAutoEnable = true;
    }

    //sets the feautre flag boolean for basic auto
    else if(autoSelector == "basic"){
      visionEnable = false;
      basicAutoEnable = true;
      advancedAutoEnable = false;
    }

    //runs the initialize method
    initialize();

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
    
    //vision auto
    if(autoSelector == "vision"){
      visionAuto.AutoPeriodic(driveTrain, powerCell, doReverse);
    }

    //advanced auto
    else if(autoSelector == "advanced" && imu.isYawValid()){
      advancedAuto.AutoPeriodic(driveTrain, powerCell, doReverse);
    }

    //basic auto
    else if(autoSelector == "basic" && imu.isYawValid()){
      basicAuto.AutoPeriodic(driveTrain, powerCell, doReverse);
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

    //Vision code
    if(colorEnable && colorInitialized && oi.copilot.getRawButton(Buttons.autoButton) && imu.isYawValid()){
      vision.driveToTarget(targetDistance);
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
    //advanced auto
    if(advancedAutoEnable && advancedAutoInitialized == false){
      advancedAuto.AutoInit(driveTrain,imu, powerCell);
      advancedAutoInitialized = true;
    }

    //basic auto
    if(basicAutoEnable && basicAutoInitialize == false){
      basicAuto.AutoInit(driveTrain);
      basicAutoInitialize = true;
    }

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
      driveTrain.Init(oi, imu, vision);
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

    //vision
    if(visionEnable && visionInitialized == false){
      limeLight.init();
      distSensor.init(ai);
      vision.init(imu, driveTrain, limeLight, distSensor);
      motionProfiling.init(driveTrain);
      visionInitialized = true;
    }
    if(visionAutoEnable && visionAutoInitialized == false && visionEnable && visionInitialized == true){
      visionAuto.AutoInit(driveTrain,imu, powerCell, vision);
      visionAutoInitialized = true;
    }
    
    //logging
    if(loggingEnable && loggingInitialized == false){
      logging.init(vision, imu, driveTrain, limeLight, distSensor, powerCell, climber, spinner, compressorControl, advancedAuto, basicAuto, visionAuto, motionProfiling);
      loggingInitialized = true;
    }
  }
}