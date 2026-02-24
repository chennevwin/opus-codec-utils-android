#include "opus_decoder_native.h"
#include "jni_tools.h"
#include "opus.h"

namespace opus_native {
    class OpusDecoderNative::Impl {
    public:
        Impl() = default;

        ~Impl() {
            release();
        }

        void release() {
            if (encoder_) {
                opus_decoder_destroy(encoder_);
                encoder_ = nullptr;
            }
        }

        int init(int rate, int channels) {
            release();
            int ret;
            channels_ = channels;
            encoder_ = opus_decoder_create(rate, channels, &ret);
            return ret;
        }

        int decode(const uint8_t *opus, int len, std::vector<int16_t> &output, int frame_size, int fec) {
            auto *buffer = (opus_int16 *) malloc(channels_ * frame_size * sizeof(opus_int16));
            int ret = opus_decode(encoder_, opus, len, buffer, frame_size, fec);
            if (ret > 0) {
                std::copy(&buffer[0], &buffer[ret], std::back_inserter(output));
            }
            free(buffer);
            return ret;
        }

    private:
        OpusDecoder *encoder_{nullptr};
        int channels_ = 1;
    };

    OpusDecoderNative::OpusDecoderNative() : impl_(std::make_unique<OpusDecoderNative::Impl>()) {
    }

    int OpusDecoderNative::init(int rate, int channels) {
        return impl_->init(rate, channels);
    }

    void OpusDecoderNative::release() {
        impl_->release();
    }

    int OpusDecoderNative::decode(const uint8_t *opus, int len, std::vector<int16_t> &output, int frame_size, int fec) {
        return impl_->decode(opus, len, output, frame_size, fec);
    }


    static JNIEXPORT jlong JNICALL nativeCreateDecoder(JNIEnv *env, jobject object) {
        return reinterpret_cast<jlong>(new opus_native::OpusDecoderNative());
    }

    static JNIEXPORT void JNICALL nativeDestroyDecoder(JNIEnv *env, jobject object, jlong handle) {
        delete reinterpret_cast<opus_native::OpusDecoderNative *>(handle);
    }

    static JNIEXPORT jint JNICALL nativeInit(JNIEnv *env, jobject object, jlong handle, jint rate, jint channels) {
        return reinterpret_cast<opus_native::OpusDecoderNative *>(handle)->init(rate, channels);
    }

    static JNIEXPORT void JNICALL nativeRelease(JNIEnv *env, jobject object, jlong handle) {
        reinterpret_cast<opus_native::OpusDecoderNative *>(handle)->release();
    }

    static JNIEXPORT jbyteArray JNICALL nativeDecode(JNIEnv *env, jobject object, jlong handle, jbyteArray bytes, jint len, jint frame_size, jint fec) {
        if (handle == 0) return nullptr;
        std::vector<int16_t> output;
        jbyte *nativeBytes = env->GetByteArrayElements(bytes, JNI_FALSE);

        auto ret = reinterpret_cast<opus_native::OpusDecoderNative *>(handle)->decode(
                reinterpret_cast<uint8_t *>(nativeBytes), len, output, frame_size, fec);

        int byteArraySize = ret * 2;
        jbyteArray array = env->NewByteArray(byteArraySize);
        std::vector<jbyte> byteBuffer(byteArraySize);
        for (int i = 0; i < ret; i++) {
            int16_t sample = output[i];
            byteBuffer[i * 2] = static_cast<jbyte>(sample & 0xFF);           // Low byte
            byteBuffer[i * 2 + 1] = static_cast<jbyte>((sample >> 8) & 0xFF); // High byte
        }
        env->SetByteArrayRegion(array, 0, byteArraySize, byteBuffer.data());
        env->ReleaseByteArrayElements(bytes, nativeBytes, JNI_ABORT);
        return array;
    }

    static JNINativeMethod opusDecoderNativeMethods[] = {
            {"nativeCreateDecoder", "()J", reinterpret_cast<void *>(nativeCreateDecoder)},
            {"nativeDestroyDecoder", "(J)V", reinterpret_cast<void *>(nativeDestroyDecoder)},
            {"nativeInit", "(JII)I", reinterpret_cast<void *>(nativeInit)},
            {"nativeRelease", "(J)V", reinterpret_cast<void *>(nativeRelease)},
            {"nativeDecode", "(J[BIII)[B", reinterpret_cast<void *>(nativeDecode)},
    };

    jint RegisterOpusDecoderNativeMethods(JNIEnv *env) {
        jclass clazz = env->FindClass("com/chennevwin/opus/OpusDecoder");
        if (!clazz) {
            return JNI_ERR;
        }
        return env->RegisterNatives(clazz, opusDecoderNativeMethods, NELEM(opusDecoderNativeMethods));
    }

}