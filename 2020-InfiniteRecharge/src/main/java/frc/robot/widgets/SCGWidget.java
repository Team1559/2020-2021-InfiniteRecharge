package frc.robot.widgets;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public class SCGWidget
{
    private NetworkTableEntry widget;
    private SpeedControllerGroup motorGroup;
    private ShuffleboardTab tab;
    public SCGWidget(SpeedControllerGroup SCG, String widgetName)
    {
        motorGroup = SCG;
        tab = Shuffleboard.getTab("Drive Train");
        widget = tab.add(widgetName,0).withWidget(BuiltInWidgets.kTextView).getEntry();
    }

    public void changeOutput()
    {
        double output = widget.getDouble(0.0);
        motorGroup.set(output);
    }
}