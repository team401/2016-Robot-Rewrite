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

import edu.wpi.first.wpilibj.PWM;
import org.strongback.Strongback;
import org.strongback.SwitchReactor;
import org.strongback.components.Solenoid;
import org.strongback.components.TalonSRX;
import org.strongback.components.ui.FlightStick;
import org.strongback.hardware.Hardware;

import org.team401.robot.arm.Arm;
import org.team401.robot.arm.CannonShooter;
import org.team401.robot.arm.commands.FireBoulder;
import org.team401.robot.chassis.QuezDrive;
import org.team401.robot.components.DartLinearActuator;
import org.team401.robot.math.PIDGains;

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

        Solenoid shifter = Hardware.Solenoids.doubleSolenoid(0, 4, Solenoid.Direction.RETRACTING);
        chassis = new QuezDrive(shifter);

        Solenoid shooter = Hardware.Solenoids.doubleSolenoid(1, 2, Solenoid.Direction.RETRACTING);

        arm = new Arm(new DartLinearActuator(), new CannonShooter(new PIDGains(1, 0, 0), shooter));

        leftDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(0);
        rightDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(1);
        armController = Hardware.HumanInterfaceDevices.logitechAttack3D(2);

        SwitchReactor switchReactor = Strongback.switchReactor();
        switchReactor.onTriggered(rightDriveController.getButton(2), () -> chassis.toggleGear());

        Strongback.dataRecorder()
                .register("Gear", chassis.highGear())
                .register("Arm Unlock", armController.getThumb())
                .register("Top", arm.getDart().getTopHoloflex())
                .register("Bottom", arm.getDart().getBottomHoloflex());
    }

    @Override
    public void teleopInit() {
        Strongback.restart();
    }

    @Override
    public void teleopPeriodic() {
        chassis.drive(leftDriveController.getPitch().read(), rightDriveController.getPitch().read());
        arm.getDart().drive(armController.getPitch().read());
        if (armController.getDPad(0).getDirection() == 0) {
            arm.getShooter().spinOut(armController.getThrottle().read());
        } if (armController.getThumb().isTriggered()) {
            arm.getShooter().spinIn();
        } else {
            arm.getShooter().stop();
        }
        if (armController.getTrigger().isTriggered())
            arm.getShooter().getSolenoid().extend();
        else
            arm.getShooter().getSolenoid().retract();
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
