/*
 * MathFunctions created by Liam Lawrence on 12.5.16
 * Returns the distance from the camera to the object
 * Calculates angles robot needs to turn
 *
 */
#include "MathFunctions.hpp"
#include <vector>
#include "opencv2/core.hpp"

using namespace std;

// distance = (realWidth * focalLength) / pixelWidth --- you can replace realWidth and pixelWidth with height instead
float MathFunctions::findDistance(float focalLength, cv::Point pt1, cv::Point pt2) {
    if (pt1.x == -1 && pt1.y == -1 && pt2.x == -1 & pt2.y == -1) {
        return -1;
    }
    float pixelWidth = (pt1.x - pt2.x);

    return (12 * focalLength) / pixelWidth;
}

// Finds the angles the robot needs to turn
std::vector<float> MathFunctions::findAngles(float cx, float cy, float focalLength, cv::Point circleCenter) {
    if (circleCenter.x == -1 && circleCenter.y == -1) {
        return std::vector<float>({-1,-1});
    }
    std::vector<float> angles;
    // The thing we multiply is degrees per pixel in each direction
    angles.push_back((circleCenter.x - cx) * 0.0890625);   //yaw
    angles.push_back((circleCenter.y - cy) * 0.11875);     //pitch

    return angles;
}