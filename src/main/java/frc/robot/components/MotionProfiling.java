package frc.robot.components;

import edu.wpi.first.wpilibj.controller.ArmFeedforward;
//imports
import edu.wpi.first.wpilibj.trajectory.TrapezoidProfile;
import edu.wpi.first.wpilibj2.command.TrapezoidProfileSubsystem;
import frc.robot.subsystems.Chassis;

public class MotionProfiling extends TrapezoidProfileSubsystem {
    private Chassis driveTrain;
    private final ArmFeedforward m_feedforward = new ArmFeedforward(0.0006, 7, 8);
    public double position;
    public double velocity;
    private static double maxVelocity = 10;
    private static double maxAccelaration = 0.5;

    public void init(Chassis drivetraiN) {
        driveTrain = drivetraiN;
    }

    public MotionProfiling() {
        super(new TrapezoidProfile.Constraints(maxVelocity,maxAccelaration));

    }

    @Override
    protected void useState(TrapezoidProfile.State setpoint) {
        double feedforward = m_feedforward.calculate(setpoint.position, setpoint.velocity);
        driveTrain.setKF(feedforward / 12.0);
        position = setpoint.position;
        velocity = setpoint.velocity;
    }
}