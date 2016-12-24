package org.team401.robot;

import org.junit.Test;
import org.strongback.mock.Mock;
import org.team401.robot.math.MathUtilsKt;
import org.team401.robot.math.PIDGains;
import org.team401.robot.util.BetterSwitch;

import static org.junit.Assert.*;

public class RobotTest {

    @Test
    public void testSwitch() {
        BetterSwitch s = new BetterSwitch(Mock.triggeredSwitch());
        assertEquals(s.isTriggered(), true);
        assertEquals(s.invert().isTriggered(), false);
    }

    @Test
    public void testToRange() {
        assertEquals(MathUtilsKt.toRange(0.5, 0, 1, 10, 20), 15, .25);
        assertEquals(MathUtilsKt.toRange(0, 0, 1, 10, 1000), 10, .25);
    }

    @Test
    public void testPIDGains() {
        PIDGains gains = new PIDGains(1, 0, 0);
        assertEquals(gains.getP(), 1, 0.001);
        assertEquals(gains.getI(), 0, 0.001);
        assertEquals(gains.getD(), 0, 0.001);
    }
}