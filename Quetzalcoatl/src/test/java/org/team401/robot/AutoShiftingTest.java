package org.team401.robot;

import org.junit.jupiter.api.Test;
import org.team401.robot.chassis.QuezDrive;

import static org.junit.jupiter.api.Assertions.*;

public class AutoShiftingTest {

    @Test
    public void robotInit() {
        QuezDrive d = TestsKt.getMockQuezDrive();

        // check starting position
        d.drive(80, 80);
        assertTrue(!d.highGear().isTriggered());

        sleep(1000);

        // check upshift
        d.drive(100, 100, 6, 6);
        assertTrue(d.highGear().isTriggered());

        sleep(20);

        // check downshift - massive deceleration
        d.drive(100, 100, 5, -6);
        assertTrue(d.highGear().isTriggered());

        sleep(1000);

        d.drive(100, 100, 4, -6);
        assertTrue(!d.highGear().isTriggered());

        sleep(1000);

        // check downshift - coasting
        d.drive(100, 100, 6, 6);
        assertTrue(d.highGear().isTriggered());
        sleep(20);

        d.drive(40, 40, 3, -1);
        assertTrue(d.highGear().isTriggered());
        sleep(1000);

        d.drive(25, 25, 1, -1);
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