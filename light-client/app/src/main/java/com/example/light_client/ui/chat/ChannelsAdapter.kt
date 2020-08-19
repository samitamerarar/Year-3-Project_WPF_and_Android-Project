package com.example.light_client.ui.chat

import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.light_client.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class ChannelsAdapter(private val channelsNames: ArrayList<String>) : RecyclerView.Adapter<ChannelsAdapter.ChannelsViewHolder>() {
    data class ChannelViewData(var name: String, var notifications: Int)

    lateinit var root: ChatFragment
    var channels: ArrayList<ChannelViewData>

    init {
        channels = ArrayList(channelsNames.map { c -> ChannelViewData(c, 0) })
    }

    class ChannelsViewHolder(layout: LinearLayout) : RecyclerView.ViewHolder(layout) {
        var name: TextView = layout.findViewById(R.id.fragment_channel_item_name)
        var notifications: TextView = layout.findViewById(R.id.channel_notification_badge)
        var button: Button = layout.findViewById(R.id.enter_channel_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelsViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.chat_fragment_channel_item, parent, false) as LinearLayout
        return ChannelsViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ChannelsViewHolder, position: Int) {
        holder.name.text = channels[position].name
        if (channels[position].notifications == 0) {
            holder.notifications.visibility = View.GONE
        } else {
            holder.notifications.text = channels[position].notifications.toString()
            holder.notifications.visibility = View.VISIBLE
        }

        holder.button.setOnClickListener {
            channels[position].notifications = 0
            notifyDataSetChanged()
            root.displayChat(channels[position].name)
        }
    }

    override fun getItemCount() = channels.size

    fun addChannels(channels: Array<String>) {
        channels.forEach {
            if (!channelsNames.contains(it)) {
                channelsNames.add(it)
                this.channels.add(ChannelViewData(it, 0))
            }
        }
        notifyDataSetChanged()
    }

    fun pushNotification(channel: String) {
        if (!channelsNames.contains(channel)) { return }
        channels.find { c -> c.name == channel }!!.notifications++
        notifyDataSetChanged()
    }

    fun notifyWithSound() {
        val mp = MediaPlayer.create(root.context, R.raw.beyond_doubt_2)
        mp.start()
        mp.setOnCompletionListener { mediaPlayer ->
            mediaPlayer.stop()
            mediaPlayer.reset()
            mediaPlayer.release()
        }
    }
}