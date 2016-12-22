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
import org.team401.robot.arm.commands.PushBoulder;
import org.team401.robot.arm.commands.SetWheelSpeed;
import org.team401.robot.chassis.QuezDrive;
import org.team401.robot.components.DartLinearActuator;
import org.team401.robot.math.MathUtilsKt;
import org.team401.robot.math.PIDGains;
import org.team401.robot.path.FalconPathPlanner;

public class Robot extends IterativeRobot {

    private QuezDrive chassis;
    private Arm arm;

    private FlightStick leftDriveController, rightDriveController, armController;

    @Override
    public void robotInit() {
        Strongback.configure()
                .recordDataToFile("/home/lvuser/")
                .recordEventsToFile("/home/lvuser/", 2097152);

        BetterSwitch oneButtonShoot = new BetterSwitch(
                () -> SmartDashboard.getBoolean("Auto Shooting Mode", false));
        BetterSwitch demoMode = new BetterSwitch(
                () -> SmartDashboard.getBoolean("Demo Mode", false));

        Solenoid shifter = Hardware.Solenoids.doubleSolenoid(4, 1, Solenoid.Direction.RETRACTING);
        chassis = new QuezDrive(shifter, demoMode);

        Solenoid shooter = Hardware.Solenoids.doubleSolenoid(5, 2, Solenoid.Direction.RETRACTING);
        arm = new Arm(new DartLinearActuator(),
                new CannonShooter(new PIDGains(1, 0, 0), new PIDGains(1, 0, 0), shooter, oneButtonShoot, demoMode));

        leftDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(0);
        rightDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(1);
        armController = Hardware.HumanInterfaceDevices.logitechAttack3D(2);

        SmartDashboard.putBoolean("Demo Mode", false);
        SmartDashboard.putBoolean("Auto Shooting Mode", true);

        //Switch gearToggle = rightDriveController.getButton(2);
        Switch toggleDemoMode = armController.getButton(9); // change these buttons
        Switch toggleShootMode = armController.getButton(10);
        Switch trigger = armController.getTrigger();
        Switch spinOut = armController.getButton(5);
        Switch spinIn = armController.getButton(3);

        SwitchReactor switchReactor = Strongback.switchReactor();

        /*switchReactor.onTriggered(gearToggle,
                () -> chassis.toggleGear());*/
        switchReactor.onTriggered(toggleDemoMode,
                () -> SmartDashboard.putBoolean("Demo Mode", !SmartDashboard.getBoolean("Demo Mode")));
        switchReactor.onTriggered(toggleShootMode,
                () -> SmartDashboard.putBoolean("Auto Shooting Mode", !SmartDashboard.getBoolean("Auto Shooting Mode")));
        switchReactor.onTriggered(armController.getButton(8),
                () -> SmartDashboard.putNumber("Pulses per Rev", 20));

        /*switchReactor.onTriggeredSubmit(Switch.and(oneButtonShoot, trigger),
                () -> new FireBoulder(arm, armController.getThrottle().read()));

        switchReactor.onTriggeredSubmit(Switch.and(oneButtonShoot.invert(), spinOut),
                () -> new SetWheelSpeed(arm.getShooter(), armController.getThrottle().read()));
        switchReactor.onUntriggeredSubmit(Switch.and(oneButtonShoot.invert(), spinOut),
                () -> new SetWheelSpeed(arm.getShooter(), 0));*/

        switchReactor.onTriggeredSubmit(Switch.and(oneButtonShoot.invert(), trigger),
                () -> new PushBoulder(arm.getShooter().getSolenoid()));
        switchReactor.whileTriggered(spinIn,
                () -> arm.getShooter().spinIn());
        switchReactor.onUntriggered(spinIn,
                () -> arm.getShooter().stop());

        switchReactor.onTriggeredSubmit(spinOut,
                () -> new SetWheelSpeed(arm.getShooter(), armController.getThrottle().read()));
        switchReactor.onUntriggered(spinOut,
                () -> {
                    arm.getShooter().stop();
                    Strongback.killAllCommands();
                });

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

        SmartDashboard.putNumber("Desired Speed", MathUtilsKt.toRange(armController.getThrottle().read()*-1, -1, 1, 1000.0, 5000.0));
    }

    @Override
    public void autonomousInit() {
        Strongback.start();
        FalconPathPlanner p = new FalconPathPlanner(new double[][] {});
        p.calculate(10, .02, 2);
    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void disabledInit() {
        Strongback.disable();
    }
}
