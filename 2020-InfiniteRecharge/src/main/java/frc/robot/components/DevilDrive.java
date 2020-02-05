/* Devil Drive is a specially designed drive system that will enable us to drive our motors
* in velocity mode instead of percent output mode, which is the default for all standard drive code given by WPI.
*/
package frc.robot.components;

import edu.wpi.first.wpilibj.SpeedController;

class DevilDrive
{
    private SpeedController leftMotors;
    private SpeedController rightMotors;
    private int maxVelocity;
    private boolean squareInputs;

    public void DevilDriveInit(SpeedController lM, SpeedController rM, int mV, boolean sI)
    {
        leftMotors = lM;
        rightMotors = rM;
        maxVelocity = mV;
        squareInputs = sI;
    }

    public void DevilDriveInit(SpeedController lM, SpeedController rM, int mV)
    {
        DevilDriveInit(lM,rM,mV,true);
    }

    public void DevilScottDrive(double forwardSpeed, double backwardSpeed, double zRotation)
    {
        if(forwardSpeed > 0 && backwardSpeed <= 0.01 && zRotation == 0)
        {
            leftMotors.set(forwardSpeed*maxVelocity);
            rightMotors.set(forwardSpeed*maxVelocity);
        }
        else if(forwardSpeed <= 0.01 && backwardSpeed > 0 && zRotation == 0)
        {
            leftMotors.set(backwardSpeed*maxVelocity*-1);
            rightMotors.set(backwardSpeed*maxVelocity*-1);
        }
        else if(forwardSpeed == backwardSpeed && zRotation == 0)
        {
            leftMotors.set(0);
            rightMotors.set(0);
        }
        else if(forwardSpeed > backwardSpeed && backwardSpeed > 0 && zRotation == 0)
        {
            leftMotors.set()
        }
    }
}