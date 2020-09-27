/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
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
import frc.robot.subsystems.advancedAuto;
import frc.robot.subsystems.basicAuto;
import frc.robot.subsystems.Vision;
import frc.robot.components.CompressorControl;
import frc.robot.components.Limelight;

public class Robot extends TimedRobot{

  // feature flags booleans

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

  private boolean doAdvancedAuto = true;

  private boolean powerCellEnable = true;
  private boolean powerCellInitialized = false;

  private boolean visionEnable = true;
  private boolean visionInitialized = false;

  //constructors
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
  private advancedAuto advancedautO = new advancedAuto();
  private basicAuto basicautO = new basicAuto();
  
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
   
    initialize();
    if(ImuEnable && ImuInitialized){
      imu.zeroYaw();
    }
    if(doAdvancedAuto == true){
    advancedautO.AutoInit(driveTrain,imu, powerCell);
  }
  else{
    basicautO.AutoInit(driveTrain);
  }
}

  @Override
  public void autonomousPeriodic()
  {
    if(doAdvancedAuto == true){
    advancedautO.AutoPeriodic(driveTrain, powerCell);
    }
    else{
      basicautO.AutoPeriodic(driveTrain, powerCell);
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

      initialize();
     
  }
  @Override
  public void teleopPeriodic()
  {
    
    
    // if(ImuEnable && ImuInitialized){
    //   imu.getvalues();
    // }
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

    if(colorEnable && colorInitialized && oi.copilot.getRawButton(Buttons.autoButton) && imu.isYawValid()){
      vision.go();
    }
    else if(chassisEnable && chassisInitialized){    
      driveTrain.DriveSystem(oi.pilot);
    }
    

  }
 
  @Override
  public void testInit()
  {
  
    initialize();
  }

  @Override
  public void testPeriodic() 
  {
    if(chassisEnable && chassisInitialized){
      driveTrain.DriveSystem(oi.pilot);
    }
  }

  @Override
  public void disabledInit()
  {
    if(compressorEnable && compressorInitialized){
      compressorControl.disable();
    }
    if(chassisEnable && chassisInitialized)
    {
      driveTrain.disabled();
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

    
    if(colorEnable && colorInitialized == false)
    {
      spinner.init(oi);
      colorInitialized = true;
    }

    if(compressorEnable && compressorInitialized == false){
      compressorControl.init();
      compressorInitialized = true;
    }

    if(visionEnable && visionInitialized == false){
      limeLight.init();
      vision.init(imu, driveTrain, limeLight);
      visionInitialized = true;
    }
  }
}