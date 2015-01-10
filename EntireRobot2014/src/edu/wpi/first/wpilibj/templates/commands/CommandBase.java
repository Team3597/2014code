package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.OI;
import edu.wpi.first.wpilibj.templates.subsystems.Camera;
import edu.wpi.first.wpilibj.templates.subsystems.Chassis;
import edu.wpi.first.wpilibj.templates.subsystems.LiftSystem;
import edu.wpi.first.wpilibj.templates.subsystems.Shooter;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    public static Chassis chassis = new Chassis(); //The way you spell it here is how you will be calling it in the commands.  
    public static LiftSystem liftSystem = new LiftSystem();
    public static Shooter shooter = new Shooter(); 
//    public static Camera camera = new Camera(); 
    
    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI(); // No home so it needs to be called here. 
        // Show what command your subsystem is running on the SmartDashboard
        SmartDashboard.putData(chassis);
        SmartDashboard.putData(liftSystem);
        SmartDashboard.putData(shooter);
    }

    public CommandBase(String name) { 
        super(name);
        //constuctors that the programs uses. Matt says don't mess with them so DONT MESS WITH THEM!

    }

    public CommandBase() {
        super();
    }
}
