package com.example.light_client.ui.profile

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.light_client.Application
import com.example.light_client.MainActivity
import com.example.light_client.src.services.AuthService
import com.example.light_client.ui.core.StateManager


class ProfileState: StateManager.BaseState() {
    var profileFragmentVisibility = true
    var avatarPickerFragmentVisibility = false

    lateinit var fragmentProfile: ProfileFragment

    val profileHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            if (message.obj != null) {
                if (fragmentProfile.activity != null ) {
                    (fragmentProfile.activity as MainActivity).userAvatarId = ((message.obj) as AuthService.UserProfile).avatar
                    fragmentProfile.applyUserProfile((message.obj) as AuthService.UserProfile)
                }
            }
        }
    }

    override fun login(app: Application) {
        this.restart(app)
    }

    override fun restart(app: Application) {
        //
    }
}
