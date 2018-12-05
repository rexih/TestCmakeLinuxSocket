# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)
set(CMAKE_VERBOSE_MAKEFILE on)
project(REXIH)
message(PROJECT_BINARY_DIR: ${PROJECT_BINARY_DIR})
message(PROJECT_SOURCE_DIR: ${PROJECT_SOURCE_DIR})
message(CMAKE_CURRENT_SOURCE_DIR: ${CMAKE_CURRENT_SOURCE_DIR})

#set(CMAKE_LIBRARY_OUTPUT_DIRECTORY ${ARTIFACT_PATH}/${ANDROID_ABI})
set(ARTIFACT_PATH ${PROJECT_SOURCE_DIR}/../artifacts)


find_library(log-lib log)

set(LIB_MAIN my-jni)
set(LIB_LONG my-long)
set(LIB_INT my-int)

add_subdirectory(main)
add_subdirectory(myint)
add_subdirectory(mylong)
