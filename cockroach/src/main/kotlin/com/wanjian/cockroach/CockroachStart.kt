package com.wanjian.cockroach

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Looper
import com.wanjian.cockroach.debug.CrashLog
import com.wanjian.cockroach.debug.DebugSafeModeTipActivity
import com.wanjian.cockroach.debug.DebugSafeModeUI
import com.wanjian.cockroach.log.CockroachLog

object CockroachStart {
    private var cockroachConfig: CockroachConfig = CockroachConfig.createDefaultHotfixConfig()
    private var exceptionHandler:ExceptionHandler? = null
    fun install(context: Context,cockroachConfig:CockroachConfig,exceptionHandler:ExceptionHandler? = null){
        this.cockroachConfig = cockroachConfig
        this.exceptionHandler = exceptionHandler
        val sysExcepHandler = Thread.getDefaultUncaughtExceptionHandler()
        if (cockroachConfig.isDebug()){
            DebugSafeModeUI.init(context as Application)
        }
        Cockroach.install(context,object : ExceptionHandler() {
            override fun onUncaughtExceptionHappened(thread: Thread?, throwable: Throwable?) {
                //捕获监听中异常，防止使用方代码抛出异常时导致的反复调用
                CockroachLog.loge("--->onUncaughtExceptionHappened:$thread<---",throwable)
                CrashLog.saveCrashLog(context, throwable)
                exceptionHandler?.uncaughtExceptionHappened(thread, throwable)
            }

            override fun onBandageExceptionHappened(throwable: Throwable?) {
                CockroachLog.loge("--->onBandageExceptionHappened<---",throwable)
                exceptionHandler?.bandageExceptionHappened(throwable)
            }

            override fun onEnterSafeMode() {
                CockroachLog.loge("--->onEnterSafeMode<---")
                exceptionHandler?.enterSafeMode()
                if (cockroachConfig.isDebug()) {
                    DebugSafeModeUI.showSafeModeUI()
                    val intent = Intent(context, DebugSafeModeTipActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                }

            }

            override fun onMayBeBlackScreen(e: Throwable?) {
                super.onMayBeBlackScreen(e)
                val thread = Looper.getMainLooper().thread
                CockroachLog.loge("--->onUncaughtExceptionHappened:$thread<---", e)
                //黑屏时建议直接杀死app
                sysExcepHandler.uncaughtException(thread, RuntimeException("black screen"))
            }
        })
    }
}