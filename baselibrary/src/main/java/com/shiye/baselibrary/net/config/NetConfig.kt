@file:JvmName("NetConfig")

package com.shiye.baselibrary.net.config

/**
 * Created by issuser on 2018/7/6.
 * 网络配置的常量
 */
/**
 * 打印网络日志的标记
 */
const val TAG_LOGGER = "http_log_tag"
/**
 * 读超时
 */
const val TIMEOUT_READ = 15L
/**
 * 连接超时
 */
const val TIMEOUT_CONNECT = 15L
/**
 * 写超时
 */
const val TIMEOUT_WRITE = 15L

/**
 * 缓存大小50MB
 */
const val CACHE_SIZE = 50 * 1024 * 1024L