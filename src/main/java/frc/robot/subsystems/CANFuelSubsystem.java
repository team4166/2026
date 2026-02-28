// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.controller.PIDController;
//import edu.wpi.first.units.measure.AngularVelocity;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.FuelConstants.*;

public class CANFuelSubsystem extends SubsystemBase {
  private final SparkMax feederRoller;
  private final SparkMax intakeLauncherRoller;
    // Initializes an encoder on DIO pins 0 and 1
  // Defaults to 4X decoding and non-inverted
  private final Encoder shooterEncoder = new Encoder(8, 9);
  private final PIDController turnController;
  private final double shooterMaxRPS = 74;
  private final double voltPerRPS = 12/shooterMaxRPS;
  
  /** Creates a new CANBallSubsystem. */
  @SuppressWarnings("removal")
  public CANFuelSubsystem() {
    // create brushed motors for each of the motors on the launcher mechanism
    intakeLauncherRoller = new SparkMax(INTAKE_LAUNCHER_MOTOR_ID, MotorType.kBrushed);
    feederRoller = new SparkMax(FEEDER_MOTOR_ID, MotorType.kBrushed);
    // SmartDashboard.putNumber("Launcher P", 0.1);
    // SmartDashboard.putNumber("Launcher I", 0);
    // SmartDashboard.putNumber("Launcher D", 0);
    turnController = new PIDController(SmartDashboard.getNumber("Launcher P", 0.1), SmartDashboard.getNumber("Launcher I", 0), SmartDashboard.getNumber("Launcher D", 0));
      /// turnController.setTolerance(TurnControlConstants.TURN_TO_ANGLE_THRESHOLD);
      /// 
  shooterEncoder.setDistancePerPulse(1.0 / 2038.0);

    // create the configuration for the feeder roller, set a current limit and apply
    // the config to the controller
    SparkMaxConfig feederConfig = new SparkMaxConfig();
    feederConfig.smartCurrentLimit(FEEDER_MOTOR_CURRENT_LIMIT);
    feederRoller.configure(feederConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // create the configuration for the launcher roller, set a current limit, set
    // the motor to inverted so that positive values are used for both intaking and
    // launching, and apply the config to the controller
    SparkMaxConfig launcherConfig = new SparkMaxConfig();
    launcherConfig.inverted(true);
    launcherConfig.smartCurrentLimit(LAUNCHER_MOTOR_CURRENT_LIMIT);
    intakeLauncherRoller.configure(launcherConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    // put default values for various fuel operations onto the dashboard
    // all commands using this subsystem pull values from the dashbaord to allow
    // you to tune the values easily, and then replace the values in Constants.java
    // with your new values. For more information, see the Software Guide.
    SmartDashboard.putNumber("Intaking feeder roller value", INTAKING_FEEDER_VOLTAGE);
    SmartDashboard.putNumber("Intaking intake roller value", INTAKING_INTAKE_VOLTAGE);
    SmartDashboard.putNumber("Launching feeder roller value", LAUNCHING_FEEDER_VOLTAGE);
    SmartDashboard.putNumber("Launching launcher roller value", LAUNCHING_LAUNCHER_VOLTAGE);
    SmartDashboard.putNumber("Spin-up feeder roller value", SPIN_UP_FEEDER_VOLTAGE);
  }

  // A method to set the voltage of the intake roller
  public void setIntakeLauncherRoller(double voltage) {
    // SmartDashboard.putNumber("ShooterEncoderRate", shooterEncoder.getRate());

    intakeLauncherRoller.setVoltage(voltage);
  }
  public void setControlledIntakeLauncherRoller(double target) {
    // SmartDashboard.putNumber("ShooterEncoderRate", shooterEncoder.getRate());
    double feedback = turnController.calculate(shooterEncoder.getRate(), target);
    SmartDashboard.putNumber("ShooterEncoderFeedback", feedback);
    //target volts set to opposite
    double targetVolts =feedback * voltPerRPS *-1;
    SmartDashboard.putNumber("ShooterControllertargetVolts", targetVolts);
    setIntakeLauncherRoller(targetVolts);
  }     
  // A method to set the voltage of the intake roller
  public void setFeederRoller(double voltage) {
    feederRoller.setVoltage(voltage);
  }
  
  // A method to stop the rollers
  public void stop() {
    feederRoller.set(0);
    intakeLauncherRoller.set(0);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    SmartDashboard.putNumber("ShooterEncoderRate", shooterEncoder.getRate());
    SmartDashboard.putNumber("ShooterEncoderDistance", shooterEncoder.getDistance());
    SmartDashboard.putNumber("ShooterEncoderCount", shooterEncoder.get());

  }
}
