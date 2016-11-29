package org.team401.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

import org.strongback.components.Motor;
import org.strongback.components.ui.FlightStick;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;

public class Robot extends IterativeRobot {

    private TankDrive drive;

    private FlightStick leftDriveController, rightDriveController, armController;

    @Override
    public void robotInit() {
        // TODO fix motor ports
        Motor leftGearbox = Motor.compose(
                Hardware.Motors.talonSRX(0),
                Hardware.Motors.talonSRX(1),
                Hardware.Motors.talonSRX(2));
        Motor rightGearbox = Motor.compose(
                Hardware.Motors.talonSRX(3),
                Hardware.Motors.talonSRX(4),
                Hardware.Motors.talonSRX(5)).invert();
        // TODO add hi/lo gear solenoids
        drive = new TankDrive(leftGearbox, rightGearbox);

        // TODO fix joystick ports
        leftDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(0);
        rightDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(1);
        armController = Hardware.HumanInterfaceDevices.logitechAttack3D(2);
    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void autonomousInit() {

    }

    @Override
    public void teleopPeriodic() {
        // read values from joystick and drive (maybe)
        drive.tank(leftDriveController.getPitch().read(), rightDriveController.getPitch().read());
    }

    @Override
    public void autonomousPeriodic() {

    }
}
