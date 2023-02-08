package com.wanjian.cockroach

class CockroachConfig(private val builder: Builder) {
    companion object {
        fun createDefaultHotfixConfig(): CockroachConfig {
            return Builder()
                .setAppLargeVersion("8.0.0")
                .setDebug(false)
                .build()
        }
    }

    fun isDebug(): Boolean {
        return builder.debug
    }

    fun getAppLargeVersion(): String? {
        return builder.appLargeVersion
    }

    fun getAppSmallVersion(): String? {
        return builder.appSmallVersion
    }

    fun getAppVersionCode(): String? {
        return builder.appVersionCode
    }

    fun getCid(): String? {
        return builder.cid
    }

    class Builder {
        internal var appLargeVersion: String? = null
        internal var appSmallVersion: String? = null
        internal var appVersionCode: String? = null
        internal var cid: String? = null
        internal var debug: Boolean = false

        fun setAppLargeVersion(version: String?) = apply {
            appLargeVersion = version
        }

        fun setAppSmallVersion(version: String?) = apply {
            appSmallVersion = version
        }

        fun setAppVersionCode(code: String?) = apply {
            appVersionCode = code
        }

        fun setCid(cid: String?) = apply {
            this.cid = cid
        }

        fun setDebug(debug: Boolean) = apply {
            this.debug = debug
        }

        fun build(): CockroachConfig {
            return CockroachConfig(this)
        }
    }
}