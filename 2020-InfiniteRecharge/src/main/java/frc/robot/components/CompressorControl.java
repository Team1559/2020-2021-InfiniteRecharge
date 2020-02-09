package frc.robot.components;

import edu.wpi.first.wpilibj.Compressor;
import frc.robot.Wiring;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Config;

public class CompressorControl implements Loggable{
    public Compressor airCompressor;
    public boolean useCompressor;
    @Config
    public void useCompressor(boolean enable){
        useCompressor = enable;
    }
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