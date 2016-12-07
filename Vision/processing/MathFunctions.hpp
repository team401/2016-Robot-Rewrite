/*
 * MathFunctions.hpp created by Liam Lawrence on 12.5.16
 * Header files for the MathFunctions.cpp file
 *
 */

#ifndef INC_2017_PRESEASON_CODE_MATHFUNCTIONS_HPP
#define INC_2017_PRESEASON_CODE_MATHFUNCTIONS_HPP

#include <vector>
#include "opencv2/core.hpp"


struct MathFunctions {
    static float findDistance(float focalLength, cv::Point pt1, cv::Point pt2);
    static std::vector<float> findAngles(float cx, float cy, float focalLength, cv::Point circleCenter);
};


#endif //INC_2017_PRESEASON_CODE_MATHFUNCTIONS_HPP
