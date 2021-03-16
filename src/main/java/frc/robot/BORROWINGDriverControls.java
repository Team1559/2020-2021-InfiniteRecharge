package frc.robot;
import java.util.*;

import frc.robot.subsystems.Chassis;

public class BORROWINGDriverControls{
    private Chassis drivetrain;
    private ArrayList<Double> forwardSpeed = new ArrayList<Double>();
    private ArrayList<Double> sideSpeed = new ArrayList<Double>();
    public void init(Chassis Drivetrain){
        drivetrain = Drivetrain;
    }
    public void periodic(Double _forwardSpeed, Double _sideSpeed){
        if(forwardSpeed.size() <= 3000){
            forwardSpeed.add(_forwardSpeed);
            sideSpeed.add(_sideSpeed);
        }
    }
    public void printAll() {
        for(int i = 0; i < forwardSpeed.size(); i++){
        System.out.print(forwardSpeed.get(i) +" "+ sideSpeed.get(i)+ " ");
        }
        // System.out.print(forwardSpeed.get(i) +", ");
        // }
        // System.out.println();
        // System.out.println();
        // for(int i = 0; i < sideSpeed.size(); i++){
        // System.out.print(sideSpeed.get(i) +", ");
        // }
}


}