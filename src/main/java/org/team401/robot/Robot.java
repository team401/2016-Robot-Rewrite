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
import org.team401.robot.chassis.QuezDrive;
import org.team401.robot.components.DartLinearActuator;
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

        Solenoid shifter = Hardware.Solenoids.doubleSolenoid(4, 1, Solenoid.Direction.RETRACTING);
        chassis = new QuezDrive(shifter, false);

        Solenoid shooter = Hardware.Solenoids.doubleSolenoid(5, 2, Solenoid.Direction.RETRACTING);
        arm = new Arm(new DartLinearActuator(),
                new CannonShooter(shooter, false, false));

        leftDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(0);
        rightDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(1);
        armController = Hardware.HumanInterfaceDevices.logitechAttack3D(2);

        SmartDashboard.putBoolean("Demo Mode", false);
        SmartDashboard.putBoolean("Auto Shooting Mode", true);

        Switch gearToggle = rightDriveController.getButton(2);
        //Switch demoMode = armController.getButton(9); // change these buttons
        //Switch toggleShootMode = armController.getButton(10);
        Switch trigger = armController.getTrigger();
        Switch spinOut = armController.getButton(5);
        //Switch stopWheels = () -> armController.getDPad(0).getDirection() == -1;
        Switch spinIn = armController.getButton(3); // ??????????

        BetterSwitch oneButtonShoot = new BetterSwitch(
                () -> SmartDashboard.getBoolean("Auto Shooting Mode", false));

        SwitchReactor switchReactor = Strongback.switchReactor();

        /*switchReactor.onTriggered(gearToggle,
                () -> chassis.toggleGear());
        /*switchReactor.onTriggered(demoMode,
                () -> Strongback.submit(new ToggleDemoMode(chassis, arm)));
        switchReactor.onTriggered(toggleShootMode,
                () -> SmartDashboard.putBoolean("Auto Shooting Mode", !SmartDashboard.getBoolean("Auto Shooting Mode")));*/

        /*switchReactor.onTriggeredSubmit(Switch.and(oneButtonShoot, trigger),
                () -> new FireBoulder(arm, armController.getThrottle().read()));*/

        /*switchReactor.onTriggered(Switch.and(oneButtonShoot.invert(), spinOut),
                () -> arm.getShooter().spinOut(armController.getThrottle().read()));
        switchReactor.onTriggered(Switch.and(oneButtonShoot.invert(), spinIn),
                () -> arm.getShooter().spinIn());
        switchReactor.onTriggered(Switch.and(oneButtonShoot.invert(), stopWheels),
                () -> arm.getShooter().stop());*/

        switchReactor.onTriggeredSubmit(trigger,
                () -> new PushBoulder(arm.getShooter().getSolenoid()));
        switchReactor.onTriggered(spinIn,
                () -> arm.getShooter().spinIn());
        switchReactor.whileTriggered(spinOut,
                () -> arm.getShooter().spinOut(armController.getThrottle().read()*-1));
        switchReactor.onUntriggered(Switch.or(spinIn, spinOut),
                () -> arm.getShooter().stop());


        Strongback.dataRecorder()
                .register("Gear", chassis.highGear())
                //.register("Arm Unlock", armController.getThumb())
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
        if (armController.getButton(2).isTriggered())
            arm.getDart().drive(armController.getPitch().read());
        else
            arm.getDart().stop();
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
