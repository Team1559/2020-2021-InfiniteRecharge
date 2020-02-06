package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.OperatorInterface;
import frc.robot.Wiring;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;


 //intake has to go double the speed the shooter goes
public class PowerCell implements Loggable{
    private OperatorInterface oi;
    //pid values
    private final int TIMEOUT = 0;
    private final double cLR = 0.1;
    @Log
    private double intake_kP = 5;//5e-5 
    private double intake_kD = 0;
    @Log
    private double intake_kI = 0.00000;//1e-6
    private double intake_kF = 0;
    private double shooter_kF = 0; 
    @Log
    private double shooter_kP = 5; 
    private double shooter_kD = 0;
    @Log
    private double shooter_kI = 0.00000;//1e-6
    @Log
    private double storage_kP = 5; 
    private double storage_kD = 0;
    @Log
    private double storage_kI = 0;
    private double storage_kF = 0;
    @Log
    private double feeder_kP = 0;//5e-5 
    private double feeder_kD = 0;
    @Log
    private double feeder_kI = 0;//1e-6
    private double feeder_kF = 0;
    
    
    //motors 
    private TalonSRX storageMotorL;
    private TalonSRX storageMotorH;
    private TalonFX shooter;
    private TalonSRX intakeMotor;
    private TalonSRX feederMotor;
   
    @Log
    private double shooterRpms = 100;
    @Log
    private double intakeRpms = 0.4;
    @Log
    private double storageRpms = 0.8; //%output for now
    @Log
    private double feederRpms = 0.5;
    @Log 
    double feederIdleSpeed = 0.0;




