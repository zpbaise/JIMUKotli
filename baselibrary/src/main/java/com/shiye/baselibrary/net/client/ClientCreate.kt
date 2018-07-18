package com.shiye.baselibrary.net.client

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import com.shiye.baselibrary.net.config.CACHE_SIZE
import com.shiye.baselibrary.net.config.TIMEOUT_CONNECT
import com.shiye.baselibrary.net.config.TIMEOUT_READ
import com.shiye.baselibrary.net.config.TIMEOUT_WRITE
import com.shiye.baselibrary.net.interceptor.InjectParamsInterceptor
import com.shiye.baselibrary.net.logger.NetLogger
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File


@SuppressLint("StaticFieldLeak")
/**
 * Created by issuser on 2018/7/6.
 *
 */
class ClientCreate(private var context: Context) {
    init {
        context = context.applicationContext

    }

    /**
     * 创建okhttpclient
     */
    fun createOkHttpClient(builder: OkHttpClient.Builder = getOkHttpBuilder(),
                           cacheEnable: Boolean = false,
                           loggingEnable: Boolean = true,
                           injectParams: MutableMap<String, String>? = null): OkHttpClient {
        if (cacheEnable) {
            builder.cache(createCache())
        }
        if (loggingEnable) {
            builder.addInterceptor(creatHttpLogging())
        }
        injectParams?.let {
            builder.addInterceptor(InjectParamsInterceptor.build { addParamsMapToUrl(it) })
        }
        return builder.build()
    }

    /**
     * 创建日BODY级别的日志拦截器志拦截器
     */
    fun creatHttpLogging(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor(NetLogger())
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    fun getOkHttpBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
    }

    /**
     *
     */
    fun createCache(cacheFile: File = createDefaultCacheFile(context),
                    size: Long = CACHE_SIZE): Cache {
        return Cache(cacheFile, size)
    }

    /**
     * 如果有sd卡，在sd卡创建路径为Android/data/{packageName}/cache的缓存路径
     * 如果没有sd卡，使用默认的缓存作为缓存路径
     */
    private fun createDefaultCacheFile(context: Context): File {
        var defaultCacheFile: File
        val state = Environment.getExternalStorageState()
        //创建缓存路径
        if (state == Environment.MEDIA_MOUNTED) {
            val path = Environment.getExternalStorageDirectory().getPath()
            defaultCacheFile = File(path, "Android/data/${context.packageName}/cache")
        } else {
            defaultCacheFile = context.cacheDir
        }
        return defaultCacheFile
    }

}