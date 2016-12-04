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

class QuezDrive(leftMotors: List<TalonSRX>, rightMotors: List<TalonSRX>, val shifter: Solenoid) {

    val leftGearbox: QuezGearbox
    val rightGearbox: QuezGearbox

    var switchCount = 0
        private set
    private val highGear: Switch

    init {
        leftGearbox = QuezGearbox(leftMotors, false)
        rightGearbox = QuezGearbox(rightMotors, true)

        highGear = Switch { switchCount % 2 == 1 }
    }

    /**
     * Use the gyro to drive straight at the given speed.
     */
    fun drive(speed: Double) = drive(speed, speed)

    /**
     * Drive at different speeds with each gearbox.
     */
    fun drive(leftSpeed: Double, rightSpeed: Double) {
        leftGearbox.setSpeed(leftSpeed)
        rightGearbox.setSpeed(rightSpeed)
    }

    fun toggleGear() {
        if (highGear.isTriggered)
            shifter.retract()
        else
            shifter.extend()
        switchCount++
    }

    fun highGear(): Switch {
        return highGear
    }
}