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
      double tx = limeLightTable.getEntry("tx");
      return tx;
    }
    public double geTa(){ //Sill Needs Work
        double ta = limeLightTable.getEntry("ta");
        return ta;
    }
    public double getTy(){ //Sill Needs Work
        double ty = limeLightTable.getEntry("ty");
        return ty;
    }
}