#include <opencv2/videoio/videoio_c.h>
#include "opencv2/videoio.hpp"
#include "boost/thread/thread.hpp"
#include "imgProc/VisionProcessing.hpp"
#include "MathData.hpp"
#include "camera/CameraSettings.hpp"
#include "networking/FrameSender.hpp"
#include "networking/DataSender.hpp"
#include "zmq.hpp"
#include "ThreadManager.hpp"
#include "dataLogging/Log.hpp"
#include "camera/SetCamera.hpp"

using namespace std;
using namespace cv;


int main(int argc, char *argv[]){
    Log::init(Log::Level::INFO, true);
    std::string ld = "main";
    Log::i(ld, "Vision Processor Starting!");

    // Sets the camera up based on command line arguments
    GrabSettings::defConfig(argc, argv);

    VideoCapture cap;

    if(!cap.open(0)) {
        return 0;
    }

    cap.set(CV_CAP_PROP_FPS, 30); //TODO: Change this to 60 once Cameron gets a real laptop

    MathData mathData;
    mathData.setFOV((57 * 3.141592) / 180);
    mathData.setCy((480 / 2) - 0.5);
    mathData.setCx((640 / 2) - 0.5);
    mathData.setFocalLength(480 / (2*tan(mathData.getFOV()/2)));

    CannyDetector cannyDetector(cap, mathData, Scalar(50,250,40), Scalar(70,255,160), 30, 60);

    FrameSender frameSender(5800);
    DataSender dataSender(5801);

    Log::i(ld, "Setup complete, starting threads");

    boost::thread cannyThread(boost::bind(&CannyDetector::run, cannyDetector));
    boost::thread frameSenderThread(boost::bind(&FrameSender::run, frameSender));
    boost::thread dataSenderThread(boost::bind(&DataSender::run, dataSender));
    cannyThread.join();
    ThreadManager::set(ThreadManager::Thread::GLOBAL, false);

    Log::i(ld, "Program finished, exiting!");
}
