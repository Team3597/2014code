package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.commands.DriveWithJoystick;
import edu.wpi.first.wpilibj.templates.commands.FollowTarget;

/**
 *
 */
public class Driver extends Subsystem {
    
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
    
    public Driver() {
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
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
//        setDefaultCommand(new FollowTarget());
        setDefaultCommand(new DriveWithJoystick());
    }
    public void DriveWithJoystick() {
        Joystick DriverJoystick = CommandBase.oi.getDriverStick();
        if (DriverJoystick.getRawButton(5)) { // allows tankdrive while button is held
            Drive.tankDrive(DriverJoystick.getRawAxis(2), -DriverJoystick.getRawAxis(5));
        }
        else {
            double rotationValue = DriverJoystick.getRawAxis(3);
            Drive.mecanumDrive_Cartesian(-DriverJoystick.getRawAxis(1), DriverJoystick.getRawAxis(2), rotationValue,0);
        }
        SmartDashboard.putNumber("count", frontleftEncoder.get());
    }
    
    public void DriveWithCamera() {
        SmartDashboard.putNumber("Target X Coordinate:", CommandBase.camera.Xcoord);   
        if (CommandBase.camera.Xcoord == -1){
            Drive.mecanumDrive_Cartesian(0,0, .2,0); //rotate slowly
        }
        else if (CommandBase.camera.Xcoord > 160) {
            Drive.mecanumDrive_Cartesian(0,0, -.2,0); //don't move, only rotate right
        }
        else if (CommandBase.camera.Xcoord < 160) {
            Drive.mecanumDrive_Cartesian(0,0, .23,0); //don't move, only rotate left
        }
    }
    public void DriveWithEncoders() {
        Drive.mecanumDrive_Cartesian(0,-.2,0,0); // towards jags
    }
    public void reset(){
        frontleftEncoder.reset();
    }
    public void rotate(boolean button){
        if (button == false){
            Drive.mecanumDrive_Cartesian(0, 0, -.2, 0); // right
        }
        else{
            Drive.mecanumDrive_Cartesian(0, 0, .23, 0); // left
        }        
    }
    public boolean isCameraRotateFinished(){
        double maxStoppingValue = 0;
        if (CommandBase.camera.Xcoord == -1) {
            maxStoppingValue = (27*RotateRatio); // if no target, turn most of a full frame to find one. Not all the way or a half target on one side will be a half target on the other.
        }
        else {
            double pixelsToTravel = Math.abs(CommandBase.camera.Xcoord-160); // absolute difference between
            double degreesToTravel = pixelsToTravel*5.0/80.0; //degrees = pixels * ratio of degrees per pixel
            SmartDashboard.putNumber("Degrees to Turn", degreesToTravel);
            maxStoppingValue = (degreesToTravel*RotateRatio); //degrees times ratio of pulses per degree
        }
        int encoderValue = frontleftEncoder.get();
        
        if (Math.abs(encoderValue) >= maxStoppingValue){
            return true;
        }
        else{
            return false;
        }
        
    }  
    
    public boolean isRotateFinished(){
        double maxStoppingValue = 0;
        maxStoppingValue = (20*RotateRatio)-70; // if no target, turn almost a full frame to find one
        int encoderValue = frontleftEncoder.get();
        if (Math.abs(encoderValue) >= maxStoppingValue){
            return true;
        }
        else{
            return false;
        }
    } 
    
    public boolean isEncoderFinished(){ 
        double maxStoppingValue = CommandBase.camera.distance-.8; // for normal coasting distance
        double encoderValue = frontleftEncoder.getDistance();
        if (CommandBase.camera.Xcoord == -1){
            return true;
        }
        if (Math.abs(encoderValue) >= maxStoppingValue){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean isEncoderConstantFinished(){ 
        double maxStoppingValue = 3-.8; // for normal coasting distance
        double encoderValue = frontleftEncoder.getDistance();
        if (CommandBase.camera.Xcoord == -1){
            return true;
        }
        if (Math.abs(encoderValue) >= maxStoppingValue){
            return true;
        }
        else{
            return false;
        }
    }
}
