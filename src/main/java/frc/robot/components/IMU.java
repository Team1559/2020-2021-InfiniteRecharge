package frc.robot.components;
//imports
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;

public  class IMU{
    AHRS ahrs;
    public double roll = 0;
    public double pitch = 0;
    public double yaw = 0;
    public double x_acceleration = 0;
    public double y_acceleration = 0;
    public double z_acceleration = 0;
    public double y_angularVelocity = 0;
    public double turnRate;
    public double maxAutoYaw = 60;// messured in degrees, may change

  //the init method
    public void init(){
      try {
        ahrs = new AHRS(SPI.Port.kMXP);
        ahrs.enableLogging(true);
      }
      catch (RuntimeException ex) {
         DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
       }
        
    }

    //sets the yaw to zero
    public void zeroYaw(){
      ahrs.zeroYaw();
      yaw = 0;
    }

    //returns true if the yaw is between the valid range
    public boolean isYawValid(){
      if(ahrs.getYaw() < maxAutoYaw && ahrs.getYaw() > -maxAutoYaw){
        return true;
      }
      else{
        return false;
      }
    }

  //gets all the yaw values
  public void getvalues(){
    x_acceleration = ahrs.getWorldLinearAccelX();
    y_acceleration = ahrs.getWorldLinearAccelY();
    z_acceleration = ahrs.getWorldLinearAccelZ();
    roll = ahrs.getPitch();
    pitch = ahrs.getRoll();
    yaw = -(ahrs.getYaw()*Math.PI/180.0);// normally negated
    turnRate = ahrs.getRate(); 
    y_angularVelocity = ahrs.getRate();
   }
         
    /*the following are all the functions avalible*/

    //SmartDashboard.putNumber("IMU_Yaw", ahrs.getYaw());
    //SmartDashboard.putNumber("IMU_Pitch", ahrs.getRoll());
    //SmartDashboard.putNumber("IMU_Roll", ahrs.getPitch());
    
    //SmartDashboard.putNumber("IMU_CompassHeading", ahrs.getCompassHeading());

    /* Display 9-axis Heading (requires magnetometer calibration to be useful) */
   // SmartDashboard.putNumber("IMU_FusedHeading", ahrs.getFusedHeading());

    /* These functions are compatible w/the WPI Gyro Class, providing a simple */
    /* path for upgrading from the Kit-of-Parts gyro to the navx MXP */

    // SmartDashboard.putNumber("IMU_TotalYaw", ahrs.getAngle());
    // SmartDashboard.putNumber("IMU_YawRateDPS", ahrs.getRate());

    /* Display Processed Acceleration Data (Linear Acceleration, Motion Detect) */

    ///////////////////////////////////////////////SmartDashboard.putNumber("IMU_Accel_X", ahrs.getWorldLinearAccelX());
    ///////////////////////////////////////////////SmartDashboard.putNumber("IMU_Accel_Z", ahrs.getWorldLinearAccelZ());
    ///////////////////////////////////////////////SmartDashboard.putNumber("IMU_Accel_Y", ahrs.getWorldLinearAccelY());
    // SmartDashboard.putBoolean("IMU_IsMoving", ahrs.isMoving());
    // SmartDashboard.putBoolean("IMU_IsRotating", ahrs.isRotating());

    /* Display estimates of velocity/displacement. Note that these values are */
    /* not expected to be accurate enough for estimating robot position on a */
    /* FIRST FRC Robotics Field, due to accelerometer noise and the compounding */
    /* of these errors due to single (velocity) integration and especially */
    /* double (displacement) integration. */

    // SmartDashboard.putNumber("Velocity_X", ahrs.getVelocityX());
    // SmartDashboard.putNumber("Velocity_Y", ahrs.getVelocityY());
    // SmartDashboard.putNumber("Displacement_X", ahrs.getDisplacementX());
    // SmartDashboard.putNumber("Displacement_Y", ahrs.getDisplacementY());

    /* Display Raw Gyro/Accelerometer/Magnetometer Values */
    /* NOTE: These values are not normally necessary, but are made available */
    /* for advanced users. Before using this data, please consider whether */
    /* the processed data (see above) will suit your needs. */

    // SmartDashboard.putNumber("RawGyro_X", ahrs.getRawGyroX());
    // SmartDashboard.putNumber("RawGyro_Y", ahrs.getRawGyroY());
    // SmartDashboard.putNumber("RawGyro_Z", ahrs.getRawGyroZ());
    // SmartDashboard.putNumber("RawAccel_X", ahrs.getRawAccelX());
    // SmartDashboard.putNumber("RawAccel_Y", ahrs.getRawAccelY());
    // SmartDashboard.putNumber("RawAccel_Z", ahrs.getRawAccelZ());
    // SmartDashboard.putNumber("RawMag_X", ahrs.getRawMagX());
    // SmartDashboard.putNumber("RawMag_Y", ahrs.getRawMagY());
    // SmartDashboard.putNumber("RawMag_Z", ahrs.getRawMagZ());
    // SmartDashboard.putNumber("IMU_Temp_C", ahrs.getTempC());
    // SmartDashboard.putNumber("IMU_Timestamp", ahrs.getLastSensorTimestamp());

    /* Omnimount Yaw Axis Information */
    /* For more info, see http://navx-mxp.kauailabs.com/installation/omnimount */
    
    // SmartDashboard.putString("YawAxisDirection", yaw_axis.up ? "Up" : "Down");
    // SmartDashboard.putNumber("YawAxis", yaw_axis.board_axis.getValue());

    /* Sensor Board Information */
    //SmartDashboard.putString("FirmwareVersion", ahrs.getFirmwareVersion());

    /* Quaternion Data */
    /* Quaternions are fascinating, and are the most compact representation of */
    /* orientation data. All of the Yaw, Pitch and Roll Values can be derived */
    /* from the Quaternions. If interested in motion processing, knowledge of */
    /* Quaternions is highly recommended. */
    //SmartDashboard.putNumber("QuaternionW", ahrs.getQuaternionW());
    //SmartDashboard.putNumber("QuaternionX", ahrs.getQuaternionX());
    //SmartDashboard.putNumber("QuaternionY", ahrs.getQuaternionY());
    //SmartDashboard.putNumber("QuaternionZ", ahrs.getQuaternionZ());
}