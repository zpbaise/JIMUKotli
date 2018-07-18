@file:JvmName("GsonUtils")
package com.shiye.baselibrary.net.gson

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Created by issuser on 2018/7/6.
 *
 */
fun getGson():Gson{
    return GsonBuilder()
            .registerTypeAdapter(String::class.java,StringGsonAdapter())
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create()
}