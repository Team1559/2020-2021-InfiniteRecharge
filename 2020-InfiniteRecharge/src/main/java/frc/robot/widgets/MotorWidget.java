package frc.robot.widgets;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class MotorWidget
{
    private NetworkTableEntry widget;
    private CANSparkMax motor;
    private ShuffleboardTab tab;
    public MotorWidget(CANSparkMax inputMotor, String widgetName)
    {
        motor = inputMotor;
        tab = Shuffleboard.getTab("Drive Train");
        widget = tab.add(widgetName, 0).withWidget(BuiltInWidgets.kTextView).getEntry();
    }

    public void setOutput()
    {
        double output = motor.get();
        widget.setDouble(output);
    }

    public void changeOutput()
    {
        double output = widget.getDouble(1.0);
        motor.set(output);
    }
}