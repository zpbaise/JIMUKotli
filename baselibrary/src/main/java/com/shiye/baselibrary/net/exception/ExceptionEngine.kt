@file:JvmName("ExceptionEngine")

package com.shiye.baselibrary.net.exception

import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import java.text.ParseException

/**
 * Created by issuser on 2018/7/11.
 * 统一处理异常，将各种异常统一包装成[ResponseException]异常，并返回
 */
fun handleException(e: Throwable): ResponseException {
    return when (e) {
        is HttpException ->
            ResponseException(e, CODE_HTTP_EXCEPTION, MSG_CONN_EXCEPTION)
        is JsonParseException -> ResponseException(e, CODE_PARSE_EXCEPTION, MSG_PARSE_EXCEPTION)
        is JSONException -> ResponseException(e, CODE_PARSE_EXCEPTION, MSG_PARSE_EXCEPTION)
        is JsonSyntaxException -> ResponseException(e, CODE_PARSE_EXCEPTION, MSG_PARSE_EXCEPTION)
        is ParseException -> ResponseException(e, CODE_PARSE_EXCEPTION, MSG_PARSE_EXCEPTION)
        is MalformedJsonException -> ResponseException(e, CODE_PARSE_EXCEPTION, MSG_PARSE_EXCEPTION)
        is ConnectException -> ResponseException(e, CODE_CONN_EXCEPTION, MSG_CONN_EXCEPTION)
        is ResponseException -> e
        else -> ResponseException(e, CODE_UNKNOWN_EXCEPTION, MSG_UNKNOWN_EXCEPTION)
    }
}