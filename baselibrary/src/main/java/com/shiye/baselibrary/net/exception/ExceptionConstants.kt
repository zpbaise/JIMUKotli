@file:JvmName("ExceptionConstants")

package com.shiye.baselibrary.net.exception

/**
 * Created by issuser on 2018/7/11.
 * 网络异常的常量
 */
/**
 * http异常的统一异常码
 */
const val CODE_HTTP_EXCEPTION = "0"
const val MSG_HTTP_EXCEPTION = "http请求异常"

/**
 * 数据解析异常的统一异常码
 */
const val CODE_PARSE_EXCEPTION = "1"
const val MSG_PARSE_EXCEPTION = "数据解析异常"

/**
 * 网络连接异常的统一异常码
 */
const val CODE_CONN_EXCEPTION = "2"
const val MSG_CONN_EXCEPTION = "网络连接异常"

/**
 * 网络未知异常的统一异常码
 */
const val CODE_UNKNOWN_EXCEPTION = "3"
const val MSG_UNKNOWN_EXCEPTION = "未知的异常"

/**
 * 网络未连接异常码
 */
const val CODE_NETWORK_UNCONN = "4"
const val MSG_NETWORK_UNCONN = "设备未连接到网络"


