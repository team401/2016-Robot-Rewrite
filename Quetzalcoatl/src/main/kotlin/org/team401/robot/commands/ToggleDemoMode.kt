package org.team401.robot.commands

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import org.strongback.command.Command
import org.team401.robot.arm.Arm
import org.team401.robot.chassis.QuezDrive

class ToggleDemoMode(val drive: QuezDrive, val arm: Arm) : Command() {

    override fun initialize() {
        SmartDashboard.putBoolean("Demo Mode", !SmartDashboard.getBoolean("Demo Mode"))
        val enabled = SmartDashboard.getBoolean("Demo Mode")
        drive.demoMode = enabled
        arm.shooter.demoMode = enabled
    }

    override fun execute(): Boolean {
        return true
    }
}