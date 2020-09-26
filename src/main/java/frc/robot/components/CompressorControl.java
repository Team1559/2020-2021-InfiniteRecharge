package frc.robot.components;

import edu.wpi.first.wpilibj.Compressor;
import frc.robot.Wiring;
public class CompressorControl{
    public Compressor airCompressor;
    public boolean useCompressor = true;
    
    public void init(){
        airCompressor = new Compressor(Wiring.compressor);
    }
    public void run(){
        
            airCompressor.setClosedLoopControl(true);
        
          
        
    }
    public void disable(){
        airCompressor.setClosedLoopControl(false);
    }
}