package com.example.light_client.ui.tutorial

import com.example.light_client.Application
import com.example.light_client.ui.core.StateManager


class TutorialState: StateManager.BaseState() {
    var paintFragmentVisibility = true
    var guessFragmentVisibility = false
    var passiveFragmentVisibility = false
    var lobbyFragmentVisibility = false


    override fun login(app: Application) {
        this.restart(app)
    }

    override fun restart(app: Application) {
        //
    }

}