// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.FuelConstants.*;

public class CANFuelSubsystem extends SubsystemBase {
  private final SparkMax feederRoller;
  private final SparkMax intakeLauncherRoller;
  private final Encoder shooterEncoder;
  private final AnalogInput shooterChannelSonar;
  private double voltageScaleFactor;
  private long lastFuelSeenAt;
  private double intakeLauncherSetPoint;
  private long intakeLauncherSetChangeTime;

  /** Creates a new CANBallSubsystem. */
  @SuppressWarnings("removal")
  public CANFuelSubsystem() {
    // create brushed motors for each of the motors on the launcher mechanism
    intakeLauncherRoller = new SparkMax(INTAKE_LAUNCHER_MOTOR_ID, MotorType.kBrushed);
    feederRoller = new SparkMax(FEEDER_MOTOR_ID, MotorType.kBrushed);
    shooterEncoder = new Encoder(SHOOTER_ENCODER_DIO_CHANNEL_A, SHOOTER_ENCODER_DIO_CHANNEL_B);
    shooterEncoder.setDistancePerPulse(1.0 / SHOOTER_ENCODER_PULSES_PER_ROTATION);

    shooterChannelSonar = new AnalogInput(SHOOTER_CHANNEL_SONAR_PIN);
    voltageScaleFactor = 1;

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
    if (intakeLauncherSetPoint != voltage) {
      intakeLauncherSetPoint = voltage;
      intakeLauncherSetChangeTime = System.nanoTime();
    }
    intakeLauncherRoller.setVoltage(voltage);
  }

  // A method to set the voltage of the intake rollerSONAR_CENTIMETER_SCALING
  public void setFeederRoller(double voltage) {
    feederRoller.setVoltage(voltage);
  }

  private double getSonarDistance() {
    return shooterChannelSonar.getValue() * voltageScaleFactor * SONAR_CENTIMETER_SCALING;
  }

  public long getLastFuelSeenAt() {
    return lastFuelSeenAt;
  }
  
  // A method to stop the rollers
  public void stop() {
    feederRoller.set(0);
    intakeLauncherRoller.set(0);
  }

  public boolean shooterAtShootingSpeed() {
    return shooterEncoder.getRate() >= SHOOTER_MAX_MEASURED_RPS * SHOOTING_SPEED_TOLERANCE;
  }

  @Override
  public void periodic() {
    double encoderRate = shooterEncoder.getRate();

    // This method will be called once per scheduler run
    SmartDashboard.putNumber("ShooterEncoderRate", encoderRate);
    SmartDashboard.putNumber("ShooterEncoderDistance", shooterEncoder.getDistance());
    SmartDashboard.putNumber("ShooterEncoderCount", shooterEncoder.get());
    SmartDashboard.putNumber("ShooterSonarDistance", getSonarDistance());

    long now = System.nanoTime();

    boolean launcherIsActive = intakeLauncherSetPoint != 0 && now - intakeLauncherSetChangeTime > SPIN_UP_NANOSECONDS;
    boolean fuelHasBeenShot = encoderRate > SHOOTER_BALL_SHOT_DETECTION_SPEED;

    if (launcherIsActive && fuelHasBeenShot) {
      // Can be used in any subsystem to determine if we need to agitate
      // This might catch some after our fast spin-up finishes (due to the encoder check), but that's fine. We don't want to agitate that quickly anyway.
      lastFuelSeenAt = now;
      SmartDashboard.putNumber("LastFuelSeenAt", lastFuelSeenAt);
    }

    voltageScaleFactor = 5 / RobotController.getVoltage5V();
  }
}
