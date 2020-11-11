package com.example.medialearnapp

import android.util.Log

object LogUtil {
    private const val TAG = "LogUtil"

    fun d(msg: String) {
        Log.d(TAG, "$msg")
    }
}