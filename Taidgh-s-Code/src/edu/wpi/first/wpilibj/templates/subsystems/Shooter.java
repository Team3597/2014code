/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;

/**
 *
 * @author trobinson
 */
public class Shooter extends Subsystem {
    Relay move;
    Encoder Encodarific;
    DigitalInput Switch1;
    DigitalInput Switch2;
    Servo SafetyLockServo;
    int lockingDistance = 11;
    int haltingDistance = 1;

    public Shooter(){
        move = new Relay(RobotMap.FireRelay);
        Encodarific = new Encoder(RobotMap.Encoder1a, RobotMap.Encoder1b);
        Switch1 = new DigitalInput(RobotMap.ShooterSwitch1);
        Switch1 = new DigitalInput(RobotMap.ShooterSwitch2);
        SafetyLockServo = new Servo(RobotMap.SafetyLockServo);
        Encodarific.start();
    }
    public void initDefaultCommand() {
    }
    
    public boolean WaitForSwitch(){
        SmartDashboard.putNumber("count", Encodarific.getDistance());
        if (Switch1.get() == true || Switch2.get() == true){
            Encodarific.reset();
            return true;
        }
        else{
            return false;
        }
    }
    public boolean WaitForEncoderPreHalt(){
        SmartDashboard.putNumber("count", Encodarific.getDistance());
        if (Encodarific.get() <= haltingDistance){ // moves from higher to lower
            return true;
        }
        else{
            return false;
        }
    }
    public boolean WaitForEncoderLock(){
        SmartDashboard.putNumber("count", Encodarific.getDistance());
        if (Encodarific.get() >= lockingDistance){ // moves from lower to higher
            return true;
        }
        else{
            return false;
        }
    }
    public void StopRelay(){
        move.set(Relay.Value.kOff);
    }
    public void MoveForward(){ 
        SmartDashboard.putNumber("count", Encodarific.getDistance());
        move.set(Relay.Value.kOn);
        move.set(Relay.Value.kForward);        
    }   
    public void MoveBack(){
        SmartDashboard.putNumber("count", Encodarific.getDistance());
        move.set(Relay.Value.kOn);
        move.set(Relay.Value.kReverse);
    }
    public void resetEncoder(){
        Encodarific.reset();
    }
    public void SetSafetyLock(){
        SafetyLockServo.set(1.0); // full right on range from 0.0-1.0
    }
    public void ReleaseSafetyLock(){
        SafetyLockServo.set(0.0); // full left on range from 0.0-1.0
    }
}
