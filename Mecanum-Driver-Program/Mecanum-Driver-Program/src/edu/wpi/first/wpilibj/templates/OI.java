
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.templates.commands.ISpy;


public class OI {
    Joystick DriverStick;
    
    JoystickButton A;
    JoystickButton B;
    JoystickButton X;
    JoystickButton LB;
    JoystickButton RB;
    JoystickButton Start;

    public OI() {
        DriverStick = new Joystick(1);
        A = new JoystickButton(DriverStick,1);
        B = new JoystickButton(DriverStick,2);
        X = new JoystickButton(DriverStick,3);
        LB = new JoystickButton(DriverStick, 5);
        RB = new JoystickButton(DriverStick, 6);
        Start = new JoystickButton(DriverStick, 8);
//        B.whenPressed(new ISpy());
    }
    public Joystick getDriverStick(){
        return DriverStick;
    }
}

