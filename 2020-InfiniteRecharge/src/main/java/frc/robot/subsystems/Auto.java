package frc.robot.subsystems;

import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.PowerCell;
import frc.robot.components.Camera;
import frc.robot.components.IMU;
import frc.robot.OperatorInterface;
import frc.robot.Wiring;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.Logger;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;
import io.github.oblarg.oblog.annotations.Log.Logs;
import edu.wpi.first.wpilibj.geometry.*;

public class Auto implements Loggable {
    public enum State {
        Wait, DriveToGoal, Shoot, Move, Stop
    }

    private State state = State.Wait;
    private int timer = 0;

    private double initialWait = 0.0;
    private double driveForward = 2.438;
    private double driveBackward = 4;
    private double driveSpeed = .45;
    @Config
    private void setInitialWait(double newWait) {
        initialWait = newWait;
    }
    
    @Config(defaultValueNumeric = 3)
    private void setForward(double forward) {
        driveForward = forward;
    }
    @Config(defaultValueNumeric = 4)
    private void setReverse(double reverse) {
        driveBackward = reverse;
    }

    public void AutoInit(Chassis driveTrain) {

        driveTrain.initOdometry();
        timer = 0;
        state = State.Wait;
    }

    public void AutoPeriodic(Chassis driveTrain, PowerCell powerCell) {
        
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
            if (odometry.getTranslation().getX() <= -driveForward || timer / 50.0 >= 4.5) {
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
            driveTrain.move(driveSpeed, 0);
            if (odometry.getTranslation().getX() >= driveBackward || timer/50.0 >= 4.5) {
                timer = 0;
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