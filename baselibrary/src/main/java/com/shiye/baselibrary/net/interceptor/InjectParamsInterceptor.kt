package com.shiye.baselibrary.net.interceptor

import android.text.TextUtils

import java.io.IOException
import java.nio.charset.Charset
import java.util.ArrayList
import java.util.HashMap

import okhttp3.FormBody
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer

/**
 * Created by Administrator on 2018/2/6.
 *
 *
 * 拦截器，用于注入公共请求参数
 */

class InjectParamsInterceptor private constructor(private var urlParamsMap: MutableMap<String, String>,
                                                  private var postParamsMap: MutableMap<String, String>,
                                                  private var headerParamsMap: MutableMap<String, String>,
                                                  private var headerLinesList: MutableList<String>) : Interceptor {
    private constructor(builder: Builder) : this(builder.urlParamsMap,
            builder.postParamsMap,
            builder.headerParamsMap,
            builder.headerLinesList)

    private val UTF8 = Charset.forName("UTF-8")

    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        var request: Request? = chain.request()
        val requestBuilder = request!!.newBuilder()
        val headerBuilder = request.headers().newBuilder()

        // 以 Entry 添加消息头
        if (headerParamsMap.size > 0) {
            val iterator = headerParamsMap.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                headerBuilder.add(entry.key, entry.value)
            }
        }

        // 以 String 形式添加消息头
        if (headerLinesList.size > 0) {
            for (line in headerLinesList) {
                headerBuilder.add(line)
            }
            requestBuilder.headers(headerBuilder.build())
        }

        //将参数以url的方式拼接到地址
        if (urlParamsMap.size > 0) {
            request = injectParamsIntoUrl(request.url().newBuilder(), requestBuilder, urlParamsMap)
        }

        // post请求方式添加参数
        if (postParamsMap.size > 0) {
            if (canInjectIntoBody(request)) {
                val formBodyBuilder = FormBody.Builder()
                for ((key, value) in postParamsMap) {
                    formBodyBuilder.add(key, value)
                }

                val formBody = formBodyBuilder.build()
                var postBodyString = ""
                request?.let {
                    it.body()?.let {
                        postBodyString = bodyToString(it)
                    }
                }
                postBodyString += (if (postBodyString.length > 0) "&" else "") + bodyToString(formBody)
                requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString))
            }
        }
        request = requestBuilder.build()
        return chain.proceed(request!!)
    }

    /**
     * 检查是否post请求,只有post请求才可以添加参数，其他方式不会将添加参数
     *
     * @param request 发出的请求
     * @return true
     */
    private fun canInjectIntoBody(request: Request?): Boolean {
        if (request == null) {
            return false
        }
        if (!TextUtils.equals(request.method(), "POST")) {
            return false
        }
        val body = request.body()
        body?.let {
            val mediaType = body.contentType() ?: return false
            if (!TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded")) {
                return false
            }
        }
        return true
    }

    // func to inject params into url
    private fun injectParamsIntoUrl(httpUrlBuilder: HttpUrl.Builder, requestBuilder: Request.Builder, paramsMap: Map<String, String>): Request? {
        if (paramsMap.size > 0) {
            val iterator = paramsMap.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                httpUrlBuilder.addQueryParameter(entry.key, entry.value)
            }
            requestBuilder.url(httpUrlBuilder.build())
            return requestBuilder.build()
        }
        return null
    }

    private fun bodyToString(requestBody: RequestBody?): String {
        try {
            val buffer = Buffer()
            if (requestBody != null) {
                requestBody.writeTo(buffer)
            } else {
                return ""
            }
            return buffer.readUtf8()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

    }

    /**
     * 拦截器的构造器
     */
    class Builder {

        val urlParamsMap = mutableMapOf<String, String>() // 添加到 URL 末尾，Get Post 方法都使用
        val postParamsMap = mutableMapOf<String, String>() // 添加到公共参数到消息体，适用 Post 请求
        val headerParamsMap = mutableMapOf<String, String>() // 公共 Headers 添加
        val headerLinesList = mutableListOf<String>() // 消息头 集合形式，一次添加一行

        internal var interceptor: InjectParamsInterceptor? = null

        /**
         * post请求方式，添加公共参数到 post 请求体
         *
         * @param key
         * @param value
         * @return
         */
        fun addParamToBody(key: String, value: String): Builder {
            postParamsMap[key] = value
            return this
        }

        /**
         * post请求方式，添加公共参数到 post 请求体
         *
         * @param paramsMap
         * @return
         */
        fun addParamsMapToBody(paramsMap: MutableMap<String, String>): Builder {
            paramsMap.putAll(paramsMap)
            return this
        }

        /**
         * 添加公共参数到消息头
         *
         * @param key
         * @param value
         * @return
         */
        fun addParamToHeader(key: String, value: String): Builder {
            headerParamsMap[key] = value
            return this
        }

        /**
         * 添加公共参数到消息头
         *
         * @param paramsMap 公共参数的key-value集
         * @return
         */
        fun addParamsMapToHeader(paramsMap: Map<String, String>): Builder {
            headerParamsMap.putAll(paramsMap)
            return this
        }

        /**
         * 添加公共参数到消息头
         *
         * @param headerLine 参数格式为"key:value"
         * @return
         */
        fun addLineToHeader(headerLine: String): Builder {
            val index = headerLine.indexOf(":")
            if (index == -1) {
                throw IllegalArgumentException("Unexpected header: $headerLine")
            }
            headerLinesList.add(headerLine)
            return this
        }

        /**
         * 添加公共参数到消息头
         *
         * @param linesList
         * @return
         */
        fun addLinesListToHeader(linesList: List<String>): Builder {
            for (headerLine in linesList) {
                val index = headerLine.indexOf(":")
                if (index == -1) {
                    throw IllegalArgumentException("Unexpected header: $headerLine")
                }
                headerLinesList.add(headerLine)
            }
            return this
        }

        /**
         * get或post请求方式，添加公共参数到 URL
         *
         * @param key
         * @param value
         * @return
         */
        fun addParamToUrl(key: String, value: String): Builder {
            urlParamsMap[key] = value
            return this
        }

        /**
         * get或post请求方式，添加公共参数到 URL
         *
         * @param queryMap
         * @return
         */
        fun addParamsMapToUrl(queryMap: Map<String, String>): Builder {
            urlParamsMap.putAll(queryMap)
            return this
        }

        fun build() = InjectParamsInterceptor(this)
    }


}
