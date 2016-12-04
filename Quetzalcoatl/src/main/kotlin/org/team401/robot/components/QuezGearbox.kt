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
package org.team401.robot.components

import org.strongback.components.Solenoid
import org.strongback.components.Switch
import org.strongback.components.TalonSRX

class QuezGearbox(val motors: List<TalonSRX>, val shifter: Solenoid, inverted: Boolean) {

    // switchCount%2 == 0 is low gear
    var switchCount = 0
        private set
    val highGear: Switch

    // 0 is front, 1 is back, 2 is middle
    init {
        motors[0].invert()
        motors[1].invert()
        if (inverted)
            motors.forEach { it.invert() }
        highGear = Switch { switchCount % 2 == 1 }
    }

    // TODO add PID control

    fun setSpeed(speed: Double) {
        motors.forEach { it.speed = speed }
    }

    fun toggleGear() {
        if (switchCount % 2 == 0) // switch to high
            shifter.extend()
        else // switch to low
            shifter.retract()
        switchCount++
    }
}