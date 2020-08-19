package com.example.light_client

import android.app.Application
import android.view.View
import com.example.light_client.src.services.AuthService
import com.example.light_client.src.core.IOSocket
import com.example.light_client.ui.core.StateManager


class Application : Application() {

    lateinit var socket: IOSocket
    lateinit var authService: AuthService
    lateinit var user: AuthService.User
    lateinit var chatBadge: View

    var stateManager: StateManager = StateManager(this)

    override fun onCreate() {
        super.onCreate()
        socket = IOSocket()
        authService = AuthService(socket)
    }

    fun restart() {
        stateManager.restart(this)
    }

    fun login(user: AuthService.User) {
        this.user = user
        stateManager.login(this)
    }
}