#include <jni.h>
#include "opus_encoder_native.h"
#include "opus_decoder_native.h"

static jint RegisterOpusNativeMethods(JNIEnv *env) {
    auto ret = opus_native::RegisterOpusEncoderNativeMethods(env);
    if (ret != JNI_OK) {
        return ret;
    }
    ret =  opus_native::RegisterOpusDecoderNativeMethods(env);
    return JNI_OK;
}

#ifdef __cplusplus
extern "C"
{
#endif
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env;
    if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    jint ret = RegisterOpusNativeMethods(env);
    if (ret != JNI_OK) {
        return ret;
    }
    return JNI_VERSION_1_6;
}
#ifdef __cplusplus
}
#endif