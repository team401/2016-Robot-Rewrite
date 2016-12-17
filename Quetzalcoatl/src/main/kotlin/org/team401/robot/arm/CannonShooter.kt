/*
    Quetzalcoatl - Copperhead Robotics 2016 Robot code for FIRST Stronghold
    Copyright (C) 2016 Zach Kozar

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
package org.team401.robot.arm

import org.strongback.command.Requirable
import org.strongback.components.Solenoid
import org.strongback.components.Stoppable
import org.strongback.components.TalonSRX
import org.strongback.control.TalonController
import org.strongback.hardware.Hardware
import org.team401.robot.math.PIDGains
import org.team401.robot.math.toRange


/**
 * @param gains PID values for the wheels
 * @param solenoid the solenoid that controls the robot's shooter
 * @param auto whether to use commands to auto shoot or shoot manually
 */
class CannonShooter(val solenoid: Solenoid, val auto: Boolean, var demoMode: Boolean) : Requirable, Stoppable {

    val leftWheel: TalonSRX
    val rightWheel: TalonSRX

    companion object {
        const val INTAKE_SPEED = 0.45 // TODO fix intake speed
    }

    init {
        leftWheel = Hardware.Motors.talonSRX(3)
        rightWheel = Hardware.Motors.talonSRX(8)
    }

    /**
     * Spins the wheels to take a ball in. If a ball is already in the
     * cannon then the wheels will not spin.
     */
    fun spinIn() {
        if (!isBallIn()) {
            leftWheel.speed = INTAKE_SPEED
            rightWheel.speed = -INTAKE_SPEED
        } else
            stop()
    }

    /**
     * Spin the wheels at a certain speed to shoot the ball.
     */
    fun spinOut(throttle: Double) {
        val range = (throttle + 1) / 2
        val speed = if (demoMode) range / 2 else range
        leftWheel.speed = -speed
        rightWheel.speed = speed
    }

    fun getWheelSpeed(): Double {
        return (leftWheel.speed + rightWheel.speed) / 2
    }

    /**
     * Stops the shooter wheels from spinning.
     */
    override fun stop() {
        leftWheel.stop()
        rightWheel.stop()
    }

    /**
     * Returns whether a ball is in the shooter or not.
     */
    fun isBallIn(): Boolean {
        return false // TODO implement
    }
}