package com.shiye.baselibrary.net.data

/**
 * Created by issuser on 2018/7/9.
 *
 *
 *
 */
data class BaseResponse<T>(override var code: String,
                                           override var message: String,
                                           override var data: T?) : IResponse<T> {
}