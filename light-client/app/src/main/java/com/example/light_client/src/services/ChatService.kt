package com.example.light_client.src.services

import android.os.Handler
import com.example.light_client.src.core.*
import org.json.JSONObject

class ChatService(private val socket: IOSocket, private val handler: Handler, var channel: String) {
    data class ChatMessage(var author: String, var content: String, var channel: String, var created: String = "")
    data class ChannelForm(var name: String)
    data class MultiChannelsForm(var names: Array<String>, var username: String)
    data class ChannelResponse(var msg: String, var channels: Array<String>? = null)
    data class Channel(var name: String, var chat: Array<ChatMessage>)
    data class ChannelInteraction(var channel: String, var username: String)

    init {
        socket.on(SocketEvents.NEW_MESSAGE) {
            val json = (it[0] as JSONObject).toString()
            handler.sendMessage(handler.obtainMessage(200, Json.parse<ChatMessage>(json)))
        }
    }

    fun sendMessage(message: String, author: String) {
        socket.emit(SocketEvents.NEW_MESSAGE, ChatMessage(author, message, channel))
    }

    fun enterChannel(channel: String, username: String) {
        socket.emit(SocketEvents.ENTER_CHANNEL, ChannelInteraction(channel, username))
    }

    companion object {
        fun newChannel(name: String) {
            Http.post<ChannelResponse>("${Constants.BASE_URL}/channel", ChannelForm(name)) {}
        }

        fun getChannels(handler: Handler) {
            Http.get<Array<Channel>>("${Constants.BASE_URL}/channels") {
                handler.sendMessage(handler.obtainMessage(it.code, it.data))
            }
        }

        fun joinChannels(names: Array<String>, username: String,  handler: Handler) {
            Http.post<ChannelResponse>("${Constants.BASE_URL}/channel/join", MultiChannelsForm(names, username)) {
                handler.sendMessage(handler.obtainMessage(it.code, names))
            }
        }
    }
}