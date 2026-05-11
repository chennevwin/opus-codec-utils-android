# opus源码库导入
if (NOT TARGET Opus::opus)
    message(STATUS "import opus source code")
    include(FetchContent)
    set(OPUS_DOWNLOAD_URL https://github.com/xiph/opus/releases/download/v1.5.2/opus-1.5.2.tar.gz)

    FetchContent_Declare(
            opus
            URL ${OPUS_DOWNLOAD_URL}
            URL_HASH SHA256=65c1d2f78b9f2fb20082c38cbe47c951ad5839345876e46941612ee87f9a7ce1
    )

    FetchContent_MakeAvailable(opus)
elseif ()
    message(STATUS "found Opus::opus")
endif ()