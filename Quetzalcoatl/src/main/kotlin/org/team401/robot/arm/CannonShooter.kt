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
import org.strongback.components.TalonSRX

class CannonShooter(val leftWheel: TalonSRX, val rightWheel: TalonSRX, val solenoid: Solenoid) : Requirable {

    companion object {
        const val INTAKE_SPEED = 1.0 // TODO fix intake speed
    }

    init {
        rightWheel.invert()
    }

    /**
     * Spins the wheels to take a ball in. If a ball is already in the
     * cannon then the wheels will not spin.
     */
    fun spinIn() {
        if (!isBallIn()) {
            leftWheel.speed = INTAKE_SPEED
            rightWheel.speed = INTAKE_SPEED
        }
    }

    /**
     * Spin the wheels at a certain speed to shoot the ball.
     */
    fun spinOut(speed: Double) {
        leftWheel.speed = speed
        rightWheel.speed = speed
    }

    fun getWheelSpeed(): Double {
        return 1.0
    }

    /**
     * Stops the shooter wheels from spinning.
     */
    fun stop() {
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