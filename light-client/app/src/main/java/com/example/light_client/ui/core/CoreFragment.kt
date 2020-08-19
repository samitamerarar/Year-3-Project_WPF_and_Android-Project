package com.example.light_client.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.light_client.Application


abstract class CoreFragment<T: StateManager.BaseState>(private val key: String, private val type: Class<T>, private val layout: Int): Fragment() {

    lateinit var state: T
    val application get() = activity!!.application as Application
    lateinit var root: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        root = inflater.inflate(layout, container, false)
        state = application.stateManager.get(key, type)
        onCreateFragment()
        refreshView()
        return root
    }

    abstract fun refreshView()
    abstract fun onCreateFragment()
}
