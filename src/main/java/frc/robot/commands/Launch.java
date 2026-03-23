// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CANFuelSubsystem;

import java.awt.*;

import static frc.robot.Constants.FuelConstants.*;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class Launch extends Command {
  /** Creates a new Intake. */

  CANFuelSubsystem fuelSubsystem;
  private long lastUnhealthyAt;
  private final long launchStartTime;

  public Launch(CANFuelSubsystem fuelSystem) {
    addRequirements(fuelSystem);
    this.fuelSubsystem = fuelSystem;
    lastUnhealthyAt = 0;
    launchStartTime = System.nanoTime();
  }

  // Called when the command is initially scheduled. Set the rollers to the
  // appropriate values for intaking
  @Override
  public void initialize() {
    fuelSubsystem.setFeederRoller(SmartDashboard.getNumber("Launching feeder roller value", LAUNCHING_FEEDER_VOLTAGE));
  }

  // Called every time the scheduler runs while the command is scheduled. This
  // command doesn't require updating any values while running
  @Override
  public void execute() {
    fuelSubsystem
        .setIntakeLauncherRoller(
            SmartDashboard.getNumber("Launching launcher roller target", LAUNCHING_LAUNCHER_VOLTAGE));

    long now = System.nanoTime();

    if (now - launchStartTime >= TIME_WITHOUT_BALL_UNHEALTHY_DECLARATION && now - fuelSubsystem.getLastFuelSeenAt() >= UNHEALTHY_COOLDOWN) {
      // Only update this time if we don't already know its unhealthy (UNHEALTHY_COOLDOWN) and it's been TIME_WITHOUT_BALL_UNHEALTHY_DECLARATION since starting
      lastUnhealthyAt = System.nanoTime();
    }

    if (now - lastUnhealthyAt <= UNHEALTHY_SHOOTER_AGITATE_TIME) {
      fuelSubsystem.setFeederRoller(SmartDashboard.getNumber("Launching spin-up feeder value", SPIN_UP_FEEDER_VOLTAGE));
    } else {
      fuelSubsystem.setFeederRoller(SmartDashboard.getNumber("Launching feeder roller value", LAUNCHING_FEEDER_VOLTAGE));
    }
  }

  // Called once the command ends or is interrupted. Stop the rollers
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
