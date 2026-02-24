cmake_minimum_required(VERSION 3.16...3.26)

# using 16kb page size
set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -Wl,-z,max-page-size=16384")

project("opus_native")

#import opus module
include(cmake/import_opus.cmake)

file(GLOB_RECURSE FILE_SOURCES src/*.cpp)

add_library(${CMAKE_PROJECT_NAME} SHARED ${FILE_SOURCES})

target_include_directories(${CMAKE_PROJECT_NAME}
        PRIVATE include
)

target_link_libraries(${CMAKE_PROJECT_NAME} Opus::opus android log)