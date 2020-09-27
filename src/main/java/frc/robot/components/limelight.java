package frc.robot.components;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight{
    public NetworkTable limeLightTable;
    public void init(){
        limeLightTable = NetworkTableInstance.getDefault().getTable("limelight");
    }
    public double getTx(){
      NetworkTableEntry targetX = limeLightTable.getEntry("tx");
      double tx = targetX.getDouble(0);
      return tx;
    }
    public double geTa(){
      NetworkTableEntry targetA = limeLightTable.getEntry("ta");
      double ta = targetA.getDouble(0);
        return ta;
    }
    public double getTy(){
        NetworkTableEntry targetY = limeLightTable.getEntry("ty");
        double ty = targetY.getDouble(0);
        return ty;
    }
}