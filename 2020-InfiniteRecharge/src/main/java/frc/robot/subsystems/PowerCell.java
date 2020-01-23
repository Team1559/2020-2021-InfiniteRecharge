package frc.robot.subsystems;
import frc.robot.OperatorInterface;
import frc.robot.Robot;
import frc.robot.Wiring;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.controller.PIDController;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;


 //intake has to go double the speed the shooter goes
public class PowerCell implements Loggable{
    private  final int TIMEOUT = 0;
    private  final double kF = 0; //F-gain = (100% X 1023) / 7350 F-gain = 0.139183673 - (7350 is max speed)
	private final double kP = 5; // P-gain = (.1*1023)/(155) = 0.66 - (350 is average error)
	private  final double kD = 0;
    private  final double cLR = 0.1;
    private TalonFX intakeMotor;
    private TalonFX shooter;
    private OperatorInterface oi;
    @Log
    private PIDController pidController = new PIDController(1, 0, 0);
    @Log
    private int shooterrpms;
    @Log
    private int intakeRpms = 0;    


    @Config
    private void PidControler(PIDController newpidcontroller){
        pidController = newpidcontroller;
    }

    @Config
    private void set_shooter_Rpms(int newrpms){
        shooterrpms = newrpms;
    } 
    
    @Config
    private void set_intake_rpms(int newrpms){
        intakeRpms = newrpms;
    }
    
    public void init(){
        //Constructors
        oi = Robot.oi;
        intakeMotor = new TalonFX(Wiring.intakeMotor);
        shooter = new TalonFX(Wiring.shooterMotor);

        //Intake Motor Config
        intakeMotor.setNeutralMode(NeutralMode.Brake);
        intakeMotor.set(TalonFXControlMode.Velocity, 0);	
        intakeMotor.configClosedloopRamp(cLR, TIMEOUT);
        intakeMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        intakeMotor.config_kF(0, kF);
        intakeMotor.config_kP(0, kP);
        intakeMotor.config_kD(0, kD);
        intakeMotor.config_kI(0, 0);
        intakeMotor.configNominalOutputForward(0, TIMEOUT);
        intakeMotor.configNominalOutputReverse(0, TIMEOUT);
        intakeMotor.configPeakOutputForward(+1, TIMEOUT);
        intakeMotor.configPeakOutputReverse(-1, TIMEOUT);
        intakeMotor.setNeutralMode(NeutralMode.Brake);

        //Shooter Motor Config
        shooter.set(TalonFXControlMode.Velocity, 0);	
        shooter.configClosedloopRamp(cLR, TIMEOUT);
        shooter.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        shooter.config_kF(0, kF);
        shooter.config_kP(0, kP);
        shooter.config_kD(0, kD);
        shooter.config_kI(0, 0);
        shooter.configNominalOutputForward(0, TIMEOUT);
        shooter.configNominalOutputReverse(0, TIMEOUT);
        shooter.configPeakOutputForward(+1, TIMEOUT);
        shooter.configPeakOutputReverse(-1, TIMEOUT);
        shooter.setNeutralMode(NeutralMode.Coast);
    }

    public void stopIntake(){
        intakeMotor.set(ControlMode.Velocity, 0);
    }
    public void stopShooter(){
        shooter.set(ControlMode.Velocity, 0);
    }
	public void intake() {
        if(oi.copilot.getRawButton(1))
        {
            intakeMotor.set(ControlMode.Velocity, intakeRpms);
        }
        else
        {
           stopIntake();
        }

    }
    public void shoot(){
        if(oi.copilot.getRawButton(2)){
            shooter.set(ControlMode.Velocity, shooterrpms);
        }
        else{
            stopShooter();
        }
    }
}