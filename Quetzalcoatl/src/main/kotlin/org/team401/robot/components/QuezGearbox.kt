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
import org.strongback.control.TalonController
import org.strongback.hardware.Hardware
import org.team401.robot.math.PIDGains

class QuezGearbox(gains: PIDGains, val inverted: Boolean) {

    val leader: TalonSRX
    val front: TalonSRX
    val rear: TalonSRX

    init {
        if (inverted) {
            leader = Hardware.Motors.talonSRX(7)
            front = Hardware.Motors.talonSRX(5, leader, true)
            rear = Hardware.Motors.talonSRX(6, leader, true)
        } else {
            leader = Hardware.Motors.talonSRX(0)
            front = Hardware.Motors.talonSRX(1, leader, true)
            rear = Hardware.Motors.talonSRX(2, leader, true)
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