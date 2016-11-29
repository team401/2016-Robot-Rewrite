cmake_minimum_required(VERSION 3.6)
project(2016-Robot-Rewrite)
find_package(OpenCV REQUIRED)
find_package(Boost 1.62.0 REQUIRED COMPONENTS system thread)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")

file(GLOB vision_SRC
        "vision/*.h"
        "vision/*.c"
        "vision/*.hpp"
        "vision/*.cpp"
        )

include_directories(${Boost_INCLUDE_DIRS})
add_executable(2016-Robot-Rewrite ${vision_SRC})
target_link_libraries(2016-Robot-Rewrite ${OpenCV_LIBS} ${Boost_LIBRARIES})