package frc.robot.components;

import edu.wpi.first.wpilibj.Compressor;
import frc.robot.Wiring;

public class CompressorControl{
    public Compressor airCompressor;

    public void init(){
        airCompressor = new Compressor();
    }
    public void enable(){
        System.out.println(airCompressor.getCompressorNotConnectedFault() + "       *" + airCompressor.getCompressorShortedFault()+ airCompressor.getPressureSwitchValue() );
        System.out.println(airCompressor.enabled() );
        airCompressor.setClosedLoopControl(true);
    }
    public void disable(){
        airCompressor.setClosedLoopControl(false);
    }
}