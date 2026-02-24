package com.chennevwin.opus

open class BaseOpus {
    companion object {
        init {
            System.loadLibrary("opus_native")
        }
    }
}