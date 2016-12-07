//
// This is a wrapper to contain math data for calculating camera data
//

#ifndef INC_2017_PRESEASON_CODE_MATHDATA_HPP
#define INC_2017_PRESEASON_CODE_MATHDATA_HPP

class MathData {
public:
    MathData(){}
    MathData(float FOV_, float cy_, float cx_, float focalLength_) {
        FOV = FOV_;
        cy = cy_;
        cx = cx_;
        focalLength = focalLength_;
    }

    float getFOV() {return FOV;}
    float getCy() {return cy;}
    float getCx() {return cx;}
    float getFocalLength() {return focalLength;}

    void setFOV(float FOV_) {FOV = FOV_;}
    void setCy(float cy_) {cy = cy_;}
    void setCx(float cx_) {cx = cx_;}
    void setFocalLength(float focalLength_) {focalLength = focalLength_;}
private:
    float FOV;
    float cy;
    float cx;
    float focalLength;
};

#endif //INC_2017_PRESEASON_CODE_MATHDATA_HPP
