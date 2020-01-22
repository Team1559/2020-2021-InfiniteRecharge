package frc.robot;

import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;

public class Wiring implements Loggable
{
//Drivetrain

//Power Cell
public static int intakeMotor = 6;
    @Config
    public void intakemotor(int newmotor){
        intakeMotor = newmotor;
    }
    public static int shooterMotor = 6;
    @Config
    public void shootermotor(int newmotor){
        intakeMotor = newmotor;
    }
    
//Climber

//Control Panel

}