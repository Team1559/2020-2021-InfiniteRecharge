
package frc.robot.subsystems;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OperatorInterface;
import frc.robot.Wiring;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorSensorV3;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;
import io.github.oblarg.oblog.annotations.Log;

public class Spinner implements Loggable {
    private OperatorInterface oi;
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor;
    //Variable for the motor
    private TalonSRX spinnerMotor;
    //Variable for a temporary placeholder for the color value, to compare with the current color
    private String tempColor = "B";
    //Varible for the Solenoid, everything with "Launcher" and "Fire" is a Solenoid component
    private Solenoid spinnerLauncher;
    //Variable for the position of the spinner
    private boolean Fire = false; 
    //Variable or the button that is pushed, that is used as a toggle.
    private boolean buttonX = false;
    //speed that the spinner spins during stage 2 only, which can be changed in shuffleboard
    @Log
    private double spinnerOutput = 0.3;
    //Variable for the current color, which is displayed on shuffleboard
    @Log
    String currentColor = "None";
    public boolean skipLayout(){
        return true;
    }
    //Variable that counts the amount of color changes that the sensor detects
    @Log
    int colorCount = 0;
    @Config //(max = 1 , min = -1 , blockIncrement = .05)
    public void configSpinner(double output){
        spinnerOutput = output;
    }
    public void init( OperatorInterface ointerface)
    {
        spinnerMotor = new TalonSRX(5);
        spinnerMotor.setNeutralMode(NeutralMode.Brake);
        spinnerLauncher = new Solenoid(Wiring.spinnerLauncher);
        oi = ointerface;
    }

    public void spin(boolean colorEnable)
    {
        //Solenoid logic for a toggle, which fires the "launcher" out, towards the color wheel if when true
        buttonX = oi.pilot.getRawButtonPressed(3); 
        if(buttonX && Fire == false)
        {
            Fire = true; 
        }
        else if (buttonX && Fire == true){
            Fire = false; 
           
        }
        System.out.println(Fire); 
        if(Fire)
        {
            spinnerLauncher.set(true); 
        }
        else
        {
            spinnerLauncher.set(false);
        }


        ///This is the Field Management system code, which was found on docs.wpilib.org
        String gameData;
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        if(gameData.length() > 0)
        {
          switch (gameData.charAt(0))
          {
            case 'B' :
              //Blue case code
              break;
            case 'G' :
              //Green case code
              break;
            case 'R' :
              //Red case code
              break;
            case 'Y' :
              //Yellow case code
              break;
            default :
              //This is corrupt data
              break;
          }
        } else {
          //Code for no data received yet
        }

        //This will run when the "colorEnable" futureflag is enabled
        if(colorEnable){
            updateColor();
           
            //when pushing down the B button, this runs stage 2 code
            if(oi.pilot.getRawButton(2)){
                
                if(!tempColor.equals(currentColor)){
                    colorCount ++;
                    tempColor = currentColor;
                    

                }
                if(colorCount < 30){
        
                 spinnerMotor.set(ControlMode.PercentOutput, spinnerOutput);
                }
                else
                {
                    spinnerMotor.set(ControlMode.PercentOutput, 0);
                }
            }
            else{
                colorCount = 0; 
            }


            //when pushing down the A button, this runs the stage 3 code
            if(oi.pilot.getRawButton(1)){
                spinnerMotor.set(ControlMode.PercentOutput, 0.10);
                if(gameData.equals("Y")){
                    if(currentColor.equals("G")){
                        spinnerMotor.set(ControlMode.PercentOutput, 0);
                    }
                }
                if(gameData.equals("B")){
                    if(currentColor.equals("R")){
                        spinnerMotor.set(ControlMode.PercentOutput, 0);
                    }
                }
                if(gameData.equals("G")){
                    if(currentColor.equals("Y")){
                        spinnerMotor.set(ControlMode.PercentOutput, 0);
                    }
                }
                if(gameData.equals("R")){
                    if(currentColor.equals("B")){
                        spinnerMotor.set(ControlMode.PercentOutput, 0);
                    }
                }
            }
            else
            {
                spinnerMotor.set(ControlMode.PercentOutput, 0);
            }
            }
            
        }
    
    public Spinner() {
        m_colorSensor = new ColorSensorV3(i2cPort);
    }
    //This is used to find the current color that the sensor is detecting
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

            SmartDashboard.putNumber("Red", redColor);
            SmartDashboard.putNumber("Green", greenColor);
            SmartDashboard.putNumber("Blue", blueColor);
    }
}