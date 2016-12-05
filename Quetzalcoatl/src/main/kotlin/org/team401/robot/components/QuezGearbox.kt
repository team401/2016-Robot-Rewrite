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

import org.strongback.components.TalonSRX

class QuezGearbox(val motors: List<TalonSRX>, inverted: Boolean) {

    // 0 is front, 1 is back, 2 is middle
    init {
        motors[0].invert()
        motors[1].invert()
        if (inverted)
            motors.forEach { it.invert() }
    }

    // TODO add PID control

    fun setSpeed(speed: Double) {
        motors.forEach { it.speed = speed }
    }

    fun getSpeed(): Double {
        return motors[1].speed
    }

    fun getTotalVoltage(): Double {
        return motors[0].voltageSensor.voltage + motors[1].voltageSensor.voltage + motors[2].voltageSensor.voltage
    }

    fun getVoltage(motor: Int): Double {
        if (motor < 0 || motor > 2)
            return 0.0
        else
            return motors[0].voltageSensor.voltage
    }
}