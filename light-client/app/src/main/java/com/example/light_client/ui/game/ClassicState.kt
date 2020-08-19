package com.example.light_client.ui.game

import com.example.light_client.Application
import com.example.light_client.ui.core.StateManager


class ClassicState: StateManager.BaseState() {
    var paintFragmentVisibility = true
    var guessFragmentVisibility = false
    var passiveFragmentVisibility = false

    override fun login(app: Application) {
        this.restart(app)
    }

    override fun restart(app: Application) {
        //
    }

}