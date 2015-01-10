
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.RobotMap;
import edu.wpi.first.wpilibj.templates.commands.FindRange;

/**
 *
 */
public class RangeFinder extends Subsystem {

    AnalogChannel rangeFinder;
    double range;
    
    public RangeFinder() {
            rangeFinder = new AnalogChannel(RobotMap.rangeFinder);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new FindRange());
    }
    
    public void GetRange() {
        range = rangeFinder.getVoltage()*1000/9.8;
        SmartDashboard.putNumber("Range in inches:", range);    
    }
}

