package frc.robot.subsystems;

import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.PowerCell;
import edu.wpi.first.wpilibj.geometry.*;

public class BasicAuto{
    public enum State {
        Wait, DriveToGoal, Shoot, Move, Stop
    }

    private State state = State.Wait;
    private int timer = 0;

    private double initialWait = 0;
    private double driveForward = 2.438;
    private double driveBackward = 0;
    private double driveSpeed = .45;
   

    public void AutoInit(Chassis driveTrain) {

        driveTrain.initOdometry();
        timer = 0;
        state = State.Wait;
    }

    public void AutoPeriodic(Chassis driveTrain, PowerCell powerCell, boolean doReverse) {
        
        timer++;
        //System.out.println("Time: " + timer);
        Pose2d odometry = driveTrain.updateOdometry();
        switch (state) {
        case Wait:
            //System.out.println("We're waiting");
            if (timer / 50.0 >= initialWait) {
                timer = 0;
                state = State.DriveToGoal;
            }
            break;

        case DriveToGoal:
            //System.out.println("Driving to Goal");
            driveTrain.move(-driveSpeed, 0);
            powerCell.startShooter();
            powerCell.startStorage();
            if (driveTrain.R2M(odometry.getTranslation().getX()) <= -driveForward || timer / 50.0 >= 4.5) {
                timer = 0;
                state = State.Shoot;
            }
            break;

        case Shoot:
            //System.out.println("It's Shootin Time");
            driveTrain.move(0, 0);
            powerCell.startShooter();
            powerCell.startStorage();
            powerCell.startFeeder();
            if (timer / 50.0 >= 4.0) {
                timer = 0;
                state = State.Move;
                powerCell.stopWithoutButton();
            }
            break;

        case Move:
            //System.out.println("Moving Back");
            if(doReverse){
                driveTrain.move(driveSpeed, 0);
                if (driveTrain.R2M(odometry.getTranslation().getX()) >= driveBackward || timer/50.0 >= 4.5) {
                    timer = 0;
                    state = State.Stop;  
                }
            }   
        else{
            state = State.Stop;
        } 
            break;
        case Stop:
            //System.out.println("It's Stopped");
            driveTrain.move(0, 0);
            break;
        }
    }
}