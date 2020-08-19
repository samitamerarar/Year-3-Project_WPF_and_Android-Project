package com.example.light_client.ui.chat

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Gravity
import android.view.View
import com.example.light_client.Application
import com.example.light_client.MainActivity
import com.example.light_client.R
import com.example.light_client.src.services.ChatService
import com.example.light_client.ui.core.StateManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sdsmdg.tastytoast.TastyToast


class ChatState: StateManager.BaseState() {
    lateinit var channelsAdapter: ChannelsAdapter
    lateinit var chatAdapter: ChatAdapter
    lateinit var chatService: ChatService
    lateinit var badge: View

    var chatVisibility = false
    var channelsVisibility = true
    var chatFragmentVisibility = false
    val context: Context? get() = badge.context

    private val chatHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            val m = message.obj as ChatService.ChatMessage
            chatAdapter.addMessage(m)

            if (channelsVisibility || m.channel != chatAdapter.currentChannel) {
                channelsAdapter.pushNotification(m.channel)
            }

            val activeFragment = (channelsAdapter.root.activity as MainActivity).activeFragment
            val fragmentChat = (channelsAdapter.root.activity as MainActivity).fragmentChat
            println(chatFragmentVisibility)
            if (activeFragment != fragmentChat) {
                // badge.visibility = View.VISIBLE
                (channelsAdapter.root.activity as MainActivity).showBadgeOnNavBar()
            }

            if (!(chatFragmentVisibility && chatVisibility && m.channel == chatAdapter.currentChannel)) {
                TastyToast.makeText(context, "NOUVEAU MESSAGE!\n\n" + m.content, TastyToast.LENGTH_LONG, TastyToast.INFO)
                    .setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)

            }
            channelsAdapter.notifyWithSound()
        }
    }

    private val getHistoryHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            val channels = (message.obj as Array<ChatService.Channel>)
            val hashMap: HashMap<String, ArrayList<ChatService.ChatMessage>> = HashMap()
            channels.forEach {
                if (chatAdapter.chat.containsKey(it.name) && !chatAdapter.chatHistory.containsKey(it.name)) {
                    hashMap[it.name] = arrayListOf()
                    hashMap[it.name]!!.addAll(it.chat)
                } else if (chatAdapter.chatHistory.containsKey(it.name)) {
                    hashMap[it.name] = arrayListOf()
                    hashMap[it.name]!!.addAll(chatAdapter.chatHistory[it.name]!!)
                }
            }
            chatAdapter.chatHistory = hashMap
        }
    }

    fun addChannels(channels: Array<String>) {
        channels.forEach {
            if (!chatAdapter.chat.containsKey(it)) {
                chatAdapter.chat[it] = arrayListOf()
                chatService.enterChannel(it, chatAdapter.currentUsername)
            }
        }
    }

    fun enterChannel(channel: String) {
        chatAdapter.currentChannel = channel
        chatService.channel = channel
        if (!chatAdapter.chat.containsKey(channel)) {
            chatAdapter.chat[channel] = arrayListOf()
            chatService.enterChannel(channel, chatAdapter.currentUsername)
        }
        chatAdapter.notifyDataSetChanged()
    }

    fun getHistory() {
        ChatService.getChannels(getHistoryHandler)
    }

    override fun login(app: Application) {
        this.restart(app)
    }

    override fun restart(app: Application) {
        channelsAdapter = ChannelsAdapter(ArrayList(app.user.channels.asList()))

        val hashMap: HashMap<String, ArrayList<ChatService.ChatMessage>> = HashMap()
        app.user.channels.forEach {
            hashMap[it] = arrayListOf()
        }

        chatAdapter = ChatAdapter(hashMap)

        chatVisibility = false
        channelsVisibility = true

        chatAdapter.currentUsername = app.user.username
        chatService = ChatService(app.socket, chatHandler, chatAdapter.currentChannel)
        badge = app.chatBadge

        getHistory()
    }
}