package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Encoder;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
//    // For example to map the left and right motors, you could define the
//    // following variables to use with your drivetrain subsystem.
     public static final int frontleftMotor = 1;
     public static final int frontrightMotor = 2;
     public static final int backleftMotor = 3;
     public static final int backrightMotor = 4;
     
     public static final int frontleftEncoderA = 1;
     public static final int frontleftEncoderB = 2;
     public static final int frontrightEncoderA = 3;
     public static final int frontrightEncoderB = 4;
     public static final int backleftEncoderA = 5;
     public static final int backleftEncoderB = 6;
     public static final int backrightEncoderA = 7;
     public static final int backrightEncoderB = 8;

//    
//    // If you are using multiple modules, make sure to define both the port
//    // number and the module. For example you with a rangefinder:
//    // public static final int rangefinderPort = 1;
//    // public static final int rangefinderModule = 1;
}
