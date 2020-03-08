package frc.robot.components;

import edu.wpi.first.wpilibj.Compressor;
import frc.robot.Wiring;
public class CompressorControl{
    public Compressor airCompressor;
    public boolean useCompressor;
    
    public void init(){
        airCompressor = new Compressor(Wiring.compressor);
    }
    public void run(){
        if(useCompressor){
            airCompressor.setClosedLoopControl(true);
          }
          else {
            disable();
          }
        
    }
    public void disable(){
        airCompressor.setClosedLoopControl(false);
    }
}