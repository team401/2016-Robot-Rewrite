package org.team401vision.networkclient;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by cameronearle on 12/6/16.
 */

public class VisionData {
    private AtomicReference<Double> yaw = new AtomicReference<>();
    private AtomicReference<Double> pitch = new AtomicReference<>();
    private AtomicReference<Double> distance = new AtomicReference<>();

    VisionData(double yaw, double pitch, double distance) {
        this.yaw.set(yaw);
        this.pitch.set(pitch);
        this.distance.set(distance);
    }

    VisionData(String combined) {
        String[] split = combined.split(",");
        try {
            this.yaw.set(Double.parseDouble(split[0]));
            this.pitch.set(Double.parseDouble(split[1]));
            this.distance.set(Double.parseDouble(split[2]));
        } catch (Exception ignored) {
            this.yaw.set(-1d);
            this.pitch.set(-1d);
            this.distance.set(-1d);
        }
    }

    public double getYaw() {
        return yaw.get();
    }

    public double getPitch() {
        return pitch.get();
    }

    public double getDistance() {
        return distance.get();
    }
}