	@Config
    private void Intake_PID(double kP, double kI, double kD, double Rpms){
   
        intakeMotor.config_kP(0, kP);
        intakeMotor.config_kD(0, kD);
        intakeMotor.config_kI(0, kI);
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
    private void Feeder_PID(double kP, double kI, double kD, double Rpms, double idleSpeed){
        feederMotor.config_kP(0, kP);
        feederMotor.config_kD(0, kD);
        feederMotor.config_kI(0, kI);
        feederRpms = Rpms;
        feederIdleSpeed = idleSpeed;
        feeder_kP = kP;
    }
    
    @Config
    private void Storage_PID(double kP, double kI, double kD, double Rpms){
        storageMotorH.config_kP(0, kP);
        storageMotorH.config_kD(0, kD);
        storageMotorH.config_kI(0, kI);
        storageMotorL.config_kP(0, kP);
        storageMotorL.config_kD(0, kD);
        storageMotorL.config_kI(0, kI);
        storageRpms = Rpms;
        storage_kP = kP;
    }

    public void init(OperatorInterface operatorinterface){                                                                                                                                                                                                                                                                                                                                                        
        //Constructors
        shooter = new TalonFX(Wiring.shooterMotor);
        intakeMotor = new TalonSRX(Wiring.intakeMotor);
        storageMotorH = new TalonSRX(Wiring.storageMotorH);
        storageMotorL = new TalonSRX(Wiring.storageMotorL);
        feederMotor = new TalonSRX(Wiring.feederMotor);
        oi = operatorinterface;
        //Intake Motor Config

        intakeMotor.set(ControlMode.PercentOutput, 0);	
        intakeMotor.configClosedloopRamp(cLR, TIMEOUT);
        intakeMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        intakeMotor.config_kF(0, intake_kF);
        intakeMotor.config_kP(0, intake_kP);
        intakeMotor.config_kD(0, intake_kD);
        intakeMotor.config_kI(0, intake_kI);
        intakeMotor.configNominalOutputForward(0, TIMEOUT);
        intakeMotor.configNominalOutputReverse(0, TIMEOUT);
        intakeMotor.configPeakOutputForward(+1, TIMEOUT);
        intakeMotor.configPeakOutputReverse(-1, TIMEOUT);
        intakeMotor.enableCurrentLimit(true);
		intakeMotor.configPeakCurrentLimit(75,TIMEOUT);
		intakeMotor.configContinuousCurrentLimit(40, TIMEOUT);
		intakeMotor.configPeakCurrentDuration(1800,TIMEOUT);
        intakeMotor.setNeutralMode(NeutralMode.Coast);

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
    
        //Feeder motor config
        feederMotor.set(ControlMode.PercentOutput, 0);	
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
        feederMotor.enableCurrentLimit(true);
		feederMotor.configPeakCurrentLimit(75,TIMEOUT);
		feederMotor.configContinuousCurrentLimit(40, TIMEOUT);
		feederMotor.configPeakCurrentDuration(1800,TIMEOUT);
        feederMotor.setNeutralMode(NeutralMode.Brake);

        //Storage Motor Config
        storageMotorH.set(ControlMode.PercentOutput, 0);	
        storageMotorH.configClosedloopRamp(cLR, TIMEOUT);
        storageMotorH.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        storageMotorH.config_kF(0, storage_kF);
        storageMotorH.config_kP(0, storage_kP);
        storageMotorH.config_kD(0, storage_kD);
        storageMotorH.config_kI(0, storage_kI);
        storageMotorH.configNominalOutputForward(0, TIMEOUT);
        storageMotorH.configNominalOutputReverse(0, TIMEOUT);
        storageMotorH.configPeakOutputForward(+1, TIMEOUT);
        storageMotorH.configPeakOutputReverse(-1, TIMEOUT);
        storageMotorH.enableCurrentLimit(true);
		storageMotorH.configPeakCurrentLimit(5,TIMEOUT);
		storageMotorH.configContinuousCurrentLimit(5, TIMEOUT);
		storageMotorH.configPeakCurrentDuration(1800,TIMEOUT);
        storageMotorH.setNeutralMode(NeutralMode.Brake);

        storageMotorL.set(ControlMode.PercentOutput, 0);	
        storageMotorL.configClosedloopRamp(cLR, TIMEOUT);
        storageMotorL.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        storageMotorL.config_kF(0, storage_kF);
        storageMotorL.config_kP(0, storage_kP);
        storageMotorL.config_kD(0, storage_kD);
        storageMotorL.config_kI(0, storage_kI);
        storageMotorL.configNominalOutputForward(0, TIMEOUT);
        storageMotorL.configNominalOutputReverse(0, TIMEOUT);
        storageMotorL.configPeakOutputForward(+1, TIMEOUT);
        storageMotorL.configPeakOutputReverse(-1, TIMEOUT);
        storageMotorL.enableCurrentLimit(true);
		storageMotorL.configPeakCurrentLimit(5, TIMEOUT);
		storageMotorL.configContinuousCurrentLimit(5, TIMEOUT);
		storageMotorL.configPeakCurrentDuration(1800,TIMEOUT);
        storageMotorL.setNeutralMode(NeutralMode.Brake);
    }
    public void stopfeeder(){
        feederMotor.set(ControlMode.PercentOutput, feederIdleSpeed);
    }
    public void stopStorage(){
        storageMotorH.set(ControlMode.PercentOutput, 0);
        storageMotorL.set(ControlMode.PercentOutput, 0);
    }
    public void stopIntake(){
        //intakeMotorPID.setReference(0, ControlType.kDutyCycle);
        intakeMotor.set(ControlMode.PercentOutput, 0);
    }
    public void stopShooter(){
        shooter.set(TalonFXControlMode.PercentOutput, 0);
    }
    public void feeder(){
        
        
        if(oi.pilot.getRawButton(1)){
            feederMotor.set(ControlMode.PercentOutput, feederRpms);// Will need to be velocity
        }
        else{
            stopfeeder();
        }
    }
    public void storage(){
        if(oi.copilot.getRawButton(3)){
            storageMotorH.set(ControlMode.PercentOutput, -storageRpms);// Will need to be velocity
            storageMotorL.set(ControlMode.PercentOutput, storageRpms);// Will need to be velocity
        }
        else{
           stopStorage();
        }
    }
	public void intake() {
        if(oi.copilot.getRawButton(3)){
            intakeMotor.set(ControlMode.PercentOutput, intakeRpms);// Will need to be velocity
        }
        else{
            stopIntake();
        }
    }
    public void shoot(){
        if(oi.pilot.getRawButton(6)){
           
            shooter.set(ControlMode.Velocity, shooterRpms);
        }
        else{
            stopShooter();
        }
    }
}