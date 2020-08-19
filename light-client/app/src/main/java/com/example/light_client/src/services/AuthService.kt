package com.example.light_client.src.services

import android.os.Handler
import com.example.light_client.src.core.Constants
import com.example.light_client.src.core.Http
import com.example.light_client.src.core.IOSocket


class AuthService(private var socket: IOSocket) {
    data class AuthForm(var username: String, var password: String)
    data class User(var username: String, var channels: Array<String>, var tutorials: UserTutorials?)
    data class AuthResponse(var msg: String, var user: User)
    data class ProfileResponse(var msg: String, var data: UserProfile)

    data class ResultClassic(var w: Array<String>, var l: Array<String>)
    data class Match(var start: Float, var end: Float, var duration: Float, var participants: Array<String>,
                     var mode: GameMode, var resultClassic: ResultClassic, var resultCoop: Float,
                     var localStart: String, var localEnd: String)
    data class ProfileUpdate(var firstName: String?, var lastName: String?, var avatar: String?)
    data class UserProfile(var firstName: String?, var lastName: String?, var avatar: String?, var username: String,
                           var matchsPlayed: Float, var victoryPercent: Float, var averageMatchDuration: Float,
                           var totalMatchDuration: Float, var soloHighestScore: Float, var coopHighestScore: Float,
                           var history: Array<LoginLogout>, var gameHistory: Array<Match>, var tournamentsWon: Float)
    data class UserTutorials(var classic: Boolean, var solo: Boolean, var coop: Boolean, var tournament: Boolean)
    data class LoginLogout(var login: String, var logout: String)

    fun auth(username: String, password: String, handler: Handler) {
        Http.post<AuthResponse>("${Constants.BASE_URL}/auth", AuthForm(username, password)) {
            handler.sendMessage(handler.obtainMessage(it.code, it.data))
            if (it.code == 200) {
                socket.init(it.data.user.username)
            }
        }
    }

    fun deleteUser(username: String) {
        val url = "${Constants.BASE_URL}/user/delete/$username/"
        Http.post<Any>(url, null) { }
    }

    fun setTutorial(username: String, tutorial: String) {
        val url = "${Constants.BASE_URL}/tutorial/$username/$tutorial"
        Http.post<Any>(url, null) { }
    }

    fun updateProfile(username: String, update: ProfileUpdate, handler: Handler) {
        val url = "${Constants.BASE_URL}/user/profile/$username"
        Http.post<Any>(url, update) {
            if (it.code == 200) {
                getProfile(username, handler)
            }
        }
    }

    fun getProfile(username: String, handler: Handler) {
        val url = "${Constants.BASE_URL}/user/profile/$username"
        Http.get<ProfileResponse>(url) {
            handler.sendMessage(handler.obtainMessage(it.code, it.data.data))
        }
    }
}
