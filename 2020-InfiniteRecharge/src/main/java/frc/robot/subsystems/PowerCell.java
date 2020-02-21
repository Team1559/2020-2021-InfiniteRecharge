package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import frc.robot.Buttons;
import frc.robot.OperatorInterface;
import frc.robot.Wiring;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

//intake has to go double the speed the shooter goes
public class PowerCell implements Loggable {
    private OperatorInterface oi;
    public enum State {
        Spin, Wait    
    }
    private State state = State.Wait;
    // pid values
    private final int TIMEOUT = 0;
    private final double cLR = 0.1;
    @Log
    private double intake_kP = 5;// 5e-5
    private double intake_kD = 0;
    @Log
    private double intake_kI = 0.00000;// 1e-6
    private double intake_kF = 0;
    private double shooter_kF = 0; 
    private SupplyCurrentLimitConfiguration shooterLimit = new SupplyCurrentLimitConfiguration(true, 100, 20, 1000);
    @Log
    private double shooter_kP = 5;
    private double shooter_kD = 0;
    @Log
    private double shooter_kI = 0.00000;// 1e-6
    @Log
    private double storage_kP = 5;
    private double storage_kD = 0;
    @Log
    private double storage_kI = 0;
    private double storage_kF = 0;
    @Log
    private double feederP_kP = 5;// 5e-5
    private double feederP_kD = 0;
    @Log
    private double feederP_kI = 0;// 1e-6
    private double feederP_kF = 0;
    private SupplyCurrentLimitConfiguration feederLimit = new SupplyCurrentLimitConfiguration(true, 40, 20, 1000);
    private boolean feederButton = false;
    private boolean disableAll = false;
    
    //motors 
    private TalonSRX storageMotorL;
    private TalonSRX storageMotorH;
    private TalonFX shooter;
    private TalonSRX intakeMotor;
    private TalonFX feederMotor;
    @Log.Graph
    private double shooterTemp;
    @Log.Graph
    private double supplyCurrent;
    @Log.Graph
    private double statorCurrent;
    @Log
    private double shooterRpms = 100;
    @Log
    private double intakeRpms = 1;
    @Log
    private double storageRpms = 0.6; //%output for now
    @Log
    private double feederRpms = 0.2;
    @Log 
    double feederPosition = 0.0;

    //@Config.ToggleSwitch
    private void shooter_toggle(boolean on) {
        //shooterOn = on;
    }

	//@Config
    private void Intake_Percent(double Rpms){

        intakeRpms = Rpms;
        
    }

    //@Config
        private void Shooter_RPMS(double Rpms){
        shooterRpms = Rpms;
    }

    //@Config
    private void Feeder_PID(double kP, double kI, double kD, double Rpms){
        if(feederMotor != null){
        feederMotor.config_kP(0, kP);
        feederMotor.config_kD(0, kD);
        feederMotor.config_kI(0, kI);
        feederRpms = Rpms;
        feederP_kP = kP;
        }
    }


    
    //@Config
    private void Storage_Percent(double Rpms){
        storageRpms = Rpms;
    }

