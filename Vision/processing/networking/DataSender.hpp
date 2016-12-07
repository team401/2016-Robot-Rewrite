//
// Created by cameronearle on 12/6/16.
//

#ifndef INC_2017_PRESEASON_CODE_DATASENDER_HPP
#define INC_2017_PRESEASON_CODE_DATASENDER_HPP

#include <vector>
#include "boost/lockfree/spsc_queue.hpp"

class DataSender {
public:
    DataSender(int port_) {
        port = port_;
    }
    void run();
    static void addToQueue(std::vector<float> data_);
private:
    static boost::lockfree::spsc_queue<std::vector<float>> sendQueue;
    int port;
};


#endif //INC_2017_PRESEASON_CODE_DATASENDER_HPP
