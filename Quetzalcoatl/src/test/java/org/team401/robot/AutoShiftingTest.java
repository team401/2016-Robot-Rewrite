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
package org.team401.robot;

import org.junit.jupiter.api.Test;
import org.team401.robot.chassis.QuezDrive;

import static org.junit.jupiter.api.Assertions.*;

public class AutoShiftingTest {

    @Test
    public void robotInit() {
        QuezDrive d = TestsKt.getMockQuezDrive();

        d.drive(80, 80);
        assertTrue(!d.highGear().isTriggered());

        sleep(1000);
        d.drive(100, 100, 6, 6);
        assertTrue(d.highGear().isTriggered());

        sleep(1000);
        d.drive(100, 100, 4, -6);
        assertTrue(!d.highGear().isTriggered());
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}