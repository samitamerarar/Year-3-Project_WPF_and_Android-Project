package com.example.light_client.src.core

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Json {

    companion object {

        inline fun <reified T> parse(json: String): T {
            return Gson().fromJson(json, object: TypeToken<T>() {}.type)
        }

        fun toString(data: Any): String {
            return Gson().toJson(data)
        }

    }
}