package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import frc.robot.Buttons;
import frc.robot.OperatorInterface;
import frc.robot.Wiring;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;


public class Climber implements Loggable {
    private OperatorInterface oi;
    private TalonSRX barRider;
    private TalonFX winch;
    private double leftJoystick_x = 0;
    //@Log
    private double winchUprpms = 0.9;
    //@Log
    private double winchDownrpms = 1.0;
    //@Log
    private double balancerPercent = 1;
    //@Log.Graph
    private double winchTemp;
    //@Log.Graph
    private double winchsupplyCurrent;
    //@Log.Graph
    private double winchstatorCurrent;
    //@Log.Graph
    private double balencerCurrent;
    private SupplyCurrentLimitConfiguration scl = new SupplyCurrentLimitConfiguration(true, 120, 120, 1000);
    private final int TIMEOUT = 0;
    private final double cLR = 0.1;
    
	//Shuffleboard configs for winch and bar rider
	//@Config(defaultValueNumeric = 1.0)
    private void winchup_percent_config(double Rpms){
        winchUprpms = Rpms;
    }
    //@Config(defaultValueNumeric = 0.6)
    private void winchdown_percent_config(double Rpms){
        winchDownrpms = Rpms;
    }
    //@Config(defaultValueNumeric = 1.0)
    private void Balancer_percent_config(double OutputPercent){
        balancerPercent = OutputPercent;
    }

    public void ClimberInit(OperatorInterface operatorinterface)
    {
        barRider = new WPI_TalonSRX(Wiring.barRider);
        winch = new TalonFX(Wiring.winch);
        oi = operatorinterface;

    winch.set(TalonFXControlMode.PercentOutput, 0);	
    winch.configClosedloopRamp(cLR, TIMEOUT);
    winch.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    winch.configNominalOutputForward(0, TIMEOUT);
    winch.configNominalOutputReverse(0, TIMEOUT);
    winch.configPeakOutputForward(+1, TIMEOUT);
    winch.configPeakOutputReverse(-1, TIMEOUT);
    winch.configSupplyCurrentLimit(scl, TIMEOUT);
    winch.setNeutralMode(NeutralMode.Brake);
    winch.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen);


    barRider.set(ControlMode.PercentOutput, 0);	
    barRider.configClosedloopRamp(cLR, TIMEOUT);
    barRider.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    barRider.configNominalOutputForward(0, TIMEOUT);
    barRider.configNominalOutputReverse(0, TIMEOUT);
    barRider.configPeakOutputForward(+1, TIMEOUT);
    barRider.configPeakOutputReverse(-1, TIMEOUT);
    barRider.setNeutralMode(NeutralMode.Brake);
    barRider.enableCurrentLimit(true);
    barRider.configForwardSoftLimitEnable(false);
    barRider.configReverseSoftLimitEnable(false);
	barRider.configPeakCurrentLimit(75,TIMEOUT);
	barRider.configContinuousCurrentLimit(40, TIMEOUT);
	barRider.configPeakCurrentDuration(1800,TIMEOUT);

    }

    public void stopBarrider(){
        barRider.set(ControlMode.PercentOutput, 0);
    }

    public void holdwinch(){
       
        winch.set(TalonFXControlMode.PercentOutput, 0);
    }
    /*Main method of climber class operates all functions of climber*/
    public void drive()    {
        Balance();
        // balencerCurrent = barRider.getSupplyCurrent();
        // winchTemp = winch.getTemperature();
        // winchsupplyCurrent = winch.getSupplyCurrent();
        // winchstatorCurrent = winch.getStatorCurrent();
         

        if(oi.DPadCopilot() == Buttons.Dpad_up && oi.copilot.getRawButton(Buttons.X)) { //Told you Operator Interface button controls didn't work!
            raiseClimber();
        }
        else if(oi.DPadCopilot() == Buttons.Dpad_down) {
            raiseRobot();
        }
        else{         
           holdwinch();
        }      
    
    }
    
    /*Initializes robot's departure from the ground*/
    public void raiseRobot(){
        winch.set(TalonFXControlMode.PercentOutput, winchDownrpms);
    }
    public void raiseClimber(){
        winch.set(TalonFXControlMode.PercentOutput, -winchUprpms);
    }
    
    /*Drives wheels on the bar to allow robot to balance the bar*/
    public void Balance(){
        leftJoystick_x = oi.copilot.getRawAxis(Buttons.leftJoystick_x);
        if ( leftJoystick_x > 0.3)
        {
            barRider.set(ControlMode.PercentOutput, -balancerPercent);
        }
        else if(leftJoystick_x < -0.3)
        {
            barRider.set(ControlMode.PercentOutput, balancerPercent);
        }
        else
        {
            stopBarrider();
        } 
    }
}