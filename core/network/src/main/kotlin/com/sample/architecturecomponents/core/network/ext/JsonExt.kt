package com.sample.architecturecomponents.core.network.ext

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> Any.jsonToObject(gson: Gson? = null): T? {
    return runCatching<T> { (gson ?: Gson()).fromJson(toString(), object : TypeToken<T>() {}.type) }
        .getOrNull()
}

inline fun <reified T> T.objectToJson(gson: Gson? = null): String {
    return (gson ?: Gson()).toJson(this)
}

inline fun <reified I, reified O> I.convert(): O? {
    return objectToJson().jsonToObject()
}