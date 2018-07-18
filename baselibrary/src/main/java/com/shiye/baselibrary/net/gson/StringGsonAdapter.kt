package com.shiye.baselibrary.net.gson

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

import java.lang.reflect.Type

/**
 * Created by issuser on 2018/6/12.
 */

class StringGsonAdapter : JsonSerializer<String>, JsonDeserializer<String> {
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): String {
        return if (json.asString == "" || json.asString == "null") {
            //定义为string类型,如果后台返回""或者null,则返回""
            ""
        } else json.asString
    }

    override fun serialize(src: String, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return JsonPrimitive(src)
    }
}
