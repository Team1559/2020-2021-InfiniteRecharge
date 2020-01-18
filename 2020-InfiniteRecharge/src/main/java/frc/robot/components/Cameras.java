package frc.robot.components;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;


public class Cameras
{
    private UsbCamera camera;

    public Cameras()
    {
        camera = CameraServer.getInstance().startAutomaticCapture(0);
        
    }

}