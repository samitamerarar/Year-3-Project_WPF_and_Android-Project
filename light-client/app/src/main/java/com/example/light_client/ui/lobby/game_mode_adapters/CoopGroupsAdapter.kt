package com.example.light_client.ui.lobby.game_mode_adapters

import com.example.light_client.src.services.LobbyService
import com.example.light_client.ui.lobby.LobbyFragment
import com.example.light_client.ui.lobby.LobbyState


class CoopGroupsAdapter(private val groupList: ArrayList<LobbyService.BaseGroup>, private val state: LobbyState) : GroupsAdapter(groupList, state) {
    override lateinit var root: LobbyFragment

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            state.currentCoopGroup = groupList[position] as LobbyService.CoopGroup
            state.lobbyFragmentVisibility = false
            state.groupsFragmentVisibility = false
            state.classicModeLobbyFragmentVisibility = false
            state.soloModeLobbyFragmentVisibility = false
            state.coopModeLobbyFragmentVisibility = true
            state.tournamentModeLobbyFragmentVisibility = false
            root.updateCoopLobby(state.currentCoopGroup)
            root.refreshView()
        }
    }

    fun updateCoopList(a: Array<LobbyService.CoopGroup>?) {
        this.groupList.clear()
        if (a != null) this.groupList.addAll(a)
        if (state.coopModeLobbyFragmentVisibility && a != null) {
            state.currentCoopGroup = this.groupList.find {
                    g -> g.id == state.currentCoopGroup.id }!! as LobbyService.CoopGroup
            root.updateCoopLobby(state.currentCoopGroup)
        }

        notifyDataSetChanged()
    }
}