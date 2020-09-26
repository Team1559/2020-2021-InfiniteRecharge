package frc.robot.components;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class limelight{
    public NetworkTable limeLight = NetworkTableInstance.getDefault().getTable("limelight");
    public NetworkTableEntry tx = limeLight.getEntry("tx");
    public NetworkTableEntry ty = limeLight.getEntry("ty");
    public NetworkTableEntry ta = limeLight.getEntry("ta");
}