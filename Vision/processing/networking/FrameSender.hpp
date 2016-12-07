//
// Created by cameronearle on 12/4/16.
//

#ifndef INC_2017_PRESEASON_CODE_FRAMESENDER_HPP
#define INC_2017_PRESEASON_CODE_FRAMESENDER_HPP

#include "zmq.hpp"
#include "opencv2/core.hpp"
#include "boost/lockfree/spsc_queue.hpp"


class FrameSender {
public:
    FrameSender(int port_) {
        port = port_;
    }
    void run();
    static void addToQueue(cv::Mat frame_);
private:
    static boost::lockfree::spsc_queue<cv::Mat> sendQueue;
    int port;
};


#endif //INC_2017_PRESEASON_CODE_FRAMESENDER_HPP
