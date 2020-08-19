package com.example.light_client.ui.core

import com.example.light_client.Application

@Suppress("UNCHECKED_CAST")
class StateManager(private var app: Application) {

    abstract class BaseState {
        abstract fun restart(app: Application)
        abstract fun login(app: Application)
    }

    private var stateMap: HashMap<String, BaseState> = hashMapOf()

    fun <T: BaseState> set(key: String, data: T) {
        stateMap[key] = data
        stateMap[key]!!.restart(app)
    }

    fun <T: BaseState> get(key: String, type: Class<T>): T {
        if (!has(key)) set(key, type.newInstance())
        return stateMap[key]!! as T
    }

    fun restart(app: Application) {
        stateMap.forEach {
            it.value.restart(app)
        }
    }

    fun login(app: Application) {
        stateMap.forEach {
            it.value.login(app)
        }
    }

    private fun has(key: String): Boolean {
        return stateMap.containsKey(key)
    }
}