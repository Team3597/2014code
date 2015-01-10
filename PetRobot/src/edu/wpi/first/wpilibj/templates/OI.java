
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.templates.commands.AutoForward;
import edu.wpi.first.wpilibj.templates.commands.FollowTarget;
import edu.wpi.first.wpilibj.templates.commands.ManualDrive3Ft;
import edu.wpi.first.wpilibj.templates.commands.ManualRotate20Deg;


public class OI {
    Joystick DriverStick;
    
    JoystickButton A;
    JoystickButton B;
    JoystickButton X;
    JoystickButton Y;
    JoystickButton LB;
    JoystickButton RB;
    JoystickButton Start;

    public OI() {
        DriverStick = new Joystick(1);
        A = new JoystickButton(DriverStick,1);
        B = new JoystickButton(DriverStick,2);
        X = new JoystickButton(DriverStick,3);
        Y = new JoystickButton(DriverStick,4);
        LB = new JoystickButton(DriverStick, 5);
        RB = new JoystickButton(DriverStick, 6);
        Start = new JoystickButton(DriverStick, 8);
//        Start.whileHeld(new FollowTarget());
//        RB.whenPressed(new ManualRotate20Deg(false)); // false is right
//        LB.whenPressed(new ManualRotate20Deg(true)); // true is left
//        Y.whenPressed(new ManualDrive3Ft());
//        A.whenPressed(new AutoForward());
    }
    public Joystick getDriverStick(){
        return DriverStick;
    }
}

