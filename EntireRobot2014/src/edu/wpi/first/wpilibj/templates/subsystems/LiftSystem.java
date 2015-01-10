/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.commands.LiftWithJoystick;
/**
 *
 * @author trobinson
 */
public class LiftSystem extends Subsystem {
    Jaguar liftMotorCIM;
    DigitalInput GrabberTopSwitch;
    DigitalInput GrabberBottomSwitch;
    Encoder GrabberEncoder;
    int AutonomousEncoderPosition = -1700;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    
     public LiftSystem() {
        liftMotorCIM = new Jaguar(RobotMap.liftMotorCIM);
        GrabberTopSwitch = new DigitalInput(RobotMap.GrabberTopSwitch);
        GrabberBottomSwitch = new DigitalInput(RobotMap.GrabberBottomSwitch);
        GrabberEncoder = new Encoder(RobotMap.GrabberEncoderA,RobotMap.GrabberEncoderB);
        GrabberEncoder.start();
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new LiftWithJoystick());
    }
    public void liftWithJoystick(){
        Joystick AuxStick = CommandBase.oi.getAuxStick();
        SmartDashboard.putBoolean("GrabberTopSwitch", GrabberTopSwitch.get());
        SmartDashboard.putBoolean("GrabberBottomSwitch", GrabberBottomSwitch.get());
        SmartDashboard.putNumber("GrabberEncoder", GrabberEncoder.get());
        if ((AuxStick.getRawAxis(5) <= 0 && GrabberBottomSwitch.get() == true) || (AuxStick.getRawAxis(5) >= 0 && GrabberTopSwitch.get() == true)){ 
            liftMotorCIM.set(AuxStick.getRawAxis(5)*3.0/4.0); //CIM, worm gear is powerful and fast, watch out.
        }
        else{
            liftMotorCIM.set(0.0); //CIM 
        }
    }
    public void AutonomousMoveLiftSystem(){
        if (GrabberBottomSwitch.get() == true){ //moves toward pick up position and checks for hitting limit switch
            liftMotorCIM.set(-.3);
        }
    }
    public boolean AutonomousAimWithEncoder(){
        SmartDashboard.putNumber("GrabberEncoder", GrabberEncoder.get());
        if (GrabberEncoder.get() <= (AutonomousEncoderPosition)){ // <= a negative number
            return true;
        }
        else{
            return false;
        }
    }
}