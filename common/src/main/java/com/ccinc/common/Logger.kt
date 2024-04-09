package com.ccinc.common

import android.util.Log

interface Logger {

    companion object {
        const val DEFAULT_TAG = "DEFAULT_TAG_HI_FROM_DEV"
    }

    fun d(tag: String = DEFAULT_TAG, message: String)

    fun e(tag: String = DEFAULT_TAG, message: String)
}

fun androidLogcatLogger(): Logger = object : Logger {

    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun e(tag: String, message: String) {
        Log.e(tag, message)
    }

}