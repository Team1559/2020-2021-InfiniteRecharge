package frc.robot;
import java.util.*;

import frc.robot.subsystems.Chassis;

public class BORROWINGDriverControls{
    private Chassis drivetrain;
    private ArrayList<Double> forwardSpeed = new ArrayList<Double>();
    private ArrayList<Double> sideSpeed = new ArrayList<Double>();
    private ArrayList<Double> leftEncoderPosition = new ArrayList<Double>();
    private ArrayList<Double> LeftEncodervelocity = new ArrayList<Double>();
    private ArrayList<Double> rightEncoderPosition = new ArrayList<Double>();
    private ArrayList<Double> rightEncodervelocity = new ArrayList<Double>();
    public void init(Chassis Drivetrain){
        drivetrain = Drivetrain;
    }
    public void periodic(Double _forwardSpeed, Double _sideSpeed, Double lep, Double lev, Double rep, Double rev ){
        if(forwardSpeed.size() <= 3000){
            forwardSpeed.add(_forwardSpeed);
            sideSpeed.add(_sideSpeed);
            leftEncoderPosition.add(lep);
            LeftEncodervelocity.add(lev);
            rightEncoderPosition.add(rep);
            rightEncodervelocity.add(rev);
        }
    }
    public void printAll() {
        for(int i = 0; i < forwardSpeed.size(); i++){
        System.out.println(forwardSpeed.get(i) +" "+ sideSpeed.get(i)+ " "+leftEncoderPosition.get(i) +" "+ LeftEncodervelocity.get(i)+ " "+rightEncoderPosition.get(i) +" "+ rightEncodervelocity.get(i));
        }
}


}