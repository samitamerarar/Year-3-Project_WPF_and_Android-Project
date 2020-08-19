package com.example.light_client.ui.lobby.game_mode_adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.light_client.R
import com.example.light_client.src.services.LobbyService
import com.example.light_client.ui.lobby.LobbyFragment
import com.example.light_client.ui.lobby.LobbyState


abstract class GroupsAdapter(private val groupList: ArrayList<LobbyService.BaseGroup>, private val state: LobbyState) : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {
    abstract var root: LobbyFragment

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // internal var gameId: TextView = itemView.findViewById(R.id.game_id) as TextView
        internal var gameMode: TextView = itemView.findViewById(R.id.group_game_mode_title) as TextView
        internal var groupName: TextView = itemView.findViewById(R.id.group_name) as TextView
        internal var groupDifficulty: TextView = itemView.findViewById(R.id.difficulty) as TextView
        internal var groupImageView: ImageView = itemView.findViewById(R.id.group_image_view) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.lobby_fragment_group_item, parent, false) as LinearLayout

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // holder.gameId.text = groupList[position].id
        holder.groupName.text = groupList[position].name
        holder.gameMode.text = translateEnglishtoFrench(groupList[position].mode)
        holder.groupDifficulty.text = translateEnglishtoFrench(groupList[position].difficulty)

        // set a random image
        val images = root.resources.obtainTypedArray(R.array.group_images)
        // val choice = (Math.random() * images.length()).toInt()
        holder.groupImageView.setImageResource(images.getResourceId(position.rem(10), R.drawable.img_purple_01))
        images.recycle()
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    private fun translateEnglishtoFrench(word: String): String {
        when (word) {
            "EASY" -> return "FACILE"
            "INTERMEDIATE" -> return "INTERMÉDIAIRE"
            "HARD" -> return "DIFFICILE"
            "CLASSIC" -> return "MODE CLASSIQUE"
            "SOLO" -> return "MODE SPRINT SOLO"
            "COOP" -> return "MODE SPRINT COOPÉRATIF"
            "TOURNAMENT" -> return "MODE TOURNOI"
        }
        return "undefined"
    }
}