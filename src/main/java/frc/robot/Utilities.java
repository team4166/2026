package frc.robot;


public class Utilities {
  public static long millisecondsToNanoseconds(long milliseconds) {
    return milliseconds * 1000000;
  }

  public static long secondsToNanoseconds(double seconds) {
    return millisecondsToNanoseconds((long) (seconds * 1000));
  }
}