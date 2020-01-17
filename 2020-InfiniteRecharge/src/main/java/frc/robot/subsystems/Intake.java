package frc.robot.subsystems;
import frc.robot.OperatorInterface;
import frc.robot.Robot;
import frc.robot.subsystems.Shooter;
import frc.robot.Wiring;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Spark;
import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.robot.OperatorInterface;

public class Intake{
    public OperatorInterface oi;
    //public WPI_TalonSRX intakeMotor;
    
    public Intake(OperatorInterface oi){
    //intakeMotor = new WPI_TalonSRX(Wiring.intakeMotor);
}
    
    
    public void shoot(){
       // intakeMotor.set(ControlMode.PercentOutput, 1);

    }


}