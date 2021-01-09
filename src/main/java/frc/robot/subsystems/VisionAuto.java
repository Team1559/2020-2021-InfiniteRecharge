package frc.robot.subsystems;

import frc.robot.subsystems.Chassis;
import frc.robot.subsystems.PowerCell;
import frc.robot.components.IMU;
import frc.robot.subsystems.Vision;
import edu.wpi.first.wpilibj.geometry.*;

public class VisionAuto {
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
    //private double turn = 22;
    private IMU imu;
    private Vision vision;
    private double targetDistance = 5;
    private boolean doReverse = true;
    //private double kP = 5;

    

    public void AutoInit(Chassis driveTrain, IMU imU, PowerCell powerCell, Vision visioN) {
        imu = imU;
        vision = visioN;
        powerCell.startIntake();
        driveTrain.initOdometry();
        timer = 0;
        state = State.Wait;
        driveTrain.setRampRate(1);
    }

    public void AutoPeriodic(Chassis driveTrain, PowerCell powerCell) {

        timer++;
        Pose2d odometry = driveTrain.updateOdometry();
        switch (state) {
        case Wait:
            powerCell.lowerGatherer();
            powerCell.store();

            if (timer / 50.0 >= initialWait) {
                timer = 0;
                state = State.Reverse1;
            }
            break;

        case Reverse1:
            powerCell.lowerGatherer();
            powerCell.startStorage();
            driveTrain.move(driveSpeed, 0);

            if (driveTrain.distance() >= reverse1 || timer / 50.0 >= 4) {
                timer = 0;
                state = State.Forward1;
            }
            break;
        case Adjust:
            timer = 0;
            state = State.Forward1;
            break;
        case Forward1:
            powerCell.lowerGatherer();
            powerCell.startStorage();
            if(timer / 50.0 >= 2.5)
            {
                powerCell.stopIntake();
            }
            driveTrain.move(-driveSpeed, 0.085); // 0.03
            if (odometry.getTranslation().getX() <= -forward1 || timer / 50.0 >= 4.5) { // 4.5
                timer = 0;
                state = State.Forward2;
            }
            break;

        case Turn:
            timer = 0;
            state = State.Forward2;
            break;

        case Forward2:
            powerCell.lowerGatherer();
            powerCell.store();
            vision.driveToTarget(targetDistance);
            powerCell.startShooter();
            if (vision.getDistance() <= targetDistance || timer / 50.0 >= 2.5) { // 2.25
                timer = 0;
                state = State.Shoot;
            }
            break;

        case Shoot:
            powerCell.lowerGatherer();
            if (Math.abs(imu.yaw) >= 60) {
                state = State.Stop;
            } else {
                driveTrain.move(0, 0);
                powerCell.startStorage();
                powerCell.startIntake();
                powerCell.startShooter();
                powerCell.startFeeder();
                if (timer / 50.0 >= 4.0){
                    timer = 0;
                    state = State.Reverse2;
                    powerCell.stopWithoutButton();
                }
            }
            break;

        case Reverse2:
            if(doReverse){
                powerCell.raiseGatherer();
                driveTrain.move(driveSpeed, 0);
                if (odometry.getTranslation().getX() >= reverse2 || timer / 50.0 >= 0.0) {
                    timer = 0;
                    state = State.Stop;

                }
            }
            else{
                state=State.Stop;
            }
            break;

        case Stop:
            powerCell.raiseGatherer();
            powerCell.stopWithoutButton();
            driveTrain.move(0, 0);
            driveTrain.setRampRate(0.6);
            break;
        }
    }
}