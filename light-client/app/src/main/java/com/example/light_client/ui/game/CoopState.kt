package com.example.light_client.ui.game

import com.example.light_client.Application
import com.example.light_client.ui.core.StateManager


class CoopState: StateManager.BaseState() {
    var guessFragmentVisibility = true

    override fun login(app: Application) {
        this.restart(app)
    }

    override fun restart(app: Application) {
        //
    }

}