/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author Matthew
 */
public class ShooterLoadForward extends CommandBase {
    
    public ShooterLoadForward() {
        // Use requires() here to declare subsystem dependencies
        requires(CommandBase.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        CommandBase.shooter.MoveForward();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return CommandBase.shooter.WaitForEncoderPreHalt();
    }

    // Called once after isFinished returns true
    protected void end() {
        CommandBase.shooter.ReleaseSafetyLock();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
