package frc.robot.components;

import edu.wpi.first.wpilibj.Compressor;

public class CompressorControl{

    public Compressor airCompressor;

    public void init(){
        airCompressor = new Compressor();
    }
    public void enable(){
        airCompressor.setClosedLoopControl(true);
    }
    public void disable(){
        airCompressor.setClosedLoopControl(false);
    }
}