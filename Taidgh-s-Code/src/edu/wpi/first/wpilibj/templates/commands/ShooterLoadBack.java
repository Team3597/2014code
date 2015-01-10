/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author trobinson
 */
public class ShooterLoadBack extends CommandBase {
    
    public ShooterLoadBack() {
         requires(CommandBase.shooter);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        CommandBase.shooter.MoveBack();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return CommandBase.shooter.WaitForEncoderLock();
    }

    // Called once after isFinished returns true
    protected void end() {
        CommandBase.shooter.SetSafetyLock();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}