package com.example.light_client.ui.chat

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.light_client.R
import com.example.light_client.src.services.ChatService
import com.google.android.material.card.MaterialCardView
import android.annotation.SuppressLint
import android.graphics.Color

typealias ChatMap = HashMap<String, ArrayList<ChatService.ChatMessage>>
class ChatAdapter(var chat: ChatMap) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    var currentChannel: String = "general"
    var currentUsername: String = ""
    var chatHistory: ChatMap = hashMapOf()
    var historyMap: HashMap<String, Boolean> = hashMapOf()

    lateinit var root: ChatFragment

    class ChatViewHolder(layout: LinearLayout) : RecyclerView.ViewHolder(layout) {
        var content: TextView = layout.findViewById(R.id.fragment_chat_item_content)
        var created: TextView = layout.findViewById(R.id.fragment_chat_item_created)
        var author: TextView = layout.findViewById(R.id.fragment_chat_item_author)
        var card: MaterialCardView = layout.findViewById(R.id.fragment_chat_item_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        // create ticktock_eddited_for_looping new view
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.chat_fragment_item, parent, false) as LinearLayout
        return ChatViewHolder(layout)
    }

    @SuppressLint("NewApi", "RtlHardcoded")
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.content.text = chat[currentChannel]!![position].content
        holder.created.text = chat[currentChannel]!![position].created
        holder.author.text = chat[currentChannel]!![position].author

        if (chat[currentChannel]!![position].author == currentUsername) {
            holder.author.text = ""

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.RIGHT
                rightMargin = 8
            }
            holder.card.backgroundTintList = holder.itemView.context.resources.getColorStateList(R.color.colorPrimary)
            holder.content.setTextColor(Color.WHITE)
            holder.card.layoutParams = params
            holder.created.layoutParams = params
        }
    }

    override fun getItemCount() = chat[currentChannel]!!.size

    private fun refreshView() {
        notifyDataSetChanged()
        root.chatViewModel.chatRecyclerView.scrollToPosition(itemCount -1)
    }

    fun addMessage(message: ChatService.ChatMessage) {
        this.chat[message.channel]!!.add(message)
        if (message.channel == currentChannel) refreshView()
    }

    fun showHistory() {
        if (historyMap[currentChannel] !== null) return
        val currentChat = arrayListOf<ChatService.ChatMessage>()
        currentChat.addAll(this.chat[currentChannel]!!)

        this.chat[currentChannel]!!.clear()
        if(chatHistory[currentChannel] != null) this.chat[currentChannel]!!.addAll(chatHistory[currentChannel]!!)
        this.chat[currentChannel]!!.addAll(currentChat)
        refreshView()
        historyMap[currentChannel] = true
    }
}