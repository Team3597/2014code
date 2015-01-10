package edu.wpi.first.wpilibj.templates;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
    // For example to map the left and right motors, you could define the
    // following variables to use with your drivetrain subsystem.
    
//    PWMS
    public static final int frontrightMotor = 1; 
    public static final int frontleftMotor = 2;     
    public static final int backleftMotor = 3;
    public static final int backrightMotor = 4;
    public static final int liftMotorCIM = 5;
    public static final int BeltLiftMotor =  6;
    public static final int ShooterMotor = 7;

//    Relays
    public static final int BeltSpinRelay = 1;
        
//    Digital Inputs
    public static final int frontleftEncoderA = 2; //Blue is A, which is in 2 and hard to switch
    public static final int frontleftEncoderB = 1;
    public static final int ShooterEncoderA = 4; //4
    public static final int ShooterEncoderB = 3; //3
    
    public static final int ShooterSwitch1 = 5;
    
    public static final int GrabberEncoderA = 6;
    public static final int GrabberEncoderB =7;
    
    public static final int BeltLiftTopSwitch = 12; 
    public static final int BeltLiftBottomSwitch = 10;
    public static final int GrabberBottomSwitch = 13;
    public static final int GrabberTopSwitch = 14;
    
    
    
//    
    
}
