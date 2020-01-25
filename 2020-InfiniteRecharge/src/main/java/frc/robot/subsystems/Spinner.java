package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorSensorV3;

import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

public class Spinner implements Loggable {
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor;
    private TalonSRX spinnerMotor;
    


    //This is the Color Oblog log
    @Log
    String currentColor = "None";
    public boolean skipLayout(){
        return true;
    }

    


    public void init()
    {
        spinnerMotor = new TalonSRX(5);

        
    }

    public void spin()
    {
        spinnerMotor.set(ControlMode.PercentOutput, .2);
    }

    public Spinner() {
        m_colorSensor = new ColorSensorV3(i2cPort);
    }

    public void updateColor()
        {
        int blueColor = m_colorSensor.getBlue();
        int greenColor = m_colorSensor.getGreen();
        int redColor = m_colorSensor.getRed();
        double norm_max = 0.0; 
        double blueNorm = 0.0; 
        double redNorm = 0.0; 
        double greenNorm = 0.0;
        double blueCon = 0.0;
        double redCon = 0.0;
        double greenCon = 0.0;
        double yellowCon = 0.0;

//         String i = "B";
//         int j = 0;
//         while(j <= 0){
//         spin();
// System.out.println("Helllllllllo");
        if(blueColor > greenColor)
        {
            norm_max = blueColor; 
        }
        else
        {
            norm_max = greenColor; 
        }
        if(redColor > norm_max)
        {
            norm_max = redColor; 
        } 
      
        blueNorm = (255*(blueColor/norm_max));
        redNorm = (255*(redColor/norm_max));
        greenNorm = (255*(greenColor/norm_max));

        redCon = (((redNorm/128)+((255-greenNorm)/255)+ ((255-blueNorm)/255)))/3;
        blueCon = (((blueNorm/255)+(greenNorm/255)+ ((255-redNorm)/255)))/3;
        greenCon = (((greenNorm/255)+((255-redNorm)/255)+ ((255-blueNorm)/255)))/3;
        yellowCon = (((greenNorm/255)+(redNorm/255)+ ((255-blueNorm)/255)))/3;

        if((blueCon > greenCon) && (blueCon > redCon) && (blueCon >  yellowCon) )
        {
           currentColor = "B";


        }
        else if( (greenCon > blueCon) && (greenCon > redCon) && (greenCon > yellowCon) )
        {
            currentColor = "G";

        }
        else if((redCon > greenCon) && (redCon > blueCon) && (redCon > yellowCon) )
        {
            
            currentColor = "R"; 
        }
         else
        {
            
            currentColor = "Y";
        }
    //    if(currentColor.equalsIgnoreCase(i))
    //    {
    //      j++;

    }
    // spinnerMotor.set(ControlMode.PercentOutput, 0);

    //}


        
            SmartDashboard.putNumber("Red", redColor);
            SmartDashboard.putNumber("Green", greenColor);
            SmartDashboard.putNumber("Blue", blueColor);

        
        



    }



}