    public void init(OperatorInterface operatorinterface) {
        // Constructors
        shooter = new TalonFX(Wiring.shooterMotor);
        intakeMotor = new TalonSRX(Wiring.intakeMotor);
        storageMotorH = new TalonSRX(Wiring.storageMotorH);
        storageMotorL = new TalonSRX(Wiring.storageMotorL);
        feederMotor = new TalonFX(Wiring.feederMotor);
        oi = operatorinterface;
        // Intake Motor Config

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
        intakeMotor.configPeakCurrentLimit(75, TIMEOUT);
        intakeMotor.configContinuousCurrentLimit(40, TIMEOUT);
        intakeMotor.configPeakCurrentDuration(1800, TIMEOUT);
        intakeMotor.setNeutralMode(NeutralMode.Coast);

        // Shooter Motor Config
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
        shooter.configSupplyCurrentLimit(shooterLimit);
        
        

        // Feeder motor config
        feederMotor.set(ControlMode.PercentOutput, 0);
        feederMotor.configClosedloopRamp(cLR, TIMEOUT);
        feederMotor.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        feederMotor.config_kF(0, feederP_kF);
        feederMotor.config_kP(0, feederP_kP);
        feederMotor.config_kD(0, feederP_kD);
        feederMotor.config_kI(0, feederP_kI);
        feederMotor.configNominalOutputForward(0, TIMEOUT);
        feederMotor.configNominalOutputReverse(0, TIMEOUT);
        feederMotor.configPeakOutputForward(+1, TIMEOUT);
        feederMotor.configPeakOutputReverse(-1, TIMEOUT);
        feederMotor.setNeutralMode(NeutralMode.Brake);
        feederMotor.configSupplyCurrentLimit(feederLimit);

        // Storage Motor Config
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
        storageMotorH.configPeakCurrentLimit(10, TIMEOUT);
        storageMotorH.configContinuousCurrentLimit(10, TIMEOUT);
        storageMotorH.configPeakCurrentDuration(1800, TIMEOUT);
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
        storageMotorL.configPeakCurrentLimit(10, TIMEOUT);
        storageMotorL.configContinuousCurrentLimit(10, TIMEOUT);
        storageMotorL.configPeakCurrentDuration(1800, TIMEOUT);
        storageMotorL.setNeutralMode(NeutralMode.Brake);
        stopfeeder();
    }

    public void stopfeeder() {
        feederPosition = feederMotor.getSelectedSensorPosition();
        feederMotor.set(ControlMode.Position, feederPosition);
    }

    public void stopStorage() {
        storageMotorH.set(ControlMode.PercentOutput, 0);
        storageMotorL.set(ControlMode.PercentOutput, 0);
    }

    public void stopIntake() {
        intakeMotor.set(ControlMode.PercentOutput, 0);
    }

    public void stopShooter() {
        shooter.set(TalonFXControlMode.PercentOutput, 0);
    }

    public void feeder() {
        if (oi.pilot.getRawButton(Buttons.B)) {
            if (!feederButton) {
                feederMotor.set(ControlMode.PercentOutput, -feederRpms);
                feederButton = true;
            }
        } 
        else {
            if (feederButton) {
                stopfeeder();
                feederButton = false;
            }
        }
    }
    public void store(){
        int waitTimer = 50;
        int spinTimer = 25;
        switch (state) {
            case Wait:
               stopStorage();
                waitTimer --;
                if(waitTimer == 0 ){
                    state = State.Spin;
                }
                break;
    
            case Spin:
            waitTimer = 50;
            storageMotorH.set(ControlMode.PercentOutput, -1);
            storageMotorL.set(ControlMode.PercentOutput, 1);
            if(spinTimer == 0){
                state = State.Wait;
            }
               
            }   
    }

    public void storage() {
        
        if (disableAll) {
            
            stopStorage();
        }
        else if(oi.copilot.getRawButton(Buttons.right_Bumper))
        {
            store();
        }
         else {
            storageMotorH.set(ControlMode.PercentOutput, storageRpms);
            storageMotorL.set(ControlMode.PercentOutput, -storageRpms);
        }
    }

    public void intake() {
        if (disableAll) {
            stopIntake();
        } 
        else if (oi.copilot.getRawButton(Buttons.left_Bumper) || oi.copilot.getRawButton(Buttons.right_Bumper)) {
            intakeMotor.set(ControlMode.PercentOutput, intakeRpms);
        }
        else {
            intakeMotor.set(ControlMode.PercentOutput, -intakeRpms);// Will need to be velocity
        }
    }

    public void shoot() {
        shooterTemp = shooter.getTemperature();
        supplyCurrent = shooter.getSupplyCurrent();
        statorCurrent = shooter.getStatorCurrent();
        if (disableAll) {
            stopShooter();
        }
            else {
            shooter.set(ControlMode.Velocity, -shooterRpms);
            
        }
    }



    public void go(){
        if(oi.copilot.getRawButtonPressed(Buttons.Y)){
            disableAll = !disableAll;
        }
        
        intake();
        shoot();
        storage();
        feeder();
    }
}
