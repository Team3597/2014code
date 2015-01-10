/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author Matthew
 */
public class ChassisManualDriveConstantFt extends CommandBase {
    
    public ChassisManualDriveConstantFt() {
        requires(CommandBase.chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        CommandBase.chassis.reset();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        CommandBase.chassis.DriveWithEncoders();

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return CommandBase.chassis.isEncoderConstantFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
