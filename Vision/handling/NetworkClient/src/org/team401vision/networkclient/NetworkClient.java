package org.team401vision.networkclient;

import org.zeromq.ZMQ;

import java.util.concurrent.atomic.AtomicReference;


public class NetworkClient implements Runnable {
    private ZMQ.Context context;
    private ZMQ.Socket socket;
    private AtomicReference<VisionData> currentData = new AtomicReference<>();
    public NetworkClient(String address, int port) {
        currentData.set(new VisionData(-1d,-1d,-1d));
        context = ZMQ.context(1);
        socket = context.socket(ZMQ.SUB);
        socket.subscribe("".getBytes());
        socket.connect("tcp://" + address + ":" + port);
    }

    public VisionData getVisionData() {
        return currentData.get();
    }

    public void setVisionData(VisionData visionData) {
        currentData.set(visionData);
    }

    @Override
    public void run() {
        while (true) {
            setVisionData(new VisionData(new String(socket.recv())));
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {}
        }
    }
}