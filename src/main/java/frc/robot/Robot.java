/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2021 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.subsystems.PowerCell;

import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Spinner;
import frc.robot.components.Camera;
import frc.robot.components.IMU;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.AdvancedAuto;
import frc.robot.subsystems.BasicAuto;
import frc.robot.components.CompressorControl;
import frc.robot.BORROWINGDriverControls;
import frc.robot.RobotContainer;
import frc.robot.Logging;
//import frc.robot.Test;
import frc.robot.*;

public class Robot extends TimedRobot{
//these are changable
  private String AutoNavPathSelector = "example";// barrel racing is "barrel" , slolum is "slolum", and bounce path is "bounce" and example is "example" 
  private boolean doReverse = true;
  private String autoSelector = "learning";// learning is the good one 
  //in order to switch auto modes change what is in the quotes "basic" for basic auto, "advanced" for advanced auto, "autoNav" for bad auto, learning for machine learning auto, and "none" for no auto
 
  // feature flags booleans
  //change these to disable unused subsystems.
  private boolean loggingEnable = false;
  private boolean chassisEnable = true;
  private boolean ImuEnable = true;
  private boolean climberEnable = true;
  private boolean compressorEnable = true;
  private boolean colorEnable = true;
  private boolean powerCellEnable = true;

  //DON'T TOUCH THESE, they are used to determine if a specifiec subsystem has been initialised as to not call it's init method more than once, causing errors.
  private boolean loggingInitialized = false;
  private boolean chassisInitialized = false;
  private boolean ImuInitialized = false;
  private boolean climberInitialized = false;
  private boolean compressorInitialized = false;
  private boolean colorInitialized = false;
  private boolean powerCellInitialized = false;
  private RobotContainer m_robotContainer;
  private Command m_autonomousCommand;
  //constructors
  //private RobotContainer robotContainer = new RobotContainer();
  private Test t1 = new Test();
  //private Test2 t2 = new Test2();
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
  private BORROWINGDriverControls bdc = new BORROWINGDriverControls();
  private Pose2d pose;
  public int counter = 0;
  
  private double rightSpeed[] = t1.generated_rightEncoderPositions;
  private double leftSpeed[]= t1.generated_leftEncoderPositions;
  private double forwardSpeedLog[]= t1.generated_Velocities;
  private double sideSpeedLog[]= t1.generated_Rotations;
  
  private boolean teachTheAI = true;
  private int loopCounter = 0;

  @Override
  public void robotInit() {
  camera1.init();
  camera2.init();
  m_robotContainer = new RobotContainer(driveTrain);
  }

  @Override
  public void robotPeriodic()
  {
    CommandScheduler.getInstance().run();

  }

  @Override
  public void autonomousInit()
  {
    loopCounter = 0;
    initialize();
    if(teachTheAI){
      bdc.init();
      driveTrain.initOdometry();
    }
    if(chassisEnable && chassisInitialized){
      driveTrain.autoInit(0.0002);
    }
    if(autoSelector == "learning"){
    counter = 0;
    }
    //sets the feautre flag boolean for advanced auto
    if(autoSelector == "test"){
      driveTrain.initOdometry();
      // robotContainer.init(driveTrain);
      // autoNav.AutoInit(driveTrain, AutoNavPathSelector, robotContainer);
    
      m_autonomousCommand = m_robotContainer.getAutonomousCommand();

      /*
       * String autoSelected = SmartDashboard.getString("Auto Selector",
       * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
       * = new MyAutoCommand(); break; case "Default Auto": default:
       * autonomousCommand = new ExampleCommand(); break; }
       */
  
      // schedule the autonomous command (example)
      if (m_autonomousCommand != null) {
        m_autonomousCommand.schedule();
      }
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
      logging.Log();
    }
    

    //autoNav
    if(autoSelector == "test"){
      CommandScheduler.getInstance().run();
      pose = driveTrain.updateOdometry();
    }

    else if(autoSelector == "learning"){
      if(loopCounter > 60 && counter < leftSpeed.length){
        driveTrain.move(leftSpeed[counter], rightSpeed[counter]);
        counter++;
      }
      else{
        driveTrain.move(0, 0);
      }
      
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
    if(teachTheAI){
      bdc.periodic(driveTrain.loggingForwardSpeed, driveTrain.loggingSideSpeed, driveTrain.lEncoder.getPosition(), driveTrain.lEncoder.getVelocity(), driveTrain.rEncoder.getPosition(), driveTrain.rEncoder.getVelocity());
    }
    loopCounter ++;
  }
  
  @Override
  public void teleopInit()
  {
    loopCounter = 0;
    initialize();
    if(teachTheAI){
      bdc.init();
      driveTrain.initOdometry();
    }
    if(chassisEnable && chassisInitialized){
      driveTrain.teleopInit();
    }
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    //runs the initalize method
    
     
  }
  @Override
  public void teleopPeriodic()
  {
    
    //logging
    if(loggingEnable && loggingInitialized){
      logging.Log();
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
    if(chassisEnable && chassisInitialized){    
      driveTrain.DriveSystem(oi.pilot);
    }
    if(teachTheAI){
      bdc.periodic(driveTrain.forwardSpeed, driveTrain.sideSpeed, driveTrain.lEncoder.getPosition(), driveTrain.lEncoder.getVelocity(), driveTrain.rEncoder.getPosition(), driveTrain.rEncoder.getVelocity());
    }
    loopCounter++;
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
    if(teachTheAI){
      bdc.printAll();
    }
    //System.out.println(loopCounter);
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
      imu.zeroYaw();
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
      logging.init(imu, driveTrain, powerCell, climber, spinner, compressorControl, advancedAuto, basicAuto, m_robotContainer);
      loggingInitialized = true;
    }
  }
}