// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static edu.wpi.first.units.Units.Millisecond;
import static frc.robot.Constants.FuelConstants.SPIN_UP_NANOSECONDS;
import static frc.robot.Utilities.millisecondsToNanoseconds;
import static frc.robot.Utilities.secondsToNanoseconds;


/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final class DriveConstants {
    // Motor controller IDs for drivetrain motors
    public static final int LEFT_LEADER_ID = 12;
    public static final int LEFT_FOLLOWER_ID = 13;
    public static final int RIGHT_LEADER_ID = 10;
    public static final int RIGHT_FOLLOWER_ID = 11;

    public static final double ROBOT_PERIODIC_RATE = Millisecond.fromBaseUnits(20);
    // Current limit for drivetrain motors. 60A is a reasonable maximum to reduce
    // likelihood of tripping breakers or damaging CIM motors
    public static final int DRIVE_MOTOR_CURRENT_LIMIT = 60;
  }

  public static final class FuelConstants {
    // Motor controller IDs for Fuel Mechanism motors
    public static final int FEEDER_MOTOR_ID = 15;
    public static final int INTAKE_LAUNCHER_MOTOR_ID = 14;

    // Current limit and nominal voltage for fuel mechanism motors.
    public static final int FEEDER_MOTOR_CURRENT_LIMIT = 60;
    public static final int LAUNCHER_MOTOR_CURRENT_LIMIT = 60;
 
     // roboRIO DIO pins for the shooter encoder
    public static final int SHOOTER_ENCODER_DIO_CHANNEL_A = 8;
    public static final int SHOOTER_ENCODER_DIO_CHANNEL_B = 9;
    public static final double SHOOTER_ENCODER_PULSES_PER_ROTATION = 2038.0;
    // How fast we have observed our shooter turning in rotations per second at 12 volts
    public static final double SHOOTER_MAX_MEASURED_RPS = -74.0;
    // Percentage (e.g. 0.10 = 10%) below max shooter speed that we can start shooting at
    public static final double SHOOTING_SPEED_TOLERANCE = 0.93;
    // agitation time(?), im not sure what exactly to call it just yet.
    public static final long UNHEALTHY_SHOOTER_AGITATE_TIME = millisecondsToNanoseconds(500);
    public static final long UNHEALTHY_COOLDOWN = UNHEALTHY_SHOOTER_AGITATE_TIME + millisecondsToNanoseconds(1500);
    public static final long TIME_WITHOUT_FUEL_UNHEALTHY_DECLARATION = millisecondsToNanoseconds(500);
    // Voltage values for various fuel operations. These values may need to be tuned
    // based on exact robot construction.
    // See the Software Guide for tuning information
    public static final double INTAKING_FEEDER_VOLTAGE = -12;
    public static final double INTAKING_INTAKE_VOLTAGE = 8;
    public static final double LAUNCHING_FEEDER_VOLTAGE = 12;
    public static final double LAUNCHING_LAUNCHER_VOLTAGE = 12;
    public static final double SPIN_UP_FEEDER_VOLTAGE = -6;
    public static final double SPIN_UP_SECONDS_TIMEOUT = 1;
    public static final long SPIN_UP_NANOSECONDS = secondsToNanoseconds(0);
    public static final long SHOOTER_BALL_SHOT_DETECTION_SPEED = -55;
  }

  public static final class OperatorConstants {
    // Port constants for driver and operator controllers. These should match the
    // values in the Joystick tab of the Driver Station software
    public static final int DRIVER_CONTROLLER_PORT = 0;
    public static final int OPERATOR_CONTROLLER_PORT = 1;

    // This value is multiplied by the joystick value when rotating the robot to
    // help avoid turning too fast and beign difficult to control
    public static final double DRIVE_SCALING = .7;
    public static final double ROTATION_SCALING = .8;
  }

  public final static class SmartDashboardConstants {
    public static final String AGITATING_KEY = "Agitating";
    public static final String LAUNCHING_LAUNCHER_ROLLER_VALUE_KEY = "Launching launcher roller value";
    public static final String LAUNCHING_SPINUP_FEEDER_VALUE_KEY = "Launching spin-up feeder value";
    public static final String LAUNCHING_LAUNCHER_ROLLER_TARGET_KEY = "Launching launcher roller target";
    public static final String LAUNCHING_FEEDER_ROLLER_VALUE_KEY = "Launching feeder roller value";
    public static final String SPIN_DURATION_KEY = "SpinDuration";
  }
  
}

