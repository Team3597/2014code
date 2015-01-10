/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author Matthew
 */
public class ShooterFireAndReload extends CommandGroup {
    
    public ShooterFireAndReload() {
        addSequential(new ShooterFire());
        addSequential(new ShooterLoadBack());
        addSequential(new ShooterLoadForward());
    }
}
