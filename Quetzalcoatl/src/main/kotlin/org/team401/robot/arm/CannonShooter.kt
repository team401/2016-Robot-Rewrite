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
package org.team401.robot.arm

import edu.wpi.first.wpilibj.CANTalon
import org.strongback.components.Solenoid
import org.strongback.hardware.Hardware
import org.team401.robot.*
import org.team401.robot.math.PIDGains
import org.team401.robot.math.toRange


/**
 * @param gains PID values for the wheels
 * @param solenoid the solenoid that controls the robot's shooter
 * @param auto whether to use commands to auto shoot or shoot manually
 */
class CannonShooter(leftGains: PIDGains, rightGains: PIDGains, val ballIn: BetterSwitch, val auto: BetterSwitch, var demoMode: BetterSwitch) {

    val leftWheel: CANTalon
    val rightWheel: CANTalon

    val solenoid: Solenoid

    // TODO: fix units for PID

    companion object {
        const val INTAKE_SPEED = 2000.0
    }

    init {
        leftWheel = CANTalon(SHOOTER_LEFT)
        leftWheel.setControlMode(CANTalon.TalonControlMode.Speed.value)
        leftWheel.setPID(leftGains.p, leftGains.i, leftGains.d)
        leftWheel.configEncoderCodesPerRev(20)
        leftWheel.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder)
        leftWheel.enable()

        rightWheel = CANTalon(SHOOTER_RIGHT)
        rightWheel.setControlMode(CANTalon.TalonControlMode.Speed.value)
        rightWheel.setPID(rightGains.p, rightGains.i, rightGains.d)
        rightWheel.configEncoderCodesPerRev(20)
        rightWheel.setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder)
        rightWheel.enable()

        solenoid = Hardware.Solenoids.doubleSolenoid(SHOOTING_SOLENOID, 2, Solenoid.Direction.RETRACTING)
    }

    /**
     * Spins the wheels to take a ball in. If a ball is already in the
     * cannon then the wheels will not spin.
     */
    fun spinIn() {
        if (!isBallIn()) {
            leftWheel.setpoint = INTAKE_SPEED
            rightWheel.setpoint = -INTAKE_SPEED
        } else
            stop()
    }

    /**
     * Spin the wheels at a certain speed to shoot the ball.
     */
    fun spinOut(throttle: Double) {
        val speed = toRange(throttle * -1, -1.0, 1.0, 1000.0, if (demoMode.isTriggered) 3000.0 else 5000.0)
        leftWheel.setpoint = -speed
        rightWheel.setpoint = speed
    }

    /**
     * Stops the shooter wheels from spinning.
     */
    fun stop() {
        leftWheel.setpoint = 0.0
        rightWheel.setpoint = 0.0
    }

    /**
     * Returns whether a ball is in the shooter or not.
     */
    fun isBallIn(): Boolean {
        return ballIn.isTriggered
    }
}