package com.shiye.baselibrary.net.exception

/**
 * Created by issuser on 2018/7/11.
 * 网络异常的统一异常类，
 */

class ResponseException(private var originalException: Throwable, var code: String, override var message: String) : Exception(originalException) {
    init {
        originalException.printStackTrace()
    }
}