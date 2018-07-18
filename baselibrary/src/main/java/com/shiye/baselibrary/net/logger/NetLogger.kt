package com.shiye.baselibrary.net.logger

import android.util.Log
import com.shiye.baselibrary.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by issuser on 2018/7/6.
 *
 */
class NetLogger : HttpLoggingInterceptor.Logger {
    override fun log(message: String?) {
        if(BuildConfig.DEBUG) {
            Log.i("Http", message)
        }
    }
}