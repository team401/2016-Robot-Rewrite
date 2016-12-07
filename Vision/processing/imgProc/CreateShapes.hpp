/* Class CreateShapes by Liam Lawrence on 11.28.16
 * Sets up constructor for the CreateShapes class
 */

#include "opencv2/videoio.hpp"
#ifndef INC_2017_PRESEASON_CODE_CREATESSHAPES_HPP
#define INC_2017_PRESEASON_CODE_CREATESSHAPES_HPP
using namespace cv;
using namespace std;

class CreateShapes {
public:
    static std::vector<cv::Point> shapes(Mat &frame, int idx, vector<vector<Point>> contours);
private:
    static std::string ld;
};


#endif //INC_2017_PRESEASON_CODE_CREATESSHAPES_HPP
