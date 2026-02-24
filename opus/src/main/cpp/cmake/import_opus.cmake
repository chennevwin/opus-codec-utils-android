# opus源码库导入
if (NOT TARGET Opus::opus)
    message(STATUS "import opus source code")
    include(FetchContent)
    set(OPUS_DOWNLOAD_URL https://github.com/xiph/opus/releases/download/v1.5.2/opus-1.5.2.tar.gz)
    set(OPUS_PACKAGE_VERSION "1.5.2")
    set(OPUS_PROJECT_VERSION "1.5.2")

    FetchContent_Declare(
        opus
        URL ${OPUS_DOWNLOAD_URL}
        CMAKE_ARGS
            -DBUILD_SHARED_LIBS=OFF
            -DOPUS_BUILD_SHARED_LIBRARY=OFF
            -DOPUS_BUILD_TESTING=OFF
            -DOPUS_BUILD_PROGRAMS=OFF
            -DOPUS_CUSTOM_MODES=ON
            -DOPUS_DISABLE_INTRINSICS=OFF
            -DOPUS_FIXED_POINT=OFF
            -DOPUS_ENABLE_FLOAT_API=ON
            -DOPUS_ASSERTIONS=OFF
            -DOPUS_PACKAGE_VERSION=${OPUS_PACKAGE_VERSION}
            -DOPUS_PROJECT_VERSION=${OPUS_PROJECT_VERSION}
    )

    FetchContent_MakeAvailable(opus)
elseif ()
    message(STATUS "found Opus::opus")
endif ()