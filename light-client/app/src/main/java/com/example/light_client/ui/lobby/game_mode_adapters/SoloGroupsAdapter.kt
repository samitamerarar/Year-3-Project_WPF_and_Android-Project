package com.example.light_client.ui.lobby.game_mode_adapters

import com.example.light_client.src.services.LobbyService
import com.example.light_client.ui.lobby.LobbyFragment
import com.example.light_client.ui.lobby.LobbyState


class SoloGroupsAdapter(private val groupList: ArrayList<LobbyService.BaseGroup>, private val state: LobbyState) : GroupsAdapter(groupList, state) {
    override lateinit var root: LobbyFragment

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            state.currentSoloGroup = groupList[position] as LobbyService.SoloGroup
            state.lobbyFragmentVisibility = false
            state.groupsFragmentVisibility = false
            state.classicModeLobbyFragmentVisibility = false
            state.soloModeLobbyFragmentVisibility = true
            state.coopModeLobbyFragmentVisibility = false
            state.tournamentModeLobbyFragmentVisibility = false
            root.updateSoloLobby(state.currentSoloGroup)
            root.refreshView()
        }
    }

    fun updateSoloList(a: Array<LobbyService.SoloGroup>?) {
        this.groupList.clear()
        if (a != null) this.groupList.addAll(a)
        if (state.soloModeLobbyFragmentVisibility && a != null) {
            state.currentSoloGroup = this.groupList.find {
                    g -> g.id == state.currentSoloGroup.id }!! as LobbyService.SoloGroup
            root.updateSoloLobby(state.currentSoloGroup)
        }

        notifyDataSetChanged()
    }
}