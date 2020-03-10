
package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Buttons;
import frc.robot.OperatorInterface;
import frc.robot.Wiring;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorSensorV3;



public class Spinner{
    private OperatorInterface oi;
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor;
    // Variable for the motor
    private TalonSRX spinnerMotor;
    // Variable for a temporary placeholder for the color value, to compare with the
    // current color
    private String tempColor = "B";
    // Varible for the Solenoid, everything with "Launcher" and "Fire" is a Solenoid
    // component
    private Solenoid spinnerLauncher;
    // Variable for the position of the spinner
    private boolean Fire = false;
    // Variable or the button that is pushed, that is used as a toggle
    private boolean buttonX = false;
    // speed that the spinner spins during stage 2 only
    private double spinnerOutput;
    // Variable for the current color
    private String currentColor = "None";
    private int blueColor = 0;
        private int greenColor = 0;
        private int redColor = 0;
        private double norm_max = 0.0;
        private double blueNorm = 0.0;
        private double redNorm = 0.0;
        private double greenNorm = 0.0;
        private double blueCon = 0.0;
        private double redCon = 0.0;
        private double greenCon = 0.0;
        private double yellowCon = 0.0;
        private String gameData;
        private String FMScolor = "";

    public boolean skipLayout() {
        return true;
    }

    // Variable that counts the amount of color changes that the sensor detects
    private int colorCount = 0;


    public void init(OperatorInterface ointerface) {
        spinnerMotor = new TalonSRX(Wiring.spinnerMotor);
        spinnerMotor.setNeutralMode(NeutralMode.Brake);
        spinnerLauncher = new Solenoid(Wiring.spinnerLauncher);
        oi = ointerface;
        spinnerOutput = 0.3;
    }

    public void spin(boolean compressorEnable) {
        if (compressorEnable) {
            // Solenoid logic for a toggle, which fires the "launcher" out, towards the
            // color wheel if when true
            buttonX = oi.pilot.getRawButtonPressed(Buttons.Y);
            if (buttonX && Fire == false) {
                Fire = true;
            } else if (buttonX && Fire == true) {
                Fire = false;

            }
            if(Fire) {
                spinnerLauncher.set(true);
            } else {
                spinnerLauncher.set(false);
            }
        }

        /// This is the Field Management system code, which was found on docs.wpilib.org
        
        gameData = DriverStation.getInstance().getGameSpecificMessage();
        if (gameData.length() > 0) {
            FMScolor = gameData.substring(0, 1);
        }
        // This will run when the "colorEnable" feature flag is enabled
        updateColor();

        // when pushing down the B button, this runs stage 2 code
        if (oi.copilot.getRawAxis(Buttons.leftTrigger) > .3) {

            if (!tempColor.equals(currentColor)) {
                colorCount++;
                tempColor = currentColor;

            }
            if (colorCount < 30) {
                spinnerMotor.set(ControlMode.PercentOutput, spinnerOutput);
            } else {
                spinnerMotor.set(ControlMode.PercentOutput, 0);
            }
        } else {
            colorCount = 0;

            // when pushing down the A button, this runs the stage 3 code
            if (oi.copilot.getRawAxis(Buttons.rightTrigger) > .3) {
                spinnerMotor.set(ControlMode.PercentOutput, 0.15);
                if (FMScolor.equals("Y")) {
                    if (currentColor.equals("G")) {
                        spinnerMotor.set(ControlMode.PercentOutput, 0);
                    }
                }
                if (FMScolor.equals("B")) {
                    if (currentColor.equals("R")) {
                        spinnerMotor.set(ControlMode.PercentOutput, 0);
                    }
                }
                if (FMScolor.equals("G")) {
                    if (currentColor.equals("Y")) {
                        spinnerMotor.set(ControlMode.PercentOutput, 0);
                    }
                }
                if (FMScolor.equals("R")) {
                    if (currentColor.equals("B")) {
                        spinnerMotor.set(ControlMode.PercentOutput, 0);
                    }
                }
            } else {
                spinnerMotor.set(ControlMode.PercentOutput, 0);
            }
        }
    }

    public Spinner() {
        m_colorSensor = new ColorSensorV3(i2cPort);
    }

    // This is used to find the current color that the sensor is detecting
    public void updateColor() {
         blueColor = m_colorSensor.getBlue();
         greenColor = m_colorSensor.getGreen();
         redColor = m_colorSensor.getRed();
        if (blueColor > greenColor) {
            norm_max = blueColor;
        } else {
            norm_max = greenColor;
        }
        if (redColor > norm_max) {
            norm_max = redColor;
        }

        blueNorm = (255 * (blueColor / norm_max));
        redNorm = (255 * (redColor / norm_max));
        greenNorm = (255 * (greenColor / norm_max));

        redCon = (((redNorm / 128) + ((255 - greenNorm) / 255) + ((255 - blueNorm) / 255))) / 3;
        blueCon = (((blueNorm / 255) + (greenNorm / 255) + ((255 - redNorm) / 255))) / 3;
        greenCon = (((greenNorm / 255) + ((255 - redNorm) / 255) + ((255 - blueNorm) / 255))) / 3;
        yellowCon = (((greenNorm / 255) + (redNorm / 255) + ((255 - blueNorm) / 255))) / 3;

        if ((blueCon > greenCon) && (blueCon > redCon) && (blueCon > yellowCon)) {
            currentColor = "B";
        } else if ((greenCon > blueCon) && (greenCon > redCon) && (greenCon > yellowCon)) {
            currentColor = "G";
        } else if ((redCon > greenCon) && (redCon > blueCon) && (redCon > yellowCon)) {
            currentColor = "R";
        } else {
            currentColor = "Y";
        }

    }
}