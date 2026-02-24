package com.chennevwin.opus

/**
 * Opus -> PCM
 */
class OpusDecoder : BaseOpus() {
    private var handle: Long = 0L

    /**
     * Initializes the Opus decoder.
     * @param rate The sampling frequency of the input data. This must be one of 8000, 12000, 16000, 24000, or 48000.
     * @param channels The number of channels in the input data. (1 or 2)
     * @return [OPUS_OK] on success, or a negative error code on failure
     */
    fun init(rate: Int, channels: Int): Int {
        if (handle == 0L) {
            handle = nativeCreateDecoder()
        }
        return nativeInit(handle, rate, channels)
    }

    /**
     * Releases the Opus decoder.
     */
    fun release() {
        if (handle != 0L) {
            nativeDestroyDecoder(handle)
            nativeRelease(handle)
            handle = 0L
        }
    }

    /**
     * Decodes a packet of data.
     * @param data Input payload. Use a NULL pointer to indicate packet loss.
     * @param length Number of bytes in payload*.
     * @param frameSize Number of samples per channel of available space in pcm.
     *  If this is less than the maximum packet duration (120ms; 5760 for 48kHz), this function will
     *  not be capable of decoding some packets. In the case of PLC (data==NULL) or FEC (decode_fec=1),
     *  then frame_size needs to be exactly the duration of audio that is missing, otherwise the
     *  decoder will not be in the optimal state to decode the next incoming packet. For the PLC and
     *  FEC cases, frame_size <b>must</b> be a multiple of 2.5 ms.
     * @param fec Flag (0 or 1) to request that any in-band forward error correction data be
     *  decoded. If no such data is available, the frame is decoded as if it were lost.
     * @return pcm audio data
     */
    fun decode(data: ByteArray, length: Int, frameSize: Int, fec: Int = 0): ByteArray {
        if (handle == 0L) {
            throw NullPointerException("not init jni handle")
        }
        val decodedData = nativeDecode(handle, data, length, frameSize, fec)
        return decodedData
    }

    private external fun nativeCreateDecoder(): Long

    private external fun nativeDestroyDecoder(handle: Long)

    private external fun nativeInit(handle: Long, rate: Int, channels: Int): Int
    private external fun nativeRelease(handle: Long)
    private external fun nativeDecode(
        handle: Long,
        data: ByteArray,
        length: Int,
        frameSize: Int,
        fec: Int
    ): ByteArray

}