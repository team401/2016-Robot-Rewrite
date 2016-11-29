package org.team401.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import org.strongback.components.Motor;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;

public class Robot extends IterativeRobot {

    private TankDrive drive;

    @Override
    public void robotInit() {
        Motor leftGearbox = Motor.compose(
                Hardware.Motors.talonSRX(0),
                Hardware.Motors.talonSRX(1),
                Hardware.Motors.talonSRX(2));
        Motor rightGearbox = Motor.compose(
                Hardware.Motors.talonSRX(3),
                Hardware.Motors.talonSRX(4),
                Hardware.Motors.talonSRX(5)).invert();
        drive = new TankDrive(leftGearbox, rightGearbox);
    }

    @Override
    public void disabledInit() {

    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void disabledPeriodic() {

    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopPeriodic() {

    }
}
