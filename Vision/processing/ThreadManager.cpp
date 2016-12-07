//
// Created by cameronearle on 12/5/16.
//

#include "ThreadManager.hpp"
#include "dataLogging/Log.hpp"

std::atomic<bool> ThreadManager::GLOBAL_RUNNING(true);
std::atomic<bool> ThreadManager::CANNY_DETECTOR_RUNNING(true);
std::atomic<bool> ThreadManager::FRAME_SENDER_RUNNING(true);
std::atomic<bool> ThreadManager::DATA_SENDER_RUNNING(true);
std::string ThreadManager::ld = "ThreadManager";

void ThreadManager::set(Thread thread_, bool value_) {
    switch (thread_) {
        case GLOBAL:
            GLOBAL_RUNNING = value_;
            Log::d(ld, "Set thread GLOBAL to " + std::to_string(value_));
            break;
        case CANNY_DETECTOR:
            CANNY_DETECTOR_RUNNING = value_;
            Log::d(ld, "Set thread CANNY_DETECTOR to " + std::to_string(value_));
            break;
        case FRAME_SENDER:
            FRAME_SENDER_RUNNING = value_;
            Log::d(ld, "Set thread FRAME_SENDER to " + std::to_string(value_));
            break;
        case DATA_SENDER:
            DATA_SENDER_RUNNING = value_;
            Log::d(ld, "Set thread DATA_SENDER to " + std::to_string(value_));
            break;

    }
}

bool ThreadManager::get(Thread thread_) {
    switch (thread_) {
        case GLOBAL:
            return GLOBAL_RUNNING;
        case CANNY_DETECTOR:
            return (GLOBAL_RUNNING && CANNY_DETECTOR_RUNNING);
        case FRAME_SENDER:
            return (GLOBAL_RUNNING && FRAME_SENDER_RUNNING);
        case DATA_SENDER:
            return (GLOBAL_RUNNING && DATA_SENDER_RUNNING);
    }
}