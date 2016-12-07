//
// Created by cameronearle on 12/6/16.
//

#include "DataSender.hpp"

#include <string>
#include "zhelpers.hpp"
#include "boost/thread/thread.hpp"
#include "boost/date_time/posix_time/posix_time.hpp"
#include "../ThreadManager.hpp"
#include <iostream>


boost::lockfree::spsc_queue<std::vector<float>> DataSender::sendQueue(512);

void DataSender::run() {
    zmq::context_t context(1);
    zmq::socket_t socket(context, ZMQ_PUB);
    socket.bind("tcp://*:" + std::to_string(port));
    std::vector<float> latestData;
    std::string dataToSend;
    while (ThreadManager::get(ThreadManager::Thread::DATA_SENDER)) {
        while (sendQueue.pop(latestData)) {
            if (latestData.size() < 3) {
                break;
            }
            dataToSend = std::to_string(latestData[0]) + "," + std::to_string(latestData[1]) + "," + std::to_string(latestData[2]);
            s_send(socket, dataToSend);
        }
        boost::this_thread::sleep(boost::posix_time::milliseconds(10));
    }
}


void DataSender::addToQueue(std::vector<float> data_) {
    sendQueue.push(data_);
}