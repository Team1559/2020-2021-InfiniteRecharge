package frc.robot.components;

import edu.wpi.first.cameraserver.CameraServer;

public class Camera
{
    int cameraId;
    
    public Camera(int id) {
        cameraId = id;
    }

    public void init() {
        CameraServer.getInstance().startAutomaticCapture(cameraId);
    }
}
