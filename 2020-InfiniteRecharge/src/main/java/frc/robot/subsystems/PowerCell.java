package frc.robot.subsystems;

import frc.robot.OperatorInterface;
import frc.robot.Robot;
import frc.robot.Wiring;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.revrobotics.CANEncoder;


 //intake has to go double the speed the shooter goes
public class PowerCell implements Loggable{
    //pid values
    private final int TIMEOUT = 0;
    private final double cLR = 0.1;
    @Log
    private double intake_kP = 0.0005; // P-gain = (.1*1023)/(155) = 0.66 - (350 is average error)
    private double intake_kD = 0;
    private double intake_kI = 0;
    private double shooter_kF = 0; //F-gain = (100% X 1023) / 7350 F-gain = 0.139183673 - (7350 is max speed)
    @Log
    private double shooter_kP = 5; // P-gain = (.1*1023)/(155) = 0.66 - (350 is average error)
    private double shooter_kD = 0;
    private double shooter_kI = 0;
    @Log
    private double storage_kP = 5; // P-gain = (.1*1023)/(155) = 0.66 - (350 is average error)
    private double storage_kD = 0;
    private double storage_kI = 0;
    private double storage_kF = 0;
    
    
    //motors 
    private TalonSRX storageMotor;
    private CANSparkMax intakeMotor;
    private TalonFX shooter;
    private OperatorInterface oi;
    private CANPIDController intakeMotorPID;   
    private CANEncoder intakeEncoder;
    @Log
    private double shooterRpms;
    @Log
    private double intakeRpms;
    @Log
    private double storageRpms; 
    @Log.Graph
    private double intakeMotorOutputCurrent;
    @Log.Graph
    private double intakevelocity;

    

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
        storageMotor.config_kP(0, kP);
        storageMotor.config_kD(0, kD);
        storageMotor.config_kI(0, kI);
        storageRpms = Rpms;
        
         storage_kP = kP;
       
        
    }

    public void init(){
        //Constructors
        oi = Robot.oi;
        shooter = new TalonFX(Wiring.shooterMotor);
        intakeMotor = new CANSparkMax(Wiring.intakeMotor, MotorType.kBrushless);
        intakeMotorPID = intakeMotor.getPIDController();
        storageMotor = new TalonSRX(Wiring.storageMotor);
        intakeEncoder = new CANEncoder(intakeMotor);

        //Intake Motor Config
        intakeMotorPID.setP(intake_kP);
        intakeMotorPID.setI(intake_kI);
        intakeMotorPID.setD(intake_kD);
        intakeMotorPID.setOutputRange(-1, 1);
        intakeMotorPID.setReference(0, ControlType.kVelocity);
        intakeMotorPID.setFeedbackDevice(intakeEncoder);
        intakeMotor.setIdleMode(IdleMode.kBrake);
        intakeEncoder = intakeMotor.getEncoder();
        

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
    
        
        //Storage Motor Config
        storageMotor.set(ControlMode.Velocity, 0);	
        storageMotor.configClosedloopRamp(cLR, TIMEOUT);
        storageMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        storageMotor.config_kF(0, storage_kF);
        storageMotor.config_kP(0, storage_kP);
        storageMotor.config_kD(0, storage_kD);
        storageMotor.config_kI(0, storage_kI);
        storageMotor.configNominalOutputForward(0, TIMEOUT);
        storageMotor.configNominalOutputReverse(0, TIMEOUT);
        storageMotor.configPeakOutputForward(+1, TIMEOUT);
        storageMotor.configPeakOutputReverse(-1, TIMEOUT);
        storageMotor.setNeutralMode(NeutralMode.Coast);
 
    }
    public void stopStorage(){
        storageMotor.set(ControlMode.PercentOutput, 0);
    }
    public void stopIntake(){
        intakeMotorPID.setReference(0, ControlType.kDutyCycle);
    }
    public void stopShooter(){
        shooter.set(TalonFXControlMode.PercentOutput, 0);
    }
    public void storage(){
        if(oi.copilot.getRawButton(3)){
            storageMotor.set(ControlMode.PercentOutput, storageRpms);
        }
        else{
           stopStorage();
        }

    }
	public void intake() {
        if(oi.copilot.getRawButton(1))
        {
            intakevelocity = intakeEncoder.getVelocity();
            intakeMotorOutputCurrent = intakeMotor.getOutputCurrent();
            intakeMotorPID.setReference(intakeRpms, ControlType.kVelocity);
        }
        else
        {
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