/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author Matthew
 */
public class ManualDriveConstantFt extends CommandBase {
    
    public ManualDriveConstantFt() {
        // Use requires() here to declare subsystem dependencies
        requires(CommandBase.camera);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        CommandBase.driver.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        CommandBase.driver.DriveWithEncoders();

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return CommandBase.driver.isEncoderConstantFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
