package com.chennevwin.opus


//opus_errorcodes
/** No error */
const val OPUS_OK = 0

/** One or more invalid/out of range arguments */
const val OPUS_BAD_ARG = -1

/** Not enough bytes allocated in the buffer */
const val OPUS_BUFFER_TOO_SMALL = -2

/** An internal error was detected */
const val OPUS_INTERNAL_ERROR = -3

/** The compressed data passed is corrupted */
const val OPUS_INVALID_PACKET = -4

/** Invalid/unsupported request number */
const val OPUS_UNIMPLEMENTED = -5

/** An encoder or decoder structure is invalid or already freed */
const val OPUS_INVALID_STATE = -6

/** Memory allocation has failed */
const val OPUS_ALLOC_FAIL = -7

enum class OpusEncoderRequest(val value: Int) {
    OPUS_SET_APPLICATION_REQUEST(4000),
    OPUS_GET_APPLICATION_REQUEST(4001),
    OPUS_SET_BITRATE_REQUEST(4002),
    OPUS_GET_BITRATE_REQUEST(4003),
    OPUS_SET_MAX_BANDWIDTH_REQUEST(4004),
    OPUS_GET_MAX_BANDWIDTH_REQUEST(4005),
    OPUS_SET_VBR_REQUEST(4006),
    OPUS_GET_VBR_REQUEST(4007),
    OPUS_SET_BANDWIDTH_REQUEST(4008),
    OPUS_GET_BANDWIDTH_REQUEST(4009),
    OPUS_SET_COMPLEXITY_REQUEST(4010),
    OPUS_GET_COMPLEXITY_REQUEST(4011),
    OPUS_SET_INBAND_FEC_REQUEST(4012),
    OPUS_GET_INBAND_FEC_REQUEST(4013),
    OPUS_SET_PACKET_LOSS_PERC_REQUEST(4014),
    OPUS_GET_PACKET_LOSS_PERC_REQUEST(4015),
    OPUS_SET_DTX_REQUEST(4016),
    OPUS_GET_DTX_REQUEST(4017),
    OPUS_SET_VBR_CONSTRAINT_REQUEST(4020),
    OPUS_GET_VBR_CONSTRAINT_REQUEST(4021),
    OPUS_SET_FORCE_CHANNELS_REQUEST(4022),
    OPUS_GET_FORCE_CHANNELS_REQUEST(4023),
    OPUS_SET_SIGNAL_REQUEST(4024),
    OPUS_GET_SIGNAL_REQUEST(4025),
    OPUS_GET_LOOKAHEAD_REQUEST(4027),

    /* OPUS_RESET_STATE = 4028 */
    OPUS_GET_SAMPLE_RATE_REQUEST(4029),
    OPUS_GET_FINAL_RANGE_REQUEST(4031),
    OPUS_GET_PITCH_REQUEST(4033),
    OPUS_SET_GAIN_REQUEST(4034),
    OPUS_GET_GAIN_REQUEST(4045), /* Should have been = 4035 */
    OPUS_SET_LSB_DEPTH_REQUEST(4036),
    OPUS_GET_LSB_DEPTH_REQUEST(4037),
    OPUS_GET_LAST_PACKET_DURATION_REQUEST(4039),
    OPUS_SET_EXPERT_FRAME_DURATION_REQUEST(4040),
    OPUS_GET_EXPERT_FRAME_DURATION_REQUEST(4041),
    OPUS_SET_PREDICTION_DISABLED_REQUEST(4042),
    OPUS_GET_PREDICTION_DISABLED_REQUEST(4043),

    /* Don't use = 4045, it's already taken by OPUS_GET_GAIN_REQUEST */
    OPUS_SET_PHASE_INVERSION_DISABLED_REQUEST(4046),
    OPUS_GET_PHASE_INVERSION_DISABLED_REQUEST(4047),
    OPUS_GET_IN_DTX_REQUEST(4049),
    OPUS_SET_DRED_DURATION_REQUEST(4050),
    OPUS_GET_DRED_DURATION_REQUEST(4051),
    OPUS_SET_DNN_BLOB_REQUEST(4052);
}

enum class OpusApplications(val value: Int) {
    /** Best for most VoIP/videoconference applications where listening quality and intelligibility matter most*/
    OPUS_APPLICATION_VOIP(2048),

    /** Best for broadcast/high-fidelity application where the decoded audio should be as close as possible to the input*/
    OPUS_APPLICATION_AUDIO(2049),

    /** Only use when lowest-achievable latency is what matters most. Voice-optimized modes cannot be used.*/
    OPUS_APPLICATION_RESTRICTED_LOW_DELAY(2051);

    companion object {
        @JvmStatic
        fun fromValue(value: Int): OpusApplications {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("Invalid value for OpusApplications")
        }
    }
}


object OpusBitrate {
    const val OPUS_AUTO = -1000
    const val OPUS_BITRATE_MAX = -1
}

enum class OpusSignal(val value: Int) {
    /** default*/
    OPUS_AUTO(-1000),

    /** Signal being encoded is voice */
    OPUS_SIGNAL_VOICE(3001),

    /** Signal being encoded is music */
    OPUS_SIGNAL_MUSIC(3002);

    companion object {
        @JvmStatic
        fun fromValue(value: Int): OpusSignal {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("Invalid value for OpusSignal")
        }
    }
}

enum class OpusFrameDuration(val value: Int) {
    /** Select frame size from the argument (default) */
    OPUS_FRAMESIZE_ARG(5000),

    /** Use 2.5 ms frames */
    OPUS_FRAMESIZE_2_5_MS(5001),

    /** Use 5 ms frames */
    OPUS_FRAMESIZE_5_MS(5002),

    /** Use 10 ms frames */
    OPUS_FRAMESIZE_10_MS(5003),

    /** Use 20 ms frames */
    OPUS_FRAMESIZE_20_MS(5004),

    /** Use 40 ms frames */
    OPUS_FRAMESIZE_40_MS(5005),

    /** Use 60 ms frames */
    OPUS_FRAMESIZE_60_MS(5006),

    /** Use 80 ms frames */
    OPUS_FRAMESIZE_80_MS(5007),

    /** Use 100 ms frames */
    OPUS_FRAMESIZE_100_MS(5008),

    /** Use 120 ms frames */
    OPUS_FRAMESIZE_120_MS(5009);

    companion object {
        @JvmStatic
        fun fromValue(value: Int): OpusFrameDuration {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("Invalid value for OpusSignal")
        }
    }
}

enum class OpusBandwidth(val value: Int) {
    /**(default)*/
    OPUS_AUTO(-1000),

    /** 4 kHz bandpass */
    OPUS_BANDWIDTH_NARROWBAND(1101),

    /** 6 kHz bandpass */
    OPUS_BANDWIDTH_MEDIUMBAND(1102),

    /** 8 kHz bandpass */
    OPUS_BANDWIDTH_WIDEBAND(1103),

    /**12 kHz bandpass */
    OPUS_BANDWIDTH_SUPERWIDEBAND(1104),

    /**20 kHz bandpass */
    OPUS_BANDWIDTH_FULLBAND(1105);

    companion object {
        @JvmStatic
        fun fromValue(value: Int): OpusBandwidth {
            return entries.find { it.value == value }
                ?: throw IllegalArgumentException("Invalid value for OpusSignal")
        }
    }
}
