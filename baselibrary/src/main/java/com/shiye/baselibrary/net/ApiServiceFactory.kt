package com.shiye.baselibrary.net

import android.content.Context
import com.shiye.baselibrary.net.client.ClientCreate
import com.shiye.baselibrary.net.gson.getGson
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by issuser on 2018/7/6.
 * 创建网络请求的服务对象
 */
object ApiServiceFactory {
    /**
     * 使用默认的方式创建网络请求
     */
    fun <T> defaultCreateApiService(context: Context, baseUrl: String, injectParams: MutableMap<String, String>?, service: Class<T>): T {
        val defaultHttpClient = ClientCreate(context).createOkHttpClient(injectParams = injectParams)
        return createApiService(context,
                baseUrl,
                defaultHttpClient,
                RxJava2CallAdapterFactory.create(),
                GsonConverterFactory.create(getGson()),
                service)
    }

    /**
     * 使用RxJava2CallAdapterFactory和GsonConverterFactory创建网络请求并解析数据
     */
    fun <T> createApiServiceRXgson(context: Context, baseUrl: String, client: OkHttpClient, service: Class<T>): T {
        return createApiService(context,
                baseUrl,
                client,
                RxJava2CallAdapterFactory.create(),
                GsonConverterFactory.create(getGson()),
                service)
    }

    /**
     * 创建网络请求
     */
    fun <T> createApiService(context: Context,
                             baseUrl: String,
                             client: OkHttpClient,
                             callAdapterFactory: CallAdapter.Factory,
                             converterFactory: Converter.Factory,
                             service: Class<T>): T {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(converterFactory)
                .client(client)
                .build()
                .create(service)
    }
}