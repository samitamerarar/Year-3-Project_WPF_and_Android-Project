package com.example.light_client.ui.lobby.game_mode_adapters

import com.example.light_client.src.services.LobbyService
import com.example.light_client.ui.lobby.LobbyFragment
import com.example.light_client.ui.lobby.LobbyState


class TournamentGroupsAdapter(private val groupList: ArrayList<LobbyService.BaseGroup>, private val state: LobbyState) : GroupsAdapter(groupList, state) {
    override lateinit var root: LobbyFragment

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            state.currentTournamentGroup = groupList[position] as LobbyService.TournamentGroup
            state.lobbyFragmentVisibility = false
            state.groupsFragmentVisibility = false
            state.classicModeLobbyFragmentVisibility = false
            state.soloModeLobbyFragmentVisibility = false
            state.coopModeLobbyFragmentVisibility = false
            state.tournamentModeLobbyFragmentVisibility = true
            root.updateTournamentLobby(state.currentTournamentGroup)
            root.refreshView()
        }
    }

    fun updateTournamentList(a: Array<LobbyService.TournamentGroup>?) {
        this.groupList.clear()
        if (a != null) this.groupList.addAll(a)
        if (state.tournamentModeLobbyFragmentVisibility && a != null) {
            state.currentTournamentGroup = this.groupList.find {
                    g -> g.id == state.currentTournamentGroup.id }!! as LobbyService.TournamentGroup
            root.updateTournamentLobby(state.currentTournamentGroup)
        }

        notifyDataSetChanged()
    }
}