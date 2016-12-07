//
// Created by cameronearle on 12/4/16.
//
#include "FrameSender.hpp"
#include <opencv2/imgcodecs.hpp>
#include "zhelpers.hpp"
#include "boost/thread/thread.hpp"
#include "boost/date_time/posix_time/posix_time.hpp"
#include "../ThreadManager.hpp"


boost::lockfree::spsc_queue<cv::Mat> FrameSender::sendQueue(512);

void FrameSender::run() {
    zmq::context_t context(1);
    zmq::socket_t socket(context, ZMQ_PUB);
    socket.bind("tcp://*:" + std::to_string(port));
    std::vector<uchar> buff;
    cv::Mat latestFrame;
    while (ThreadManager::get(ThreadManager::Thread::FRAME_SENDER)) {
        while (sendQueue.pop(latestFrame)) {
            cv::imencode(".jpg", latestFrame, buff);
            s_send(socket, std::string(buff.begin(), buff.end()));
            buff.clear();
        }
        boost::this_thread::sleep(boost::posix_time::milliseconds(10));
    }
}


void FrameSender::addToQueue(cv::Mat frame_) {
    sendQueue.push(frame_);
}