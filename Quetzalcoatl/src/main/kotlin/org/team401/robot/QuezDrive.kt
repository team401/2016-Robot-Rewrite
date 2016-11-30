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
package org.team401.robot

import org.strongback.components.Motor
import org.strongback.components.Relay
import org.strongback.components.Solenoid

import org.strongback.drive.TankDrive

class QuezDrive(val leftGearbox: Motor, val rightGearbox: Motor,
                val leftSolenoid: Solenoid, val rightSolenoid: Solenoid,
                val highGear: Relay) : TankDrive(leftGearbox, rightGearbox, highGear) {

    companion object {
        const val HIGH_GEAR_SWAP = 10.0 // TODO find a value to switch between
    }

    init {
        // do init code here
    }

    /**
     * Use the gyro to drive straight at the given speed.
     */
    fun drive(speed: Double) {
        checkGearSwitch(speed)
        super.tank(speed, speed)
    }

    /**
     * Drive at different speeds with each gearbox.
     */
    fun drive(leftSpeed: Double, rightSpeed: Double) {
        checkGearSwitch((leftSpeed + rightSpeed) / 2)
        super.tank(leftSpeed, rightSpeed)
    }

    /**
     * Check and switch gear boxe
     */
    fun checkGearSwitch(speed: Double) {
        if (speed >= HIGH_GEAR_SWAP) {
            if (highGear.isOff) {
                highGear()
            } else {
                lowGear()
            }
        }
    }

    override fun highGear() {
        leftSolenoid.extend()
        rightSolenoid.extend()
        super.highGear()
    }

    override fun lowGear() {
        leftSolenoid.retract()
        rightSolenoid.retract()
        super.lowGear()
    }
}