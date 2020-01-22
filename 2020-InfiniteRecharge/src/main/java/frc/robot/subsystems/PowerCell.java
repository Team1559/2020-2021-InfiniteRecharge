package frc.robot.subsystems;

import frc.robot.OperatorInterface;
import frc.robot.Wiring;
import io.github.oblarg.oblog.annotations.Config;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.controller.PIDController;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import frc.robot.OperatorInterface;

 //intake has to go double the speed the shooter goes
public class PowerCell{
    private OperatorInterface oi;
    private TalonSRX intakeMotor;
    
    @Config
    private PIDController pidController = new PIDController(1, 0, 0);
 
    @Config.NumberSlider(min=0, max=3000)
    private double intakeRPM = 120.0;
    
    public PowerCell(OperatorInterface oInterface){
        intakeMotor = new TalonSRX(Wiring.intakeMotor);
        intakeMotor.setNeutralMode(NeutralMode.Brake);
        oi = oInterface;
    }

    public void intake() {
        if(oi.pilot.getRawButton(1))
        {
            intakeMotor.set(ControlMode.Velocity, intakeRPM);
        }
        else
        {
            intakeMotor.set(ControlMode.Velocity, 0);
        }
    }
}
    
