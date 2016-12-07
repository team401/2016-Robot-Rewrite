/*
    Quetzalcoatl - Copperhead Robotics 2016 Robot code for FIRST Stronghold
    Copyright (C) 2016 FRC Team 401

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/
package org.team401.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.components.Solenoid;
import org.strongback.components.TalonSRX;
import org.strongback.components.ui.FlightStick;
import org.strongback.hardware.Hardware;

import org.team401.robot.arm.Arm;
import org.team401.robot.arm.CannonShooter;
import org.team401.robot.chassis.QuezDrive;
import org.team401.robot.components.DartLinearActuator;

import java.util.ArrayList;
import java.util.List;

public class Robot extends IterativeRobot {

    private QuezDrive chassis;
    private Arm arm;

    private FlightStick leftDriveController, rightDriveController, armController;

    private int tick;


    @Override
    public void robotInit() {
        Strongback.configure()
                .recordDataToFile("/home/lvuser/")
                .recordEventsToFile("/home/lvuser/", 2097152);
        // init motors
        /*List<TalonSRX> leftMotors = new ArrayList<>();
        leftMotors.add(Hardware.Motors.talonSRX(1));
        leftMotors.add(Hardware.Motors.talonSRX(2));
        leftMotors.add(Hardware.Motors.talonSRX(0));
        List<TalonSRX> rightMotors = new ArrayList<>();
        rightMotors.add(Hardware.Motors.talonSRX(5));
        rightMotors.add(Hardware.Motors.talonSRX(6));
        rightMotors.add(Hardware.Motors.talonSRX(7));*/

        Solenoid shifter = Hardware.Solenoids.doubleSolenoid(0, 4, Solenoid.Direction.RETRACTING);
        chassis = new QuezDrive(shifter);

        TalonSRX dart = Hardware.Motors.talonSRX(4);
        TalonSRX leftShooterWheel = Hardware.Motors.talonSRX(3);
        TalonSRX rightShooterWheel = Hardware.Motors.talonSRX(8);
        Solenoid shooter = Hardware.Solenoids.doubleSolenoid(1, 2, Solenoid.Direction.RETRACTING);

        arm = new Arm(new DartLinearActuator(dart, () -> false, () -> false), new CannonShooter(leftShooterWheel, rightShooterWheel, shooter));

        leftDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(0);
        rightDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(1);
        armController = Hardware.HumanInterfaceDevices.logitechAttack3D(2);

        SwitchReactor switchReactor = Strongback.switchReactor();
        switchReactor.onTriggered(rightDriveController.getButton(2), () -> chassis.toggleGear());

        Strongback.dataRecorder()
                .register("Gear", chassis.highGear())
                .register("Arm Unlock", armController.getThumb());
    }

    @Override
    public void teleopInit() {
        Strongback.restart();
    }

    @Override
    public void teleopPeriodic() {
        chassis.drive(leftDriveController.getPitch().read(), rightDriveController.getPitch().read());
        if (armController.getThumb().isTriggered())
            arm.getDart().drive(armController.getPitch().read());
    }

    @Override
    public void autonomousInit() {
        Strongback.start();
    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void disabledInit() {
        Strongback.disable();
    }
}
