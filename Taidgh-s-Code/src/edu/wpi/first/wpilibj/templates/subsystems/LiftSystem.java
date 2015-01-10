/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.RobotMap;
/**
 *
 * @author trobinson
 */
public class LiftSystem extends Subsystem {
    Jaguar liftMotor;
    Relay LiftRelay;
    Relay FireLift;
    DigitalInput FireliftForward;
    DigitalInput FireliftBack;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
     public LiftSystem() {
         liftMotor = new Jaguar(RobotMap.liftmotor);
         LiftRelay = new Relay(RobotMap.LiftRelay);
         FireLift = new Relay(RobotMap.FireLift);
         FireliftForward = new DigitalInput(RobotMap.FireliftForward);
         FireliftBack = new DigitalInput(RobotMap.FireLiftBack);
         
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
    }
    
    
     public void relayr(){
        LiftRelay.set(Relay.Value.kOn);
        LiftRelay.set(Relay.Value.kReverse);
    
    }
    
     public void relayf(){
        LiftRelay.set(Relay.Value.kOn);
        LiftRelay.set(Relay.Value.kForward);
    }
     
     
     
     public void LiftForShoot(){
     
         FireLift.set(Relay.Value.kOn);
         FireLift.set(Relay.Value.kForward);
         boolean topped = FireliftForward.get();
         
         if(topped == true){
         FireLift.set(Relay.Value.kOff);
         }
         
     }
     
     
     public void DropForLit(){
     
         FireLift.set(Relay.Value.kOn);
         FireLift.set(Relay.Value.kReverse);
         boolean BackItUp = FireliftBack.get();
         
         if(BackItUp == true){
         FireLift.set(Relay.Value.kOff);
         }
         
     }
    
    
   
}