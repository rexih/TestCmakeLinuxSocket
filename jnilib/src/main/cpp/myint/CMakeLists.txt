set(LIB_NAME ${LIB_INT})
set(SRC_LIST my-int.cpp)

add_library(${LIB_NAME} SHARED ${SRC_LIST})

set_target_properties(${LIB_NAME}
        PROPERTIES LIBRARY_OUTPUT_DIRECTORY
        "${ARTIFACT_PATH}/${LIB_NAME}/lib/${ANDROID_ABI}")


add_custom_command(TARGET ${LIB_NAME}
        POST_BUILD
        COMMAND "${CMAKE_COMMAND}" -E
            copy_directory "${CMAKE_CURRENT_SOURCE_DIR}/include"
            "${ARTIFACT_PATH}/${LIB_NAME}/include"
        COMMENT "Copying ${LIB_NAME} header files to artifact directory")

target_link_libraries(${LIB_NAME} ${log-lib})