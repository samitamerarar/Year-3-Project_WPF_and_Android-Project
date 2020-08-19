package com.example.light_client.ui.lobby.game_mode_adapters

import com.example.light_client.src.services.LobbyService
import com.example.light_client.ui.lobby.LobbyFragment
import com.example.light_client.ui.lobby.LobbyState


class ClassicGroupsAdapter(private val groupList: ArrayList<LobbyService.BaseGroup>, private val state: LobbyState) : GroupsAdapter(groupList, state) {
    override lateinit var root: LobbyFragment

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            state.currentClassicGroup = groupList[position] as LobbyService.ClassicGroup
            state.lobbyFragmentVisibility = false
            state.groupsFragmentVisibility = false
            state.classicModeLobbyFragmentVisibility = true
            state.soloModeLobbyFragmentVisibility = false
            state.coopModeLobbyFragmentVisibility = false
            state.tournamentModeLobbyFragmentVisibility = false
            root.updateClassicLobby(state.currentClassicGroup)
            root.refreshView()
        }
    }

    fun updateClassicList(a: Array<LobbyService.ClassicGroup>?) {
        this.groupList.clear()
        if (a != null) this.groupList.addAll(a)
        if (state.classicModeLobbyFragmentVisibility && a != null) {
            state.currentClassicGroup = this.groupList.find {
                    g -> g.id == state.currentClassicGroup.id }!! as LobbyService.ClassicGroup
            root.updateClassicLobby(state.currentClassicGroup)
        }

        notifyDataSetChanged()
    }
}