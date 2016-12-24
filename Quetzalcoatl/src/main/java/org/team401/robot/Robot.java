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
import org.strongback.components.Switch;
import org.strongback.components.ui.FlightStick;
import org.strongback.hardware.Hardware;

import org.team401.robot.arm.Arm;
import org.team401.robot.arm.CannonShooter;
import org.team401.robot.arm.commands.FireBoulder;
import org.team401.robot.arm.commands.PushBoulder;
import org.team401.robot.arm.commands.SetWheelSpeed;
import org.team401.robot.chassis.QuezDrive;
import org.team401.robot.components.DartLinearActuator;
import org.team401.robot.math.MathUtilsKt;
import org.team401.robot.math.PIDGains;
import org.team401.robot.path.FalconPathPlanner;
import org.team401.robot.util.BetterSwitch;

public class Robot extends IterativeRobot {

    private QuezDrive chassis;
    private Arm arm;

    private FlightStick leftDriveController, rightDriveController, armController;

    private double leftP, leftI, leftD, leftF;
    private double rightP, rightI, rightD, rightF;

    @Override
    public void robotInit() {
        Strongback.configure()
                .recordDataToFile("/home/lvuser/")
                .recordEventsToFile("/home/lvuser/", 2097152);

        BetterSwitch oneButtonShoot = new BetterSwitch(
                () -> SmartDashboard.getBoolean("Auto Shooting Mode", false));
        BetterSwitch demoMode = new BetterSwitch(
                () -> SmartDashboard.getBoolean("Demo Mode", false));
        BetterSwitch ballIn = new BetterSwitch(Hardware.Switches.normallyClosed(ConstantsKt.BANNER_SENSOR));

        chassis = new QuezDrive(demoMode);

        arm = new Arm(new DartLinearActuator(),
                new CannonShooter(new PIDGains(1, 0, 0), new PIDGains(1, 0, 0), ballIn, demoMode));

        leftDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(ConstantsKt.JOYSTICK_LEFT_DRIVE);
        rightDriveController = Hardware.HumanInterfaceDevices.logitechAttack3D(ConstantsKt.JOYSTICK_RIGHT_DRIVE);
        armController = Hardware.HumanInterfaceDevices.logitechAttack3D(ConstantsKt.JOYSTICK_MASHER);

        leftP = SmartDashboard.getNumber("P Left");
        leftI = SmartDashboard.getNumber("I Left");
        leftD = SmartDashboard.getNumber("D Left");
        leftF = SmartDashboard.getNumber("F Left");
        rightP = SmartDashboard.getNumber("P Right");
        rightI = SmartDashboard.getNumber("I Right");
        rightD = SmartDashboard.getNumber("D Right");
        rightF = SmartDashboard.getNumber("F Right");

        Switch gearToggle = rightDriveController.getButton(ConstantsKt.SWITCH_TOGGLE_GEAR);
        Switch toggleDemoMode = armController.getButton(ConstantsKt.SWITCH_TOGGLE_DEMO);
        Switch toggleShootMode = armController.getButton(ConstantsKt.SWITCH_TOGGLE_AUTO_SHOOT);
        Switch trigger = armController.getTrigger();
        Switch spinOut = armController.getButton(ConstantsKt.SWITCH_SPIN_OUT);
        Switch spinIn = armController.getButton(ConstantsKt.SWITCH_SPIN_IN);

        SwitchReactor switchReactor = Strongback.switchReactor();

        switchReactor.onTriggered(gearToggle, // shifting is a no no
                () -> chassis.toggleGear());
        switchReactor.onTriggered(toggleDemoMode,
                () -> SmartDashboard.putBoolean("Demo Mode", !SmartDashboard.getBoolean("Demo Mode")));
        switchReactor.onTriggered(toggleShootMode,
                () -> SmartDashboard.putBoolean("Auto Shooting Mode", !SmartDashboard.getBoolean("Auto Shooting Mode")));

        switchReactor.onTriggered(ballIn,
                () -> SmartDashboard.putBoolean("Ball In Shooter", true));
        switchReactor.onUntriggered(ballIn,
                () -> SmartDashboard.putBoolean("Ball In Shooter", false));

        // auto shoot
        switchReactor.onTriggeredSubmit(Switch.and(oneButtonShoot, trigger),
                () -> new FireBoulder(arm, armController.getThrottle().read()));

        // manual shoot
        switchReactor.onTriggeredSubmit(Switch.and(oneButtonShoot.invert(), spinOut),
                () -> new SetWheelSpeed(arm.getShooter(), armController.getThrottle().read(), 200));
        switchReactor.onTriggeredSubmit(Switch.and(oneButtonShoot.invert(), trigger),
                () -> new PushBoulder(arm.getShooter().getSolenoid()));
        switchReactor.onUntriggered(Switch.or(oneButtonShoot, spinOut),
                () -> arm.getShooter().stop());

        switchReactor.whileTriggered(spinIn,
                () -> arm.getShooter().spinIn());
        switchReactor.onUntriggered(spinIn,
                () -> arm.getShooter().stop());

        switchReactor.onTriggeredSubmit(spinOut,
                () -> new SetWheelSpeed(arm.getShooter(), armController.getThrottle().read(), 200));
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

        SmartDashboard.putNumber("Desired Speed", MathUtilsKt.toRange(armController.getThrottle().read()*-1, -1, 1, 1500.0, 4800.0));
        SmartDashboard.putNumber("Actual Speed (Left)", arm.getShooter().getLeftWheel().getSpeed());
        SmartDashboard.putNumber("Actual Speed (Right)", arm.getShooter().getRightWheel().getSpeed());

        if (SmartDashboard.getNumber("P Left") != leftP || SmartDashboard.getNumber("P Right") != rightP ||
                SmartDashboard.getNumber("I Left") != leftI || SmartDashboard.getNumber("I Right") != rightI ||
                SmartDashboard.getNumber("D Left") != leftD || SmartDashboard.getNumber("D Right") != rightD ||
                SmartDashboard.getNumber("F Left") != leftF || SmartDashboard.getNumber("F Right") != rightF) {
            leftP = SmartDashboard.getNumber("P Left");
            leftI = SmartDashboard.getNumber("I Left");
            leftD = SmartDashboard.getNumber("D Left");
            leftF = SmartDashboard.getNumber("F Left");
            rightP = SmartDashboard.getNumber("P Right");
            rightI = SmartDashboard.getNumber("I Right");
            rightD = SmartDashboard.getNumber("D Right");
            rightF = SmartDashboard.getNumber("F Left");
            arm.getShooter().updateGains(new PIDGains(leftP, leftI/1000000, leftD), leftF,
                    new PIDGains(rightP, rightI/1000000, rightD), rightF);
        }
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

    @Override
    public void disabledPeriodic() {

    }
}
