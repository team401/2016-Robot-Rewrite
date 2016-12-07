//
// Created by cameronearle on 12/5/16.
//

#include "Log.hpp"
#include <boost/date_time/posix_time/posix_time.hpp>
#include <boost/date_time/local_time_adjustor.hpp>
#include <boost/date_time/c_local_time_adjustor.hpp>
#include <string>
#include <mutex>
#include <ios>

Log::Level Log::useLevel;
bool Log::useFile;
std::mutex Log::fileLock;
std::ofstream Log::file;

void Log::init(Level level_, bool useFile_, std::string filePath_) {
    useLevel = level_;
    useFile = useFile_;
    if (useFile) {
        file.open(filePath_, std::ios::app);
    }
}

std::string Log::getDateTime() {
    using boost::posix_time::ptime;
    using boost::posix_time::second_clock;
    using boost::posix_time::to_simple_string;
    using boost::gregorian::day_clock;

    return "[" + to_simple_string((day_clock::universal_day(), second_clock::universal_time().time_of_day())) + "]";
}

void Log::writeToFile(std::string outString_) {
    fileLock.lock();
    file << outString_ << "\n";
    fileLock.unlock();
}

void Log::close() {
    if (useFile) {
        file.close();
    }
}

void Log::d(std::string ld_, std::string data_) {
    std::string outString = getDateTime() + " [DEBUG] [" + ld_ + "] " + data_;
    std::cout << outString << std::endl;
    if (useFile && useLevel <= Level::DEBUG) { writeToFile(outString); }
}

void Log::i(std::string ld_, std::string data_) {
    std::string outString = getDateTime() + " [INFO] [" + ld_ + "] " + data_;
    std::cout << outString << std::endl;
    if (useFile && useLevel <= Level::INFO) { writeToFile(outString); }
}

void Log::w(std::string ld_, std::string data_) {
    std::string outString = getDateTime() + " [WARNING] [" + ld_ + "] " + data_;
    std::cerr << outString << std::endl;
    if (useFile && useLevel <= Level::WARNING) { writeToFile(outString); }
}

void Log::e(std::string ld_, std::string data_) {
    std::string outString = getDateTime() + " [ERROR] [" + ld_ + "] " + data_;
    std::cerr << outString << std::endl;
    if (useFile && useLevel <= Level::ERROR) { writeToFile(outString); }
}

void Log::x(std::string ld_, std::string data_) {
    std::string outString = getDateTime() + " [EXCEPTION] [" + ld_ + "] " + data_;
    std::cerr << outString << std::endl;
    if (useFile && useLevel <= Level::EXCEPTION) { writeToFile(outString); }
}

void Log::wtfomgy(std::string ld_, std::string data_){
    std::string outString = getDateTime() + " [WHAT THE FRICK, OH MY GOD WHY?] [" + ld_ + "] " + data_;
    std::cerr << outString << std::endl;
    if (useFile) { writeToFile(outString); }
}