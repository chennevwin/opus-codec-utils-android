#include "opus_encoder_native.h"
#include "opus.h"
#include "jni_tools.h"

namespace opus_native {
    class OpusEncoderNative::Impl {
    public:
        Impl() = default;

        ~Impl() {
            release();
        }

        int init(int rate, int channels, int application) {
            release();
            int ret = OPUS_OK;
            encoder_ = opus_encoder_create(rate, channels, application, &ret);
            return ret;
        }

        void release() {
            if (encoder_) {
                opus_encoder_destroy(encoder_);
                encoder_ = nullptr;
            }
        }

        int encode(const uint8_t *bytes, int frame_size, std::vector<int8_t> &output) {
            static const int MAX_OPUS_PACKET = 1024 * 4;
            auto *buffer = (uint8_t *) malloc((size_t) MAX_OPUS_PACKET);
            int ret = opus_encode(encoder_, (opus_int16 *) bytes, frame_size, buffer, MAX_OPUS_PACKET);
            if (ret > 0) {
                std::copy(&buffer[0], &buffer[ret], std::back_inserter(output));
            }
            free(buffer);
            return ret;
        }

        int controlWithInt(int request, int value) {
            if (request & 1) {
                int ret;
                auto suc = opus_encoder_ctl(encoder_, request, __opus_check_int_ptr(&ret));
                if (suc == OPUS_OK) {
                    return ret;
                }
                return OPUS_INTERNAL_ERROR;
            } else {
                return opus_encoder_ctl(encoder_, request, __opus_check_int(value));
            }
        }

    private:
        OpusEncoder *encoder_{nullptr};
    };

    OpusEncoderNative::OpusEncoderNative() : impl_(std::make_unique<OpusEncoderNative::Impl>()) {
    }

    int OpusEncoderNative::init(int rate, int channels, int application) {
        return impl_->init(rate, channels, application);
    }

    void OpusEncoderNative::release() {
        impl_->release();
    }

    int OpusEncoderNative::encode(const uint8_t *bytes, int frame_size, std::vector<int8_t> &output) {
        return impl_->encode(bytes, frame_size, output);
    }

    int OpusEncoderNative::controlWithInt(int request, int value) {
        return impl_->controlWithInt(request, value);
    }

    static JNIEXPORT jlong JNICALL nativeCreateEncoder(JNIEnv *env, jobject object) {
        return reinterpret_cast<jlong>(new opus_native::OpusEncoderNative());
    }

    static JNIEXPORT void JNICALL nativeDestroyEncoder(JNIEnv *env, jobject object, jlong handle) {
        if (handle == 0) return;
        delete reinterpret_cast<opus_native::OpusEncoderNative *>(handle);
    }

    static JNIEXPORT jint JNICALL nativeInit(JNIEnv *env, jobject object, jlong handle, jint rate, jint channels, jint application) {
        if (handle == 0) return OPUS_ALLOC_FAIL;
        return reinterpret_cast<opus_native::OpusEncoderNative *>(handle)->init(rate, channels, application);
    }

    static JNIEXPORT void JNICALL nativeRelease(JNIEnv *env, jobject object, jlong handle) {
        reinterpret_cast<opus_native::OpusEncoderNative *>(handle)->release();
    }

    static JNIEXPORT jbyteArray JNICALL nativeEncode(JNIEnv *env, jobject object, jlong handle, jbyteArray bytes, jint frame_size) {
        if (handle == 0) return nullptr;
        std::vector<int8_t> output_data;
        jbyte *nativeBytes = env->GetByteArrayElements(bytes, JNI_FALSE);
        int ret = reinterpret_cast<opus_native::OpusEncoderNative *>(handle)->encode(
                reinterpret_cast<const uint8_t *>(nativeBytes), frame_size, output_data);
        if (ret <= 0) {
            env->ReleaseByteArrayElements(bytes, nativeBytes, JNI_FALSE);
            return nullptr;
        }
        jbyteArray result = env->NewByteArray(ret);
        env->SetByteArrayRegion(result, 0, ret, reinterpret_cast<const jbyte *>(output_data.data()));
        env->ReleaseByteArrayElements(bytes, nativeBytes, JNI_FALSE);
        return result;
    }

    static JNIEXPORT jint JNICALL nativeEncoderControllerInt(JNIEnv *env, jobject object, jlong handle, jint request, jint value) {
        if (handle == 0) return OPUS_INTERNAL_ERROR;
        return reinterpret_cast<opus_native::OpusEncoderNative *>(handle)->controlWithInt(request, value);
    }

    static const JNINativeMethod opusEncoderNativeMethods[] = {
            {"nativeCreate", "()J", reinterpret_cast<void *>(nativeCreateEncoder)},
            {"nativeDestroy", "(J)V", reinterpret_cast<void *>(nativeDestroyEncoder)},
            {"nativeInit", "(JIII)I", reinterpret_cast<void *>(nativeInit)},
            {"nativeRelease", "(J)V", reinterpret_cast<void *>(nativeRelease)},
            {"nativeEncode", "(J[BI)[B", reinterpret_cast<void *>(nativeEncode)},
            {"nativeEncoderControllerInt", "(JII)I", reinterpret_cast<void *>(nativeEncoderControllerInt)},
    };

    jint RegisterOpusEncoderNativeMethods(JNIEnv *env) {
        jclass clazz = env->FindClass("com/chennevwin/opus/OpusEncoder");
        if (!clazz) {
            return JNI_ERR;
        }
        auto ret = env->RegisterNatives(clazz, opusEncoderNativeMethods, NELEM(opusEncoderNativeMethods));
        return ret;
    }

} // namespace opus_native