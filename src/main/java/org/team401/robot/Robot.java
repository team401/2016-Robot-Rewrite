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

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.components.Motor;
import org.strongback.components.Solenoid;
import org.strongback.components.Switch;
import org.strongback.components.ui.FlightStick;
import org.strongback.drive.TankDrive;
import org.strongback.hardware.Hardware;

import org.team401.robot.arm.Arm;
import org.team401.robot.arm.CannonShooter;
import org.team401.robot.components.DartLinearActuator;

public class Robot extends IterativeRobot {

    private TankDrive drive;
    private Arm arm;

    private FlightStick leftDriveController, rightDriveController, armController;

    @Override
    public void robotInit() {
        Strongback.configure()
                .recordDataToFile("/home/lvuser/")
                .recordEventsToFile("/home/lvuser/", 2097152);
        drive = new TankDrive(
                Motor.compose(
                        Hardware.Motors.talon(0),
                        Hardware.Motors.talon(1)),
                Motor.compose(
                        Hardware.Motors.talon(2),
                        Hardware.Motors.talon(3)));

        Solenoid shooter = Hardware.Solenoids.doubleSolenoid(5, 2, Solenoid.Direction.RETRACTING);

        arm = new Arm(new DartLinearActuator(),
                new CannonShooter(shooter, false, false));

        leftDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(0);
        rightDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(1);
        armController = Hardware.HumanInterfaceDevices.logitechAttack3D(2);
    }

    @Override
    public void teleopInit() {
        Strongback.restart();
    }

    @Override
    public void teleopPeriodic() {
        drive.tank(leftDriveController.getPitch().read(), rightDriveController.getPitch().read());
        if (armController.getThumb().isTriggered())
            arm.getDart().drive(armController.getPitch().read());
        else
            arm.getDart().stop();
        if(armController.getTrigger().isTriggered())
            arm.getShooter().getSolenoid().extend();
        else
            arm.getShooter().getSolenoid().retract();
        if(armController.getButton(3).isTriggered())
            arm.getShooter().spinOut(armController.getThrottle().read());
        else if(armController.getButton(4).isTriggered())
            arm.getShooter().spinIn();
        else
            arm.getShooter().stop();
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

    public void disabledPeriodic(){
        drive.tank(0,0);
    }
}