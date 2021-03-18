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

    public void init(){
        forwardSpeed.clear();
        sideSpeed.clear();
        leftEncoderPosition.clear();
        LeftEncodervelocity.clear();
        rightEncoderPosition.clear();
        rightEncodervelocity.clear();
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
        System.out.print(forwardSpeed.get(i) +" "+ sideSpeed.get(i)+ " "+leftEncoderPosition.get(i) +" "+ LeftEncodervelocity.get(i)+ " "+rightEncoderPosition.get(i) +" "+ rightEncodervelocity.get(i)+ " ");
        }
    }

    public double interpolate(double counter, double[] value){
        int intCounter = (int)counter;
        double percent = (counter -intCounter) * 100; 
        double interpolatedValue = (value[intCounter] + (percent * (value[intCounter] - value[intCounter+1])));
        return interpolatedValue;
    }
}