package frc.robot.components;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.OperatorInterface;

public class Cameras
{
    UsbCamera camera1;
    UsbCamera camera2;
    OperatorInterface oi;
    NetworkTableEntry cameraSelection;
    
    public Cameras(OperatorInterface Oi) {
        oi = Oi;
        camera1 = CameraServer.getInstance().startAutomaticCapture(0);
        camera2 = CameraServer.getInstance().startAutomaticCapture(1);
    
        cameraSelection = NetworkTableInstance.getDefault().getTable("").getEntry("CameraSelection");
    }
    
    public void cameraView() {
        if (oi.DPad() == -1) {
            System.out.println("Setting camera 2");
            cameraSelection.setString(camera2.getName());
        } else{
            System.out.println("Setting camera 1");
            cameraSelection.setString(camera1.getName());
        }
    }
}