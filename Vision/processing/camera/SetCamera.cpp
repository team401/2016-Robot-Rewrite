/*
 * GrabSettings.cpp created by Liam Lawrence on 12.7.16
 * Sets the camera settings based on command line arguments and a config.txt file
 */

#include "SetCamera.hpp"
#include "CameraSettings.hpp"
#include <iostream>
#include <fstream>

using namespace std;

/* FUNCTION HEADERS */
void setCamera(char *arg[]);
bool toBool(char *arg);

// Load the default values
void GrabSettings::defConfig(int argc, char *argv[]){
    char *defValues[6] = {0};   // Initializes an array of 6 values all set to 0 at the start
    ifstream configFile ("config.txt"); // Opens the file

    // If the file loads, scan it and get the values from the config file
    if (configFile.is_open()){
        for(int i = 0; i < 5; i++){
            configFile >> defValues[i]; // Sets defValues equal to the config file values
        }
        configFile.close();
    }

    else {
        cout << "Unable to open and read file";
    }

    // Sets the camera settings
    if(argc == 0){
        setCamera(defValues);
    }
    else{
        cmdConfig(argv, defValues);
    }

    return;
}


// Gets values from command line
void GrabSettings::cmdConfig(char *argv[], char *defValues[]){
    char *values[6];

    // Sets the camera work with the command line arguments,
    // if you use -1 it will use the default value
    for(int i = 0; i < 6; i++){
        if(argv[i] == "-1"){
            values[i] = defValues[i];
        }
        else{
            values[i] = argv[i];
        }
    }
    // Sets camera settings
    setCamera(values);


    // if the sixth argument is 1, store these values as the new default
    if(argv[6] == "1") {
        ofstream defFile("config.txt");
        if (defFile.is_open()) {
            defFile << values[0] << "\n";
            defFile << values[1] << "\n";
            defFile << values[2] << "\n";
            defFile << values[3] << "\n";
            defFile << values[4] << "\n";
            defFile << values[5] << "\n";
            defFile.close();
        }
        else{
            cout << "Unable to open and write file";
        }
    }
    return;
}

// converts a 1 into true and 0 into false
bool toBool(char *arg){
    return atoi(arg) == 1;
}

// Sets camera settings
void setCamera(char *arg[]){
    CameraSettings("/dev/video0")
            .autoExposure(toBool(arg[0]))
            .autoWB(toBool(arg[1]))
            .autoGain(toBool(arg[2]))
            .setExposure(atoi(arg[3]))
            .setSaturation(atoi(arg[4]))
            .setContrast(atoi(arg[5]))
            .finish();
    return;
}