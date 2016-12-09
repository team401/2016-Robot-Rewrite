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
import org.strongback.components.Solenoid;
import org.strongback.components.Switch;
import org.strongback.components.ui.FlightStick;
import org.strongback.hardware.Hardware;

import org.team401.robot.arm.Arm;
import org.team401.robot.arm.CannonShooter;
import org.team401.robot.arm.commands.FireBoulder;
import org.team401.robot.arm.commands.PushBoulder;
import org.team401.robot.arm.commands.SetWheelSpeed;
import org.team401.robot.chassis.QuezDrive;
import org.team401.robot.commands.ToggleDemoMode;
import org.team401.robot.components.DartLinearActuator;
import org.team401.robot.math.PIDGains;

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
        chassis = new QuezDrive(new PIDGains(1, 0, 0), shifter, false);

        Solenoid shooter = Hardware.Solenoids.doubleSolenoid(1, 5, Solenoid.Direction.RETRACTING);
        arm = new Arm(new DartLinearActuator(),
                new CannonShooter(new PIDGains(1, 0, 0), shooter, false, false));

        rightDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(1);
        armController = Hardware.HumanInterfaceDevices.logitechAttack3D(2);

        SmartDashboard.putBoolean("Demo Mode", false);
        SmartDashboard.putBoolean("Auto Shooting Mode", true);

        Switch gearToggle = rightDriveController.getButton(2);
        Switch demoMode = armController.getButton(9); // change these buttons
        Switch toggleShootMode = armController.getButton(10);
        Switch trigger = armController.getTrigger();
        Switch spinOut = () -> armController.getDPad(0).getDirection() == 0;
        Switch spinIn = () -> armController.getDPad(0).getDirection() == 2; // ??????????

        BetterSwitch oneButtonShoot = new BetterSwitch(() -> SmartDashboard.getBoolean("Auto Shooting Mode", false));

        SwitchReactor switchReactor = Strongback.switchReactor();
        switchReactor.onTriggered(gearToggle, () -> chassis.toggleGear());
        switchReactor.onTriggered(demoMode, () -> Strongback.submit(new ToggleDemoMode(chassis, arm)));
        switchReactor.onTriggered(toggleShootMode, () -> SmartDashboard.putBoolean("Auto Shooting Mode", SmartDashboard.getBoolean("Auto Shooting Mode")));

        switchReactor.onTriggeredSubmit(Switch.and(oneButtonShoot, trigger),
                () -> new FireBoulder(arm, armController.getThrottle().read()));

        switchReactor.onTriggeredSubmit(Switch.and(oneButtonShoot.invert(), spinOut),
                () -> new SetWheelSpeed(arm.getShooter(), armController.getThrottle().read()));
        switchReactor.onUntriggeredSubmit(Switch.and(oneButtonShoot.invert(), spinOut),
                () -> new SetWheelSpeed(arm.getShooter(), 0));

        switchReactor.onTriggeredSubmit(Switch.and(oneButtonShoot.invert(), trigger),
                () -> new PushBoulder(arm.getShooter().getSolenoid()));
        switchReactor.onTriggered(spinIn,
                () -> arm.getShooter().spinIn());
        switchReactor.onUntriggered(spinIn,
                () -> arm.getShooter().stop());

        Strongback.dataRecorder()
                .register("Gear", chassis.highGear())
                .register("Arm Unlock", armController.getThumb())
                .register("Top", arm.getDart().getTopHallEffect())
                .register("Bottom", arm.getDart().getBottomHallEffect());


    }

    @Override
    public void teleopInit() {
        Strongback.restart();
    }

    @Override
    public void teleopPeriodic() {
        chassis.drive(leftDriveController.getPitch().read(), rightDriveController.getPitch().read());
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
