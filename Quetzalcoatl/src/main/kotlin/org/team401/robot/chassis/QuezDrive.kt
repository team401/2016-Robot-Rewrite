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
package org.team401.robot.chassis

import org.strongback.components.Solenoid
import org.strongback.components.Switch
import org.strongback.components.TalonSRX
import org.team401.robot.components.QuezGearbox
import java.util.concurrent.TimeUnit

class QuezDrive(leftMotors: List<TalonSRX>, rightMotors: List<TalonSRX>, val shifter: Solenoid) {

    val leftGearbox: QuezGearbox
    val rightGearbox: QuezGearbox

    var lastShift: LastShift
    var lastSpeed: Double

    companion object {
        const val MAX_DIF = 0.20
        const val TIME_INTERVAL = 20 // in ms
        // TODO find values
        const val SPEED_CONST = 5.0
        const val LOW_SPEED_CONST = 2.0
        const val ACCEL_CONST = 5.0
    }

    init {
        leftGearbox = QuezGearbox(leftMotors, false)
        rightGearbox = QuezGearbox(rightMotors, true)

        lastShift = LastShift(TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS), 0, 0.0, 0.0)

        lastSpeed = 0.0
    }

    /**
     * Use the gyro to drive straight at the given speed.
     */
    fun drive(speed: Double) = drive(speed, speed)

    /**
     * Drive at different speeds with each gearbox.
     */
    fun drive(leftPitch: Double, rightPitch: Double) {
        val currentSpeed = (leftGearbox.getSpeed() + rightGearbox.getSpeed()) / 2                // get speed
        val currentAccel = (currentSpeed - lastSpeed) / TIME_INTERVAL                            // calculate acceleration
        drive(leftPitch, rightPitch, currentSpeed, currentAccel)
    }

    fun drive(leftPitch: Double, rightPitch: Double, currentSpeed: Double, currentAccel: Double) {
        val currentMs = TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS)   // get current ms
        // only shift if were going mostly straight
        val max = Math.max(leftPitch, rightPitch)
        val dif = Math.abs(leftPitch - rightPitch)
        // check for .5 seconds from last shift
        println("$leftPitch $rightPitch $currentSpeed $currentAccel ${Math.abs(currentMs - lastShift.ms)}")
        if (dif <= MAX_DIF && Math.abs(currentMs - lastShift.ms) > 500) {
            if (!highGear().isTriggered && max > 0 && currentSpeed > SPEED_CONST && currentAccel > ACCEL_CONST) {
                toggleGear(currentSpeed, currentAccel)
            } else if (highGear().isTriggered && currentSpeed <= SPEED_CONST && max > 0.75 && currentAccel < 0) {
                toggleGear(currentSpeed, currentAccel)
            } else if (highGear().isTriggered && currentSpeed <= LOW_SPEED_CONST) {
                toggleGear(currentSpeed, currentAccel)
            }
        }

        leftGearbox.setSpeed(leftPitch)
        rightGearbox.setSpeed(rightPitch)
        lastSpeed = currentSpeed
    }

    fun toggleGear() {
        if (highGear().isTriggered)
            shifter.retract()
        else
            shifter.extend()
    }

    private fun toggleGear(speed: Double, accel: Double) {
        toggleGear()
        lastShift = LastShift(TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS), lastShift.switchCount + 1, speed, accel)
    }

    fun highGear(): Switch {
        return lastShift.highGear
    }

    data class LastShift(val ms: Long, val switchCount: Int, val speed: Double, val accel: Double, val highGear: Switch = Switch { switchCount % 2 == 1 })
}