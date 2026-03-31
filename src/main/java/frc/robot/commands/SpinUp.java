// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CANFuelSubsystem;
import static frc.robot.Constants.FuelConstants.*;
import static frc.robot.Constants.SmartDashboardConstants.*;
import static frc.robot.Utilities.nanosecondsToMilliseconds; 

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class SpinUp extends Command {
  /** Creates a new Intake. */

  CANFuelSubsystem fuelSubsystem;

  private long startTimeNano; 

  public SpinUp(CANFuelSubsystem fuelSystem) {
    addRequirements(fuelSystem);
    this.fuelSubsystem = fuelSystem;
    
  }

  // Called when the command is initially scheduled. Set the rollers to the
  // appropriate values for intaking
  @Override
  public void initialize() {
    fuelSubsystem.setIntakeLauncherRoller(SmartDashboard.getNumber(LAUNCHING_LAUNCHER_ROLLER_VALUE_KEY, LAUNCHING_LAUNCHER_VOLTAGE));
    fuelSubsystem.setFeederRoller(SmartDashboard.getNumber(LAUNCHING_SPINUP_FEEDER_VALUE_KEY, SPIN_UP_FEEDER_VOLTAGE));
    SmartDashboard.putBoolean(AGITATING_KEY, true);
    startTimeNano = System.nanoTime();
  }

  // Called every time the scheduler runs while the command is scheduled. This
  // command doesn't require updating any values while running
  @Override
  public void execute() {
  }

  // Called once the command ends or is interrupted. Stop the rollers
  @Override
  public void end(boolean interrupted) {
    SmartDashboard.putBoolean(AGITATING_KEY, false);
    SmartDashboard.putNumber(SPIN_DURATION_KEY, nanosecondsToMilliseconds(System.nanoTime() - startTimeNano)) ;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return fuelSubsystem.shooterAtShootingSpeed();
  }
}
