package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.ChassisDriveWithJoystick;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;

/**
 *
 */
public class Chassis extends Subsystem {
    
    Jaguar frontleftMotor;
    Jaguar frontrightMotor;
    Jaguar backleftMotor;
    Jaguar backrightMotor;
    Encoder frontleftEncoder;
    RobotDrive Drive;
    boolean runOnce = false;
    double RotateRatio = 15.6; // 15.6 pulses per degree ???????????????????????????????
    double StraightRatio = 1.0/360.0;
    
    public Chassis() {
        frontleftMotor = new Jaguar(RobotMap.frontleftMotor);
        frontrightMotor = new Jaguar(RobotMap.frontrightMotor);
        backleftMotor = new Jaguar(RobotMap.backleftMotor);
        backrightMotor = new Jaguar(RobotMap.backrightMotor);
        frontleftEncoder = new Encoder(RobotMap.frontleftEncoderA,RobotMap.frontleftEncoderB);
        frontleftEncoder.setDistancePerPulse(StraightRatio);
        frontleftEncoder.start();
        
        Drive = new RobotDrive(frontleftMotor, frontrightMotor, backleftMotor, backrightMotor);
        Drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
        Drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        Drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
        Drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new ChassisDriveWithJoystick());
    }
    public void DriveWithJoystick() {
        SmartDashboard.putNumber("Wheel Encoder", frontleftEncoder.getDistance());
        SmartDashboard.putNumber("Wheel Speed", frontleftMotor.get());
        Joystick ChassisJoystick = CommandBase.oi.getDriverJoystick();
        Drive.mecanumDrive_Cartesian(-ChassisJoystick.getRawAxis(1), -ChassisJoystick.getRawAxis(2), -ChassisJoystick.getRawAxis(3),0);
    }
    public void rotate(boolean button){
        if (button == false){
            Drive.mecanumDrive_Cartesian(0, 0, -.2, 0); // right
        }
        else{
            Drive.mecanumDrive_Cartesian(0, 0, .23, 0); // left
        }        
    }
    public void DriveWithEncoders() {
        if (runOnce == false){
            Drive.mecanumDrive_Cartesian(0,.7,0,0); // forwards 
        }
    }
    public boolean isEncoderConstantFinished(){ 
        if (runOnce == true){ // don't move more than once
            return true;
        }
        SmartDashboard.putNumber("Wheel Encoder", frontleftEncoder.getDistance());
        double maxStoppingValue = 3-.8; // for normal coasting distance 
        double encoderValue = frontleftEncoder.getDistance();
        if (Math.abs(encoderValue) >= maxStoppingValue){
            return true;
        }
        else {
            return false;
        }
    }
    public void DriveWithCamera() {
//        if (runOnce == false) {
//            SmartDashboard.putNumber("Target X Coordinate:", CommandBase.camera.Xcoord);   
//            if (CommandBase.camera.Xcoord == -1){
//                SmartDashboard.putString("", "target not found");
//            }
//            else if (CommandBase.camera.Xcoord > 120) {
//                Drive.mecanumDrive_Cartesian(0,0, -.2,0); //don't move, only rotate right
//            }
//            else if (CommandBase.camera.Xcoord < 120) {
//                Drive.mecanumDrive_Cartesian(0,0, .23,0); //don't move, only rotate left
//            }
//        }
    }
    public boolean isCameraRotateFinished(){
        return true;
//        if (runOnce == true){
//            return true; //stop immediately if already cycled through autonomous once
//        }
//        double maxStoppingValue = 0;
//        if (CommandBase.camera.Xcoord == -1) {
//            return true;
//        }
//        else {
//            double pixelsToTravel = Math.abs(CommandBase.camera.Xcoord-120); // absolute difference between
//            double degreesToTravel = pixelsToTravel*5.0/80.0; //degrees = pixels * ratio of degrees per pixel
//            SmartDashboard.putNumber("Degrees to Turn", degreesToTravel);
//            maxStoppingValue = (degreesToTravel*RotateRatio); //degrees times ratio of pulses per degree
//        }
//        int encoderValue = frontleftEncoder.get();
//        if (Math.abs(encoderValue) >= maxStoppingValue){
//            return true;
//        }
//        else{
//            return false;
//        }
    }
    public void changeRunOnce(){
        runOnce = true; // for autonomous, only one loop of following the target needed. Uses autonomous command group, not Follow Target
    }
    public void reset(){
        frontleftEncoder.reset();
    }
}
