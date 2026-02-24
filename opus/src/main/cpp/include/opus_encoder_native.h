/**
 * opus 编码器封装
 */
#ifndef ANDROIDOPUSUTILS_OPUS_ENCODER_NATIVE_H
#define ANDROIDOPUSUTILS_OPUS_ENCODER_NATIVE_H
#include <memory>
#include <jni.h>
#include <vector>
namespace opus_native {
    class OpusEncoderNative {
    public:
      OpusEncoderNative();

      int init(int rate, int channels, int application);

      void release();

    /**
     * 编码 PCM 16-bit 有符号短整型数据
     * @param bytes PCM 音频数据指针
     * @param frame_size 每声道样本数
     * @param[out] output 编码后数据输出缓冲区
     * @return 编码后数据字节数，负数为错误码
     */
      int encode(const uint8_t* bytes, int frame_size, std::vector<int8_t> &output);

      /**
       * 操作编码器参数
       * @param request 操作请求类型
       * @param value 操作参数
       * @return 操作结果
       */
      int controlWithInt(int request, int value);

    private:
      class Impl;
      std::unique_ptr<Impl> impl_;
    };

    jint RegisterOpusEncoderNativeMethods(JNIEnv *env);
} // namespace opus_native
#endif // ANDROIDOPUSUTILS_OPUS_ENCODER_NATIVE_H
