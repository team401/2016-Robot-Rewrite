package org.team401.robot;

import org.junit.jupiter.api.Test;
import org.strongback.components.Motor;
import org.strongback.components.TalonSRX;
import org.strongback.mock.Mock;
import org.strongback.mock.MockSolenoid;
import org.team401.robot.chassis.QuezDrive;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
class RobotTest {

    @Test
    void robotInit() {
        List<TalonSRX> leftMotors = new ArrayList<>();
        leftMotors.add(Mock.stoppedTalonSRX(1));
        leftMotors.add(Mock.stoppedTalonSRX(2));
        leftMotors.add(Mock.stoppedTalonSRX(0));
        List<TalonSRX> rightMotors = new ArrayList<>();
        rightMotors.add(Mock.stoppedTalonSRX(5));
        rightMotors.add(Mock.stoppedTalonSRX(6));
        rightMotors.add(Mock.stoppedTalonSRX(7));

        MockSolenoid solenoid = Mock.instantaneousSolenoid();

        QuezDrive d = new QuezDrive(leftMotors, rightMotors, solenoid);

        assertTrue(!d.highGear().isTriggered());
        d.toggleGear();
        assertTrue(d.highGear().isTriggered());
        d.toggleGear();
        assertTrue(!d.highGear().isTriggered());
        assertEquals(d.getSwitchCount(), 2);
    }
}