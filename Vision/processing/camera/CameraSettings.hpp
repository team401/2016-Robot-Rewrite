//
// Created by cameronearle on 12/3/16.
//

#ifndef INC_2017_PRESEASON_CODE_CAMERASETTINGS_HPP
#define INC_2017_PRESEASON_CODE_CAMERASETTINGS_HPP

#include "string"

class CameraSettings {
public:
    CameraSettings(char* uri);
    bool finish();

    CameraSettings set(int setting, int set);
    CameraSettings autoExposure(bool set);
    CameraSettings autoWB(bool set);
    CameraSettings autoGain(bool set);
    CameraSettings setExposure(int set);
    CameraSettings setSaturation(int set);
    CameraSettings setContrast(int set);
private:
    int descriptor;
    bool validity;
    std::string ld = "CameraSettings";
};


#endif //INC_2017_PRESEASON_CODE_CAMERASETTINGS_HPP
