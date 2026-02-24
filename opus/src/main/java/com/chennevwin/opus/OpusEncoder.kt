package com.chennevwin.opus

class OpusEncoder : BaseOpus() {
    private var handle = 0L

    /**
     * Initializes the encoder with the given sample rate, number of channels and application.
     * @param sampleRate The input sample rate (Hz). Supported values are 8000, 12000, 16000, 24000, and 48000.
     * @param channels Number of channels. Allowed values are 1 (mono) and 2 (stereo).
     * @param application The intended application. See [OpusApplications].
     * @return init result, [OPUS_OK] is success, other is error
     */
    fun init(sampleRate: Int, channels: Int, application: OpusApplications): Int {
        if (handle == 0L) {
            handle = nativeCreate()
        }
        return nativeInit(handle, sampleRate, channels, application.value)
    }

    /**
     * Releases the Opus encoder.
     */
    fun release() {
        if (handle != 0L) {
            nativeRelease(handle)
            nativeDestroy(handle)
            handle = 0L
        }
    }

    /**
     * Encodes an array of PCM audio data.
     * @param pcm The input PCM audio data.
     * @param frameSize The number of samples per channel in the input PCM data.
     */
    fun encode(pcm: ByteArray, frameSize: Int): ByteArray {
        check()
        return nativeEncode(handle, pcm, frameSize)
    }

    /** Configures the encoder's intended application.
     * The initial value is a mandatory argument to the encoder_create function.
     * @param application see [OpusApplications] value
     */
    fun setApplication(application: OpusApplications): Int {
        return control(OpusEncoderRequest.OPUS_SET_APPLICATION_REQUEST, application.value)
    }

    fun getApplication(): OpusApplications {
        return OpusApplications.fromValue(control(OpusEncoderRequest.OPUS_GET_APPLICATION_REQUEST))
    }

    /** Enables or disables variable bitrate (VBR) in the encoder.
     * The configured bitrate may not be met exactly because frames must
     * be an integer number of bytes in length.
     * @param vbr Allowed values:
     * false:Hard CBR. For LPC/hybrid modes at very low bit-rate, this can cause noticeable quality degradation.
     *
     * true:VBR (default). The exact type of VBR is controlled by [OpusEncoderRequest.OPUS_SET_VBR_CONSTRAINT_REQUEST].
     */
    fun setVBR(vbr: Boolean): Int {
        return control(OpusEncoderRequest.OPUS_SET_VBR_REQUEST, if (vbr) 1 else 0)
    }

    fun getVBR(): Boolean {
        return control(OpusEncoderRequest.OPUS_GET_VBR_REQUEST) != 0
    }

    /** Enables or disables constrained VBR in the encoder.
     * This setting is ignored when the encoder is in CBR mode.
     * @warning Only the MDCT mode of Opus currently heeds the constraint.
     *  Speech mode ignores it completely, hybrid mode may fail to obey it
     *  if the LPC layer uses more bitrate than the constraint would have
     *  permitted.
     *
     * @param enable false:Unconstrained VBR.
     *
     * true:Constrained VBR (default). This creates a maximum of one
     *               frame of buffering delay assuming a transport with a
     *               serialization speed of the nominal bitrate.
     */
    fun setVBRConstraint(enable: Boolean): Int {
        return control(OpusEncoderRequest.OPUS_SET_VBR_CONSTRAINT_REQUEST, if (enable) 1 else 0)
    }

    fun getVBRConstraint(): Boolean {
        return control(OpusEncoderRequest.OPUS_GET_VBR_CONSTRAINT_REQUEST) != 0
    }

    /** Configures the bitrate in the encoder.
     * Rates from 500 to 512000 bits per second are meaningful, as well as the
     * special values [OpusBitrate.OPUS_AUTO] and [OpusBitrate.OPUS_BITRATE_MAX].
     * The value [OpusBitrate.OPUS_BITRATE_MAX] can be used to cause the codec to use as much
     * rate as it can, which is useful for controlling the rate by adjusting the
     * output buffer size.
     * @param bitrate  Bitrate in bits per second. The default
     * is determined based on the number of
     * channels and the input sampling rate.
     */
    fun setBitrate(bitrate: Int): Int {
        return control(OpusEncoderRequest.OPUS_SET_BITRATE_REQUEST, bitrate)
    }

    fun getBitRate(): Int {
        return control(OpusEncoderRequest.OPUS_GET_BITRATE_REQUEST)
    }

    /** Configures the encoder's computational complexity.
     * The supported range is 0-10 inclusive with 10 representing the highest complexity.
     * @param complexity Allowed values: 0-10, inclusive.
     */
    fun setComplexity(complexity: Int): Int {
        return control(OpusEncoderRequest.OPUS_SET_COMPLEXITY_REQUEST, complexity)
    }

    fun getComplexity(): Int {
        return control(OpusEncoderRequest.OPUS_GET_COMPLEXITY_REQUEST)
    }

    /** Configures the type of signal being encoded.
     * This is a hint which helps the encoder's mode selection.
     *
     * @param signal: Allowed values:
     * [OpusSignal.OPUS_AUTO] (default)
     * [OpusSignal.OPUS_SIGNAL_VOICE] Bias thresholds towards choosing LPC or Hybrid modes.
     * [OpusSignal.OPUS_SIGNAL_MUSIC] Bias thresholds towards choosing MDCT modes.
     */
    fun setSignal(signal: OpusSignal): Int {
        return control(OpusEncoderRequest.OPUS_SET_SIGNAL_REQUEST, signal.value)
    }

    fun getSignal(): OpusSignal {
        return OpusSignal.fromValue(control(OpusEncoderRequest.OPUS_GET_SIGNAL_REQUEST))
    }

    fun control(request: OpusEncoderRequest, value: Int = -1): Int {
        return control(request.value, value)
    }

    fun control(request: Int, value: Int = -1): Int {
        check()
        return nativeEncoderControllerInt(handle, request, value)
    }

    private fun check(){
        if(handle == 0L) {
            throw NullPointerException("not init jni handle")
        }
    }
    private external fun nativeCreate(): Long
    private external fun nativeDestroy(handle: Long)
    private external fun nativeInit(
        handle: Long,
        sampleRate: Int,
        channels: Int,
        application: Int
    ): Int

    private external fun nativeRelease(handle: Long)
    private external fun nativeEncode(handle: Long, pcm: ByteArray, frameSize: Int): ByteArray
    private external fun nativeEncoderControllerInt(handle: Long, request: Int, value: Int): Int

}