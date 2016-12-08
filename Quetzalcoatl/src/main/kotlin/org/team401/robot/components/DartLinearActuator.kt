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

import org.strongback.components.Switch
import org.strongback.components.TalonSRX
import org.strongback.hardware.Hardware

class DartLinearActuator() {

    val motor: TalonSRX
    val topHoloflex: Switch
    val bottomHoloflex: Switch

    init {
        motor = Hardware.Motors.talonSRX(4)
        topHoloflex = Hardware.Switches.normallyClosed(0)
        bottomHoloflex = Hardware.Switches.normallyClosed(2)
    }

    fun getPosition(): Double {
        return 0.0 // TODO implement
    }

    fun setPosition() {
        // TODO implement
    }

    fun drive(pitch: Double) {
        if (topHoloflex.isTriggered && pitch > 0)
            stop()
        else if (bottomHoloflex.isTriggered && pitch < 0)
            stop()
        else
            motor.speed = pitch

    }

    fun stop() = drive(0.0)
}