// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.core.CorePigeon2;

//https://maven.ctr-electronics.com/release/com/ctre/phoenix/Phoenix5-frc2024-latest.json v5 not v6


/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  
   // Create a new Pigeon2 object with device ID 1
   CorePigeon2 pigeon = new CorePigeon2(13);
   //CorePigeon2 pigeon = new CorePigeon2(13,"Canivore");
   
   // Configure the Pigeon2 for basic use
   Pigeon2Configuration configs = new Pigeon2Configuration();

   private int m_HeartbeatCounter = 0;
   private final int kUpdateLogHeartbeatInterval = 50;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    System.out.println("robotInit: Start Pigeon config.");
    
    configs.MountPose.MountPoseYaw = 0;
    //configs.MountPose.MountPosePitch = 90;  // Use 90 if Pigeon is mounted X-up, so we should mount-pose with Pitch at 90 degrees- Not true
    configs.MountPose.MountPosePitch = 0;  // Must not be 90 to avoid gimble lock which messes up the yaw calculation. We have pigeon flat (z-up), 
                                           // so set to zeroi.  If mount x-axis up then want to be 90 here. 
    configs.MountPose.MountPoseRoll = 0;
    // This Pigeon has no need to trim the gyro
    configs.GyroTrim.GyroScalarX = 0;
    configs.GyroTrim.GyroScalarY = 0;
    configs.GyroTrim.GyroScalarZ = 0;
    // We want the thermal comp and no-motion cal enabled, with the compass disabled for best behavior
    configs.Pigeon2Features.DisableNoMotionCalibration = false;
    configs.Pigeon2Features.DisableTemperatureCompensation = false;
    configs.Pigeon2Features.EnableCompass = false;
    
    // Write these configs to the Pigeon2
    pigeon.getConfigurator().apply(configs);
  
      // Set the yaw to 0 degrees for initial use
    pigeon.setYaw(0);
    System.out.println("robotInit: End Pigeon config.");
    System.out.println("Start the test by Enable in Teleop.");
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {

  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    double yaw = pigeon.getYaw().getValueAsDouble();
    double pitch = pigeon.getPitch().getValueAsDouble();
    double roll = pigeon.getRoll().getValueAsDouble();

    // Print the yaw value
    if (m_HeartbeatCounter++ % kUpdateLogHeartbeatInterval == 0) { 
      int seconds = (m_HeartbeatCounter / kUpdateLogHeartbeatInterval);
      System.out.println(
        "Seconds: " + seconds + 
        " Yaw: " + yaw + 
        "  Pitch: " + pitch + 
        "  Roll: " + roll);

        // "Seconds: " + String.format("%005d",seconds)  + 
        // " Yaw: " + String.format("%5.3f",yaw) + 
        // "  Pitch: " + String.format("%5.3f",pitch) + 
        // "  Roll: " + String.format("%5.3",roll));
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {}

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
