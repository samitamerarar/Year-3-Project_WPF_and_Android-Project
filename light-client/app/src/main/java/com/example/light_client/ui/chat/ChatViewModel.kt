package com.example.light_client.ui.chat

import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView


class ChatViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is chat Fragment"
    }
    val text: LiveData<String> = _text

    lateinit var channelsRecyclerView: RecyclerView
    lateinit var channelsViewManager: RecyclerView.LayoutManager
    lateinit var chatRecyclerView: RecyclerView
    lateinit var chatViewManager: RecyclerView.LayoutManager
    lateinit var chatButton: Button
    lateinit var chatInput: EditText
    lateinit var channelsSearchButton: Button
    lateinit var channelsAddButton: Button
    lateinit var channelsBackButton: Button
    lateinit var showChatHistoryButton: Button
}