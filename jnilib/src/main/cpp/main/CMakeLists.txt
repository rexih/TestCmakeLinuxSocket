# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.



# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.
set(LIB_NAME ${LIB_MAIN})
set(SRC_LIST my-jni.cpp)
add_library(
        ${LIB_NAME}

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        ${SRC_LIST})

set_target_properties(${LIB_NAME}
        PROPERTIES LIBRARY_OUTPUT_DIRECTORY
        "${ARTIFACT_PATH}/${LIB_NAME}/lib/${ANDROID_ABI}")


add_custom_command(TARGET ${LIB_NAME}
        POST_BUILD
        COMMAND "${CMAKE_COMMAND}" -E
        copy_directory "${CMAKE_CURRENT_SOURCE_DIR}/include"
        "${ARTIFACT_PATH}/${LIB_NAME}/include"
        COMMENT "Copying ${LIB_NAME} header files to artifact directory")


#include_directories(${PROJECT_SOURCE_DIR}/myint/)
#include_directories(${PROJECT_SOURCE_DIR}/mylong/)
#include_directories(include/)

target_include_directories(${LIB_NAME} PRIVATE
        ${PROJECT_SOURCE_DIR}/myint/include
        ${PROJECT_SOURCE_DIR}/mylong/include)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.



target_link_libraries( # Specifies the target library.
        ${LIB_NAME}

        # Links the target library to the log library
        # included in the NDK.
        ${log-lib} my-int my-long)

