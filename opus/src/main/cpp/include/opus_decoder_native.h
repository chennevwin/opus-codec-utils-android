/**
 * opus解码器封装
 */

#ifndef ANDROIDOPUSUTILS_OPUS_DECODER_NATIVE_H
#define ANDROIDOPUSUTILS_OPUS_DECODER_NATIVE_H

#include <memory>
#include <jni.h>
#include <vector>

namespace opus_native {
    class OpusDecoderNative {
    public:
        OpusDecoderNative();

        ~OpusDecoderNative() = default;

        /**
         * 初始化解码器
         * @param rate 采样率
         * @param channels 通道数
         * @return 初始化结果
         */
        int init(int rate, int channels);

        /**
         * 释放解码器
         */
        void release();

        int decode(const uint8_t *opus, int len, std::vector<int16_t> &output, int frame_size, int fec = 0);
    private:
        class Impl;
        std::unique_ptr<Impl> impl_;
    };

    jint RegisterOpusDecoderNativeMethods(JNIEnv *env);
}
#endif // ANDROIDOPUSUTILS_OPUS_DECODER_NATIVE_H
