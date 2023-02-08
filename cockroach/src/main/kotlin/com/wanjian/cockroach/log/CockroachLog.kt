package com.wanjian.cockroach.log

import android.util.Log

object CockroachLog {

    fun loge(msg:String,throwable: Throwable? = null,tag:String = "CockroachLog"){
        Log.e(
            tag,
            msg,throwable
        )
    }
    fun logd(msg:String,throwable: Throwable? = null,tag:String = "CockroachLog"){
        Log.d(
            tag,
            msg,throwable
        )
    }
}