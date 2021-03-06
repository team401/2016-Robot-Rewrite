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
package org.team401.robot.arm.commands

import org.strongback.command.Command
import org.strongback.components.Solenoid

class PushBoulder(val solenoid: Solenoid) : Command() {

    override fun execute(): Boolean {
        solenoid.extend()
        Thread.sleep(500)
        solenoid.retract()
        return true
    }

    override fun end() {
        solenoid.retract()
    }

    override fun interrupted() {
        solenoid.retract()
    }
}