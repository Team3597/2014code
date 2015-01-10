
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.commands.DriveWithJoystick;

/**
 *
 */
public class Chassis extends Subsystem {
    Jaguar frontleftMotor;
    Jaguar frontrightMotor;
    Jaguar backleftMotor;
    Jaguar backrightMotor;
    Encoder frontleftEncoder;
//    Encoder frontrightEncoder;
//    Encoder backleftEncoder;
//    Encoder backrightEncoder;
    double RotateRatio = 15.6; // 15.6 pulses per degree
    
    RobotDrive Drive;  
    double distance = 0;
    boolean runOnce = false;
    
// here. Call these from Commands.
    public Chassis() {
        frontleftMotor = new Jaguar(RobotMap.frontleftMotor);
        frontrightMotor = new Jaguar(RobotMap.frontrightMotor);
        backleftMotor = new Jaguar(RobotMap.backleftMotor);
        backrightMotor = new Jaguar(RobotMap.backrightMotor);
        // perhaps we can control with only knowing the distance of one wheel.
        frontleftEncoder = new Encoder(RobotMap.frontleftEncoderA,RobotMap.frontleftEncoderB);
        frontleftEncoder.setDistancePerPulse(1.0/360.0); //360 = 1 ft
        frontleftEncoder.start();

//        frontrightEncoder = new Encoder(RobotMap.frontrightEncoderA,RobotMap.frontrightEncoderB);
//        backleftEncoder = new Encoder(RobotMap.backleftEncoderA,RobotMap.backleftEncoderB);
//        backrightEncoder = new Encoder(RobotMap.backrightEncoderA,RobotMap.backrightEncoderB);

        Drive = new RobotDrive(frontleftMotor, frontrightMotor, backleftMotor, backrightMotor);
        Drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
        Drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        Drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
        Drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new DriveWithJoystick());
    }
    
    public void driveWithJoystick(){
        Joystick DriverJoystick = CommandBase.oi.getDriverJoystick();
        if (DriverJoystick.getRawButton(5)) { // allows tankdrive while button is held
            Drive.tankDrive(DriverJoystick.getRawAxis(2), -DriverJoystick.getRawAxis(5));
        }
        else {
            double rotationValue = DriverJoystick.getRawAxis(3);
            double sideValue = -DriverJoystick.getRawAxis(1);
            double forwardBackValue = DriverJoystick.getRawAxis(2);
            Drive.mecanumDrive_Cartesian(sideValue, forwardBackValue, rotationValue,0);
        }    
    }
}

