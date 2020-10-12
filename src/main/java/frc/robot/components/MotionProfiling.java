package frc.robot.components;
//imports
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileSubsystem;
import frc.robot.subsystems.Chassis;
import frc.robot.Constants;

public class MotionProfiling extends TrapezoidProfileSubsystem {
    private Chassis driveTrain;


    private static double maxVelocity = 10;
    private static double maxAccelaration = 0.5;

    public void init(Chassis drivetraiN) {
        driveTrain = drivetraiN;
    }

    public MotionProfiling() {
        super(new TrapezoidProfile.Constraints(maxVelocity,maxAccelaration));

        // TODO Auto-generated constructor stub
    }

    @Override
    protected void useState(State setpoint) {
        // TODO Auto-generated method stub

    }


}