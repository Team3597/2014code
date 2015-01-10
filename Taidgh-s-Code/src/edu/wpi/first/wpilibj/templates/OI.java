
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.templates.commands.ShooterRelayForward;
import edu.wpi.first.wpilibj.templates.commands.ShooterRelayReverse;
import edu.wpi.first.wpilibj.templates.commands.ShooterStopRelay;
/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    Joystick Stick;
    Joystick Stick2;
    JoystickButton RightTrigger;
    JoystickButton A; 
    JoystickButton B; 
    JoystickButton X;
    JoystickButton Y;
    JoystickButton RightBumper;
    JoystickButton LeftBumper;
    
    JoystickButton A2;
    JoystickButton B2;
    JoystickButton X2;
    JoystickButton Y2;
    JoystickButton RightBumper2;
    JoystickButton LeftBumper2;
    
    public OI(){
     
    Stick = new Joystick(1);
    Stick2 = new Joystick(2);
   
    RightTrigger = new JoystickButton(Stick, 6); // RB button 
    
//    A2 = new JoystickButton(Stick2, 1);
//    A2.whenPressed(new Load());
//    
//    B2 = new JoystickButton(Stick2, 2);
//    B2.whenPressed(new Fire());
    
    X = new JoystickButton(Stick,3);
    X.whenPressed(new ShooterStopRelay());
    
//    Y2 = new JoystickButton(Stick2,4);
//    Y2.whenPressed(new DropForLit());
         
    RightBumper = new JoystickButton(Stick, 6);
    RightBumper.whenPressed(new ShooterRelayForward());
    
    LeftBumper = new JoystickButton(Stick, 5);
    LeftBumper.whenPressed(new ShooterRelayReverse());
    
        
//A = 1
//B = 2
//X = 3 
//Y = 4
//LB = 5
//RB = 6
    }
   
    
    public Joystick getDriverJoystick(){
        return Stick;
        
    }
    public Joystick getDoYouEvenLift(){
        return Stick2;
    }
  
    // Another type of button you can create is a DigitalIOButton, which is
    // a button or switch hooked up to the cypress module. These are useful if
    // you want to build a customized operator interface.
    // Button button = new DigitalIOButton(1);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
}

