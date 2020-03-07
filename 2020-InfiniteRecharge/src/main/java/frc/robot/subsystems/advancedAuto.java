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

import javax.lang.model.util.ElementScanner6;

import edu.wpi.first.wpilibj.geometry.*;

public class advancedAuto implements Loggable {
    public enum State {
        Wait, Reverse1, Adjust, Forward1, Turn, Forward2, Shoot, Reverse2, Stop
    }

    private State state = State.Wait;
    private int timer = 0;

    private double initialWait = 0;
    private double forward1 = 2.438;
    private double forward2 = 9999999;
    private double reverse1 = 150;
    private double reverse2 = 0;
    private double driveSpeed = .9;
    private double turn = 22;
    private IMU imu;
    private double kP = 5;

    //@Config(defaultValueNumeric = 0)
    private void setInitialWait(double newWait) {
        initialWait = newWait;
    }

    //@Config(defaultValueNumeric = 25)
    private void setTurn(double Turn) {
        turn = Turn;
    }

   // @Config(defaultValueNumeric = 4)
    private void setForward2(double forward) {
        forward2 = forward;
    }

   // @Config(defaultValueNumeric = 4)
    private void setMove1(double move1) {
        reverse1 = move1;
        forward1 = move1;
    }

   // @Config(defaultValueNumeric = 0)
    private void setReverse2(double reverse) {
        reverse2 = reverse;
    }

    public void AutoInit(Chassis driveTrain, IMU imU, PowerCell powerCell) {
        imu = imU;
        powerCell.startIntake();
        driveTrain.initOdometry();
        timer = 0;
        state = State.Wait;
    }

    public void AutoPeriodic(Chassis driveTrain, PowerCell powerCell) {

        timer++;
        // System.out.println("Time: " + timer);
        Pose2d odometry = driveTrain.updateOdometry();
        switch (state) {
        case Wait:
        powerCell.lowerGatherer();
            // System.out.println("We're waiting");
            powerCell.store();

            if (timer / 50.0 >= initialWait) {
                timer = 0;
                state = State.Reverse1;
            }
            break;

        case Reverse1:
        powerCell.lowerGatherer();
        powerCell.startStorage();

            // System.out.println("Sucking the balls");
           System.out.println(driveTrain.distance());
            driveTrain.move(driveSpeed, 0);
            
            if (driveTrain.distance() >= reverse1 || timer / 50.0 >= 4) {
                timer = 0;
                state = State.Forward1;
            }
            break;
        case Adjust:
            // System.out.println("Yaw" + imu.getYaw());
            // if (timer <= 100) {
            //     driveTrain.move(0, 0);
            // } else {
            //     if (imu.getYaw() >= -1) {
            //         driveTrain.move(0, .1);
            //     } else {
                    timer = 0;
                    state = State.Forward1;
              //  }
           // }

            break;
        case Forward1:
        powerCell.lowerGatherer();
            powerCell.startStorage();

            //System.out.println("Forward one");
            System.out.println(imu.y_angularVelocity); 
            driveTrain.move(-driveSpeed, 0.085); // 0.03
            if (odometry.getTranslation().getX() <= -forward1 || timer / 50.0 >= 4.5) { //4.5
                timer = 0;
                state = State.Forward2;
            }
            break;

        case Turn:
            // driveTrain.move(0, .1);
            // powerCell.store();
            // System.out.println(imu.getYaw());
            //if (imu.getYaw() <= -turn) {
                timer = 0;
                state = State.Forward2;
            //}
            // else if(imu.getYaw() > 10){//We're in trouble, direction is way off
            //     timer = 0;
            //     state = State.Stop;
            // }
            // else if(timer / 50.0 >= 3){
            //     timer = 0;
            //     state = State.Stop;
            // }
            break;

        case Forward2:
        powerCell.lowerGatherer();
            powerCell.store();
            //  System.out.println("Driving to Goal");
            driveTrain.move(-driveSpeed/2.0, 0);
            powerCell.startShooter();
            
            if (driveTrain.distance() <= -forward2 || timer / 50.0 >= 2.5) { //2.25
                timer = 0;
                state = State.Shoot;
            }
            break;

        case Shoot:
        powerCell.lowerGatherer();
            if(Math.abs(imu.getYaw()) >= 60){
                state = State.Stop;
            }
            else{
                //  System.out.println("It's Shootin Time");
                driveTrain.move(0, 0);
                powerCell.startStorage();

                powerCell.startShooter();
                powerCell.startFeeder();
                if (timer / 50.0 >= 4.0) {
                    timer = 0;
                    state = State.Reverse2;
                    powerCell.stopWithoutButton();
                }
            }
            break;

        case Reverse2:
            //  System.out.println("Moving Back");
            powerCell.raiseGatherer();
            driveTrain.move(driveSpeed, 0);
            if (odometry.getTranslation().getX() >= reverse2 || timer / 50.0 >= 0.0) {
                timer = 0;
                state = State.Stop;

            }
            break;

        case Stop:
            // System.out.println("It's Stopped");
            powerCell.raiseGatherer();
            powerCell.stopWithoutButton();
            driveTrain.move(0, 0);
            break;
        }
    }
}