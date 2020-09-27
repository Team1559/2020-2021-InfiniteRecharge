package frc.robot.components;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class Limelight{
    public NetworkTable limeLightTable;
    public void init(){
        limeLightTable = NetworkTableInstance.getDefault().getTable("limelight");
    }
    public double getTx(){ //Sill Needs Work
      NetworkTableEntry targetX = limeLightTable.getEntry("tx");
      double tx = targetX.getDouble(0);
      return tx;
    }
    public double geTa(){ //Sill Needs Work
      NetworkTableEntry targetA = limeLightTable.getEntry("ta");
      double ta = targetA.getDouble(0);
        return ta;
    }
    public double getTy(){ //Sill Needs Work
        NetworkTableEntry targetY = limeLightTable.getEntry("ty");
        double ty = targetY.getDouble(0);
        return ty;
    }
}