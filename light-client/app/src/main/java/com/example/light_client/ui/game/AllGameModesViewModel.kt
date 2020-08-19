package com.example.light_client.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class AllGameModesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is allmodes Fragment"
    }
    val text: LiveData<String> = _text
}
