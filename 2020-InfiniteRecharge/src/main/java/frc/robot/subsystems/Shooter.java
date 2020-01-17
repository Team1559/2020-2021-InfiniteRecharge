package frc.robot.subsystems;
import frc.robot.OperatorInterface;
import frc.robot.Robot;
import frc.robot.subsystems.Intake;
import frc.robot.Wiring;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Spark;
import com.ctre.phoenix.motorcontrol.ControlMode;
import frc.robot.OperatorInterface;

public class Shooter{
    public OperatorInterface oi;
    public WPI_TalonSRX shooterMotor;
    
    public Shooter(OperatorInterface oi){
    shooterMotor = new WPI_TalonSRX(Wiring.shooterMotor);
}
    
    
    public void shoot(){
        shooterMotor.set(ControlMode.PercentOutput, 1);

    }


}