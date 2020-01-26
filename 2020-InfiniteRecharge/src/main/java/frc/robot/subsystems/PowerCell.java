package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.ControlType;

import frc.robot.OperatorInterface;
import frc.robot.Robot;
import frc.robot.Wiring;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;


 //intake has to go double the speed the shooter goes
public class PowerCell implements Loggable{
    //pid values
    private final int TIMEOUT = 0;
    private final double cLR = 0.1;
    @Log
    private double intake_kP = 0.00005;//5e-5 // P-gain = (.1*1023)/(155) = 0.66 - (350 is average error)
    private double intake_kD = 0;
    @Log
    private double intake_kI = 0.000001;//1e-6
    private double shooter_kF = 0; //F-gain = (100% X 1023) / 7350 F-gain = 0.139183673 - (7350 is max speed)
    @Log
    private double shooter_kP = 5; // P-gain = (.1*1023)/(155) = 0.66 - (350 is average error)
    private double shooter_kD = 0;
    @Log
    private double shooter_kI = 0.000001;//1e-6
    @Log
    private double storage_kP = 5; // P-gain = (.1*1023)/(155) = 0.66 - (350 is average error)
    private double storage_kD = 0;
    @Log
    private double storage_kI = 0;
    private double storage_kF = 0;
    @Log
    private double feeder_kP = 0;//5e-5 // P-gain = (.1*1023)/(155) = 0.66 - (350 is average error)
    private double feeder_kD = 0;
    @Log
    private double feeder_kI = 0;//1e-6
    private double feeder_kF = 0;
    
    
    //motors 
    private TalonSRX storageMotor1;
    private TalonSRX storageMotor2;
    private TalonSRX feederMotor;
    private CANSparkMax intakeMotor;
    private TalonFX shooter;
    private OperatorInterface oi;
    private CANPIDController intakeMotorPID;   
    private CANEncoder intakeEncoder;
    @Log
    private double shooterRpms;
    @Log
    private double intakeRpms = 60;
    @Log
    private double storageRpms; 
    @Log.Graph
    private double intakeMotorOutputCurrent;
    @Log.Graph
    private double intakevelocity;
    @Log.Graph
    private double motortemp;

    

    @Config
    private void Intake_PID(double kP, double kI, double kD, double Rpms){
        intakeMotorPID.setP(kP);
        intakeMotorPID.setI(kI);
        intakeMotorPID.setD(kD);
        intakeRpms = Rpms;

         intake_kP = kP;
        
    }

    @Config
    private void Shooter_PID(double kP, double kI, double kD, double Rpms){
        shooter.config_kP(0, kP);
        shooter.config_kD(0, kD);
        shooter.config_kI(0, kI);
        shooterRpms = Rpms;
        
         shooter_kP = kP;
       
        
    }
    
    @Config
    private void Storage_PID(double kP, double kI, double kD, double Rpms){
        storageMotor1.config_kP(0, kP);
        storageMotor1.config_kD(0, kD);
        storageMotor1.config_kI(0, kI);
        storageRpms = Rpms;
        
         storage_kP = kP;
       
        
    }

    public void init(){                                                                                                                                                                                                                                                                                                                                                        
        //Constructors
        oi = Robot.oi;
        shooter = new TalonFX(Wiring.shooterMotor);
        intakeMotor = new CANSparkMax(Wiring.intakeMotor, MotorType.kBrushless);
        intakeMotorPID = intakeMotor.getPIDController();
        storageMotor1 = new TalonSRX(Wiring.storageMotor1);
        storageMotor2 = new TalonSRX(Wiring.storageMotor2);
        intakeEncoder = new CANEncoder(intakeMotor);

        //Intake Motor Config
        intakeEncoder = intakeMotor.getEncoder();
        intakeMotorPID.setP(intake_kP);
        intakeMotorPID.setI(intake_kI);
        intakeMotorPID.setD(intake_kD);
        intakeMotorPID.setFF(0.0);
        intakeMotorPID.setIMaxAccum(100, 0);
        intakeMotorPID.setIZone(20, 0);
        intakeMotorPID.setOutputRange(-1, 1);
        intakeMotorPID.setReference(0, ControlType.kVelocity);
        intakeMotorPID.setFeedbackDevice(intakeEncoder);
        intakeMotor.setIdleMode(IdleMode.kBrake);
        intakeMotor.setSmartCurrentLimit(40, 25, 0);
        intakeMotor.setClosedLoopRampRate(cLR);
        

        //Feeder Motor Config
        feederMotor.set(ControlMode.Velocity, 0);	
        feederMotor.configClosedloopRamp(cLR, TIMEOUT);
        feederMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        feederMotor.config_kF(0, feeder_kF);
        feederMotor.config_kP(0, feeder_kP);
        feederMotor.config_kD(0, feeder_kD);
        feederMotor.config_kI(0, feeder_kI);
        feederMotor.configNominalOutputForward(0, TIMEOUT);
        feederMotor.configNominalOutputReverse(0, TIMEOUT);
        feederMotor.configPeakOutputForward(+1, TIMEOUT);
        feederMotor.configPeakOutputReverse(-1, TIMEOUT);
        feederMotor.setNeutralMode(NeutralMode.Coast);

        //Shooter Motor Config
        shooter.set(TalonFXControlMode.Velocity, 0);	
        shooter.configClosedloopRamp(cLR, TIMEOUT);
        shooter.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        shooter.config_kF(0, shooter_kF);
        shooter.config_kP(0, shooter_kP);
        shooter.config_kD(0, shooter_kD);
        shooter.config_kI(0, shooter_kI);
        shooter.configNominalOutputForward(0, TIMEOUT);
        shooter.configNominalOutputReverse(0, TIMEOUT);
        shooter.configPeakOutputForward(+1, TIMEOUT);
        shooter.configPeakOutputReverse(-1, TIMEOUT);
        shooter.setNeutralMode(NeutralMode.Coast);
    
        
        //Storage Motor 1 Config
        storageMotor1.set(ControlMode.Velocity, 0);	
        storageMotor1.configClosedloopRamp(cLR, TIMEOUT);
        storageMotor1.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        storageMotor1.config_kF(0, storage_kF);
        storageMotor1.config_kP(0, storage_kP);
        storageMotor1.config_kD(0, storage_kD);
        storageMotor1.config_kI(0, storage_kI);
        storageMotor1.configNominalOutputForward(0, TIMEOUT);
        storageMotor1.configNominalOutputReverse(0, TIMEOUT);
        storageMotor1.configPeakOutputForward(+1, TIMEOUT);
        storageMotor1.configPeakOutputReverse(-1, TIMEOUT);
        storageMotor1.setNeutralMode(NeutralMode.Coast);


        //Storage Motor 2 Config
        storageMotor2.set(ControlMode.Velocity, 0);	
        storageMotor2.configClosedloopRamp(cLR, TIMEOUT);
        storageMotor2.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        storageMotor2.config_kF(0, storage_kF);
        storageMotor2.config_kP(0, storage_kP);
        storageMotor2.config_kD(0, storage_kD);
        storageMotor2.config_kI(0, storage_kI);
        storageMotor2.configNominalOutputForward(0, TIMEOUT);
        storageMotor2.configNominalOutputReverse(0, TIMEOUT);
        storageMotor2.configPeakOutputForward(+1, TIMEOUT);
        storageMotor2.configPeakOutputReverse(-1, TIMEOUT);
        storageMotor2.setNeutralMode(NeutralMode.Coast);
    }
    public void stopStorage(){
        storageMotor1.set(ControlMode.PercentOutput, 0);
        storageMotor2.set(ControlMode.PercentOutput, 0);
    }
    public void stopIntake(){
        intakeMotorPID.setReference(0, ControlType.kDutyCycle);
    }
    public void stopShooter(){
        shooter.set(TalonFXControlMode.PercentOutput, 0);
    }
    public void storage(){
        if(oi.copilot.getRawButton(3)){
            storageMotor1.set(ControlMode.PercentOutput, storageRpms);
            storageMotor2.set(ControlMode.PercentOutput, -storageRpms);
        }
        else{
           stopStorage();
        }

    }
	public void intake() {
        if(oi.copilot.getRawButton(1))
        {
            motortemp = intakeMotor.getMotorTemperature();
            intakevelocity = intakeEncoder.getVelocity();
            intakeMotorOutputCurrent = intakeMotor.getOutputCurrent();
            intakeMotorPID.setReference(intakeRpms, ControlType.kVelocity);
        }
        else
        {
            motortemp = intakeMotor.getMotorTemperature();
            intakevelocity = intakeEncoder.getVelocity();
            intakeMotorOutputCurrent = intakeMotor.getOutputCurrent();
           stopIntake();
        }

    }
    public void shoot(){
        if(oi.copilot.getRawButton(2)){
           
            shooter.set(ControlMode.Velocity, shooterRpms);
        }
        else{
            stopShooter();
        }
    }
}