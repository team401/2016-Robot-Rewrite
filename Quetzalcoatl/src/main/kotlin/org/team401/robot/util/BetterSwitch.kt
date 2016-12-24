/*
    Quetzalcoatl
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
package org.team401.robot.util

import org.strongback.components.Switch

/**
 * A better implementation of org.strongback.components.Switch
 */
class BetterSwitch(val switch: Switch) : Switch {

    override fun isTriggered(): Boolean {
        return switch.isTriggered
    }

    /**
     * Returns a new util instance that triggers when the original is false
     */
    fun invert(): BetterSwitch {
        return BetterSwitch(Switch { !switch.isTriggered })
    }
}