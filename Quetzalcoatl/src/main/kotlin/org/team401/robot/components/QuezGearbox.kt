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
import org.strongback.hardware.Hardware
import org.team401.robot.*

class QuezGearbox(val inverted: Boolean) {

    val leader: TalonSRX
    val front: TalonSRX
    val rear: TalonSRX

    init {
        if (inverted) {
            leader = Hardware.Motors.talonSRX(GEARBOX_RIGHT_MIDDLE)
            front = Hardware.Motors.talonSRX(GEARBOX_RIGHT_FRONT, leader, true)
            rear = Hardware.Motors.talonSRX(GEARBOX_RIGHT_REAR, leader, true)
        } else {
            leader = Hardware.Motors.talonSRX(GEARBOX_LEFT_MIDDLE)
            front = Hardware.Motors.talonSRX(GEARBOX_LEFT_FRONT, leader, true)
            rear = Hardware.Motors.talonSRX(GEARBOX_LEFT_REAR, leader, true)
        }
    }

    // TODO add PID control

    fun setSpeed(speed: Double) {
        if (inverted)
            leader.speed = -speed
        else
            leader.speed = speed
    }

    fun getSpeed(): Double {
        return leader.speed
    }

    fun getVoltage(): Double {
        return leader.voltageSensor.voltage
    }
}