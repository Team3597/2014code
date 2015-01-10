
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.templates.commands.ShooterFireAndReload;
import edu.wpi.first.wpilibj.templates.commands.ShooterResetEncoder;
/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */

// Autonomous Mode turn on and tune
// Tune RGB camera values, chassis movement and encoder
// Check Camera connection
// fine tune rotate and straight pulses/ft ratio
// find number of feet to travel to reach across the line
// write aiming program, either camera sensor or dead reckoning. Needs encoder on lift system

public class OI {
    Joystick DriverStick;
    Joystick AuxStick;
    
//    JoystickButton A; 
//    JoystickButton B; 
//    JoystickButton X;
//    JoystickButton Y;
//    JoystickButton RightBumper;
//    JoystickButton LeftBumper;
//    JoystickButton Start;
    
    JoystickButton A2;
//    JoystickButton B2;
//    JoystickButton X2;
    JoystickButton Y2;
    JoystickButton RightBumper2;
    JoystickButton LeftBumper2;
    JoystickButton Select2;
    JoystickButton Start2;
    
    public OI(){
        
        //A = 1
        //B = 2
        //X = 3 
        //Y = 4
        //LB = 5
        //RB = 6
        //Select = 7
        //Start = 8

        DriverStick = new Joystick(1);
        AuxStick = new Joystick(2);

        RightBumper2 = new JoystickButton(AuxStick,6);
        RightBumper2.whenPressed(new ShooterFireAndReload(false));

        LeftBumper2 = new JoystickButton(AuxStick, 5);
        LeftBumper2.whenPressed(new ShooterResetEncoder());
    }
    
    public Joystick getDriverJoystick(){
        return DriverStick;
    }
    
    public Joystick getAuxStick(){
        return AuxStick;
    }
}

