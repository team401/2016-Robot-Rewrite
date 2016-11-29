#include "opencv2/videoio.hpp"
#include "boost/thread/thread.hpp"
#include "iostream"

using namespace std;
using namespace cv;
int main(){


    VideoCapture cap;

    if(!cap.open(0)) {
        return 0;
    }

    cout << "Hello World, Test!";
}