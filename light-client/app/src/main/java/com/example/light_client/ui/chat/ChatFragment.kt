package com.example.light_client.ui.chat

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.InputFilter
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.light_client.Application
import com.example.light_client.R
import com.google.android.material.textfield.TextInputLayout
import com.example.light_client.MainActivity
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.light_client.src.services.ChatService
import com.example.light_client.ui.core.CoreFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class ChatFragment : CoreFragment<ChatState>("chat", ChatState::class.java, R.layout.chat_fragment) {
    lateinit var chatViewModel: ChatViewModel

    private val listChannelsHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            val channels = (message.obj as Array<ChatService.Channel>).map { c -> c.name}
            val checkedItems = BooleanArray(channels.size)

            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle("Joindre un Canal")
            builder.setMultiChoiceItems(channels.toTypedArray(), checkedItems
            ) { _, which, isChecked ->
                checkedItems[which] = isChecked
            }
            builder.setPositiveButton("JOINDRE"
            ) { _, _ ->
                val names = channels.withIndex().filter { checkedItems[it.index] }.map { (_, value) -> value }
                val username = (activity!!.application as Application).user.username
                ChatService.joinChannels(names.toTypedArray(), username, joinChannelHandler)
            }
            builder.setNegativeButton("Annuler", null)
            val dialog = builder.create()
            dialog.show()
        }
    }

    private val joinChannelHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            val channels = message.obj as Array<String>
            state.channelsAdapter.addChannels(channels)
            state.addChannels(channels)
            state.getHistory()
        }
    }

    fun displayChat(channel: String) {
        state.enterChannel(channel)
        state.channelsVisibility = false
        state.chatVisibility = true
        refreshView()
    }

    override fun refreshView() {
        with(chatViewModel) {
            if (!state.channelsVisibility) {
                channelsRecyclerView.visibility = View.GONE
                root.findViewById<LinearLayout>(R.id.channels_list_header).visibility = View.GONE
            } else {
                channelsRecyclerView.visibility = View.VISIBLE
                root.findViewById<LinearLayout>(R.id.channels_list_header).visibility = View.VISIBLE
            }

            if (!state.chatVisibility) {
                chatRecyclerView.visibility = View.GONE
                root.findViewById<LinearLayout>(R.id.chat_send_message_form).visibility = View.GONE
                root.findViewById<LinearLayout>(R.id.chat_header).visibility = View.GONE
            } else {
                chatRecyclerView.visibility = View.VISIBLE
                root.findViewById<TextView>(R.id.chat_header_title).text = state.chatAdapter.currentChannel
                root.findViewById<LinearLayout>(R.id.chat_send_message_form).visibility = View.VISIBLE
                root.findViewById<LinearLayout>(R.id.chat_header).visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateFragment() {
        chatViewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        state.chatFragmentVisibility = true
        initializeRecyclers()
        initializeChatInput()
        initializeChannelHeader()
        removeNotificationBadge()
    }

    private fun initializeRecyclers() {
        with(chatViewModel) {

            state.channelsAdapter.root = this@ChatFragment
            channelsViewManager = LinearLayoutManager(activity)
            channelsRecyclerView = root.findViewById<RecyclerView>(R.id.fragment_channels_recycler_view).apply {
                setHasFixedSize(true)
                layoutManager = channelsViewManager
                adapter = state.channelsAdapter
            }

            state.chatAdapter.root = this@ChatFragment
            chatViewManager = LinearLayoutManager(activity)
            chatRecyclerView = root.findViewById<RecyclerView>(R.id.fragment_chat_recycler_view).apply {
                setHasFixedSize(true)
                layoutManager = chatViewManager
                adapter = state.chatAdapter
            }
        }
    }

    private fun initializeChatInput() {
        with(chatViewModel) {
            chatButton = root.findViewById((R.id.chat_button))
            chatInput = root.findViewById<TextInputLayout>(R.id.chat_content).editText!!

            val sendMessage =  {
                val content = chatInput.text.toString()
                if (!content.isBlank()) {
                    val author = (activity!!.application as Application).user.username
                    state.chatService.sendMessage(content, author)
                    (activity as MainActivity).hideKeyboard(activity)
                }
                chatInput.text.clear()
            }

            chatInput.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                    sendMessage()
                    return@OnKeyListener true
                }
                false
            })

            chatInput.setOnEditorActionListener { _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    sendMessage()
                    true } else { false } }

            chatButton.setOnClickListener {
                sendMessage()
            }
        }
    }

    private fun initializeChannelHeader() {
        with(chatViewModel) {
            channelsSearchButton = root.findViewById((R.id.search_channel_button))
            channelsAddButton = root.findViewById(R.id.new_channel_button)
            channelsBackButton = root.findViewById(R.id.back_channel_button)
            showChatHistoryButton = root.findViewById(R.id.show_chat_history_button)

            channelsSearchButton.setOnClickListener {
                ChatService.getChannels(listChannelsHandler)
            }

            channelsAddButton.setOnClickListener {
                addChannel()
            }

            channelsBackButton.setOnClickListener {
                state.channelsVisibility = true
                state.chatVisibility = false
                refreshView()
            }

            showChatHistoryButton.setOnClickListener {
                state.chatAdapter.showHistory()
            }
        }
    }

    private fun addChannel() {
        val input = EditText(activity!!)
        input.maxLines = 1
        input.hint = "Entrer le nom du nouveau canal"
        input.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(15))

        val alert = AlertDialog.Builder(activity!!)
            .setTitle("Nouveau canal de dicussion")
            .setView(input)
            .setPositiveButton(R.string.creer_canal
            ) { _, _ ->
                ChatService.newChannel(input.text.toString())
            }
            .setNegativeButton(R.string.annuler, null)

        val container = FrameLayout(activity!!)
        val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val margin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
        params.setMargins(margin, margin, margin, margin)
        input.layoutParams = params
        container.addView(input)
        alert.setView(container)
        alert.show()
    }

    private fun removeNotificationBadge() {
        state.badge.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        state.chatFragmentVisibility = false
    }

}