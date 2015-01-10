/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author jacobchristiansen
 */
public class DriveWithEncoders extends CommandBase {
    
    public DriveWithEncoders() {
        requires(CommandBase.driver);
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
        return CommandBase.driver.isEncoderFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
        CommandBase.driver.changeRunOnce();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
