package frc.robot.components;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class Camera
{
    //UsbCamera cameras = CameraServer.getInstance().startAutomaticCapture(0);
    int cameraId;
    
    public Camera(int id) {
        cameraId = id;
    }

    public void init() {
        CameraServer.getInstance().startAutomaticCapture(cameraId);
    }
}
