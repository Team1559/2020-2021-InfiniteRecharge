package frc.robot.components;

// import com.sun.net.httpserver.Authenticator.Result;
// import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.AnalogInput;


public class DistSensor
{

    private AnalogInput analogInput; 
    private double distanceOffset = 24.031;

    public void init(AnalogInput ai)
    {
        analogInput = ai; 
    }
    
    public double getRange(){

        /* Analog Sensor Testing */ 

		//int raw = analogInput.getValue();
		double volts = analogInput.getVoltage();
		//int averageRaw = analogInput.getAverageValue();
		//double averageVolts = analogInput.getAverageVoltage();
	

		double IRdistance = (distanceOffset * Math.pow(volts, -1.5549))-16;

        return IRdistance;
    }
    public boolean isRangeZero(){
        if(getRange()<= 0){
            return true;
        }
        else{
            return false;
        }
    }
}