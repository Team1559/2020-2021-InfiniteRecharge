package frc.robot.components;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource.ConnectionStrategy;
import edu.wpi.first.cameraserver.CameraServer;
import frc.robot.OperatorInterface;

public class Cameras
{
UsbCamera camera1;
UsbCamera camera2;
VideoSink server;
OperatorInterface oi;
    public Cameras(OperatorInterface Oi)
    {
        oi = Oi;
        camera1 = CameraServer.getInstance().startAutomaticCapture(0);
        camera2 = CameraServer.getInstance().startAutomaticCapture(1);
        server = CameraServer.getInstance().getServer();

        camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
        camera2.setConnectionStrategy(ConnectionStrategy.kKeepOpen);

        
    }
    public void cameraView()
    {
        if (oi.pilot.getTriggerPressed()) {
            System.out.println("Setting camera 2");
            server.setSource(camera2);
        } else if (oi.pilot.getTriggerReleased()) {
            System.out.println("Setting camera 1");
            server.setSource(camera1);
        }
    }
}