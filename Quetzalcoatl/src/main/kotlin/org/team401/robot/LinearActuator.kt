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

interface LinearActuator {

    /**
     * Get the position of the arm
     */
    fun getPosition(): Double

    fun getTopHolofex(): Boolean

    fun getBottomHolofex(): Boolean

    /**
     * Drive the linear actuator at a certain speed, a positive
     * value should drive it out while a negative value should
     * drive it back in. A speed of 0 should stop the motor.
     */
    fun drive(speed: Double)

    /**
     * Stops the actuator's motor.
     */
    fun stop() {
        drive(0.0)
    }
}