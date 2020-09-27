package frc.robot.subsystems;
//Imports
import frc.robot.components.Limelight;
import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.PowerCell;
import frc.robot.components.IMU;

public class Vision{
    //Creation of variables
    private Limelight limeLight;
    private IMU imu;
    private Chassis drivetrain;
    private double kP= -0.1f;
    private double min_command = 0.05f;
    private double leftSide = 0;
    private double rightSide = 0;
    private boolean driveTheChassis = false;// used for testing will eventually be removed along with the if statement

    public void init(IMU inertialMessurmentUnit, Chassis ChassiS, Limelight limelighT){
        imu = inertialMessurmentUnit;
        drivetrain = ChassiS;
        limeLight = limelighT;
    }

    

    public void go(){ //we should look into adding a pathfinding algorithem to allow for a more efficiant approach, currently this is example code from the lielight website, the code was written for C++ and was hopefully converted to java.   
            double tx = limeLight.getTx(); //the target xValue
            double heading_error = -tx;
            double steering_adjust = 0.0f;
            if (tx > 1.0)
            {
                steering_adjust = kP*heading_error - min_command;
            }
            else if (tx < 1.0)
            {
                steering_adjust = kP*heading_error + min_command;
            }
            leftSide += steering_adjust;
            rightSide -= steering_adjust;
            System.out.println("Tx is " + tx);
            System.out.println("sterring ajust is" + steering_adjust);
            System.out.println("left Side speed is " + leftSide + "right side speed is "+ rightSide);
            
            if(driveTheChassis){// will be removed along with the boolean
            drivetrain.driveTrain.tankDrive(leftSide, rightSide);
            }
        }
    

}