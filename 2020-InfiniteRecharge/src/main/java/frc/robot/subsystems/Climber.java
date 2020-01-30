package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.robot.Robot;
import frc.robot.Wiring;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;


public class Climber implements Loggable {
    private TalonSRX barRider;
    private TalonFX winch;
    private boolean e_stopWinch = false;
    @Log
    private double winch_kP = 5; 
    private double winch_kD = 0;
    @Log
    private double winch_kI = 0.00000;//1e-6
    private double winch_kF = 0;
    @Log
    private double winchRpms = 0;
    @Log
    private double balencerPercent;

    private final int TIMEOUT = 0;
    private final double cLR = 0.1;
    
    //Shuffleboard configs for winch and 
    @Config 
    public void e_stopWinch(boolean enable){
        e_stopWinch = enable;
    }
    @Config
    private void winch_PID(double kP, double kI, double kD, double Rpms){
        winch.config_kP(0, kP);
        winch.config_kD(0, kD);
        winch.config_kI(0, kI);
        winchRpms = Rpms;
        winch_kP = kP;
    }
    @Config
    private void Balencer_RPMS( double OutputPercent){
        balencerPercent = OutputPercent;
    }
    
    

    public void ClimberInit()
    {
        barRider = new WPI_TalonSRX(Wiring.barRider);
        winch = new TalonFX(Wiring.winch);
        

    winch.set(TalonFXControlMode.Velocity, 0);	
    winch.configClosedloopRamp(cLR, TIMEOUT);
    winch.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
    winch.config_kF(0, winch_kF);
    winch.config_kP(0, winch_kP);
    winch.config_kD(0, winch_kD);
    winch.config_kI(0, winch_kI);
    winch.configNominalOutputForward(0, TIMEOUT);
    winch.configNominalOutputReverse(0, TIMEOUT);
    winch.configPeakOutputForward(+1, TIMEOUT);
    winch.configPeakOutputReverse(-1, TIMEOUT);
    winch.setNeutralMode(NeutralMode.Coast);
    }

    public void stopBarrider(){
        barRider.set(ControlMode.PercentOutput, 0);
    }

    public void e_stopWinch(){
        winch.set(TalonFXControlMode.PercentOutput, 0);
    }

    public void holdwinch(){
       
        winch.set(TalonFXControlMode.Velocity, 0);
    }
    /*Main method of climber class operates all functions of climber*/
    public void drive()    {
        Balance();

         if(e_stopWinch){
            e_stopWinch();
        }

        if(Robot.oi.coButtonIsPressed(3) && e_stopWinch == false) {
            unwindWinch();
        }
        else if(Robot.oi.copilot.getRawButton(2) && e_stopWinch == false){
            climbup();
        }
        else if(e_stopWinch == false){         
           holdwinch();
        }      
    
    }
    
    /*Initializes robot's departure from the ground*/
    public void climbup(){
        winch.set(TalonFXControlMode.Velocity, winchRpms);
    }
    public void unwindWinch(){
        winch.set(TalonFXControlMode.Velocity, -winchRpms);
    }
    
    /*Drives wheels on the bar to allow robot to balance the bar*/
    public void Balance(){
        if(Robot.oi.DPad() == 90)
        {
            barRider.set(ControlMode.PercentOutput, balencerPercent);
        }
        else if(Robot.oi.DPad() == 270)
        {
            barRider.set(ControlMode.PercentOutput, -balencerPercent);
        }
        else
        {
            stopBarrider();
        }
    
        
    }
}