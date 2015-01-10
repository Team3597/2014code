/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.commands.ShooterMoveWithJoystick;

/**
 *
 * @author trobinson
 */
public class Shooter extends Subsystem {
    Jaguar ShooterMotor;
    Encoder ShooterEncoder;
    DigitalInput ShooterSwitch1;
    boolean runOnce = false;
    int lockingDistance = 42700;
    int haltingDistance = 3000; // was set to 3500

    public Shooter(){
        ShooterMotor = new Jaguar(RobotMap.ShooterMotor);
        ShooterEncoder = new Encoder(RobotMap.ShooterEncoderA, RobotMap.ShooterEncoderB);
        ShooterSwitch1 = new DigitalInput(RobotMap.ShooterSwitch1);
        ShooterEncoder.start();
    }
    public void initDefaultCommand() {
        setDefaultCommand(new ShooterMoveWithJoystick());
    }
    public void moveWithJoystick(){
        SmartDashboard.putNumber("Shooter Encoder count", ShooterEncoder.get());        
        SmartDashboard.putBoolean("ShooterSwitch", ShooterSwitch1.get());
        ShooterMotor.set(CommandBase.oi.getAuxStick().getRawAxis(2));
    }
    public void MoveForwardSlow(){ //firing on switches
        ShooterMotor.set(-.6);        
    }   
    public void MoveForwardFast(){ //getting to pre-halt position
        ShooterMotor.set(-1.0);        
    }   
    public void MoveBack(){ //moving to latch
        ShooterMotor.set(1.0);
    }
    public boolean WaitForSwitch(boolean autonomous){
        if (autonomous == true && runOnce == true){
            return true;
        }
        SmartDashboard.putNumber("Shooter Encoder count", ShooterEncoder.get());
        if (ShooterSwitch1.get() == true){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean WaitForEncoderPreHalt(boolean autonomous){ //stops 
        if (autonomous == true && runOnce == true){
            return true;
        }
        SmartDashboard.putNumber("Shooter Encoder count", ShooterEncoder.get());
        if (ShooterEncoder.get() <= haltingDistance){ // Shooter moves from higher to lower
            return true;
        }
        else{
            return false;
        }
    }
    public boolean WaitForEncoderLock(boolean autonomous){ // locks in latch
        if (autonomous == true && runOnce == true){
            return true;
        }
        SmartDashboard.putNumber("Shooter Encoder count", ShooterEncoder.get());
        if (ShooterEncoder.get() >= lockingDistance){ // Shooter moves from lower to higher
            return true;
        }
        else{
            return false;
        }
    }
    public void resetEncoder(){
        ShooterEncoder.reset(); //resets after firing
    }
    public void changeRunOnce(){
        runOnce = true;
    }
}
