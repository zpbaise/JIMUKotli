@file:JvmName("NetStateUtils")

package com.shiye.baselibrary.net.utils

import android.content.Context
import android.net.ConnectivityManager

        /**
         * Created by issuser on 2018/7/9.
         * 判断网络是否连接
         */
fun isConnected(context: Context): Boolean {
    val systemService: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = systemService.getActiveNetworkInfo()
    return networkInfo != null && networkInfo.isConnected
}