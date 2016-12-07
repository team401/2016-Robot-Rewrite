package org.team401vision.networkclient.test;

import org.team401vision.networkclient.NetworkClient;
import org.team401vision.networkclient.VisionData;

/**
 * Created by cameronearle on 12/4/16.
 */
public class MainTest {
    public static void main(String[] args) {
        NetworkClient networkClient = new NetworkClient("127.0.0.1", 5801);
        Thread networkClientThread = new Thread(networkClient);
        networkClientThread.start();

        while (true) {
            VisionData currentData = networkClient.getVisionData();
            System.out.println(currentData.getYaw());
            System.out.println(currentData.getPitch());
            System.out.println(currentData.getDistance());
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}
        }
    }
}
