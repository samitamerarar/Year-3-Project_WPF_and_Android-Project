package com.example.light_client.ui.lobby

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.light_client.Application
import com.example.light_client.src.services.GameMode
import com.example.light_client.src.services.LobbyService
import com.example.light_client.ui.core.StateManager
import com.example.light_client.ui.lobby.game_mode_adapters.ClassicGroupsAdapter
import com.example.light_client.ui.lobby.game_mode_adapters.CoopGroupsAdapter
import com.example.light_client.ui.lobby.game_mode_adapters.SoloGroupsAdapter
import com.example.light_client.ui.lobby.game_mode_adapters.TournamentGroupsAdapter


class LobbyState: StateManager.BaseState() {
    lateinit var classicGroupsAdapter: ClassicGroupsAdapter
    lateinit var soloGroupsAdapter: SoloGroupsAdapter
    lateinit var coopGroupsAdapter: CoopGroupsAdapter
    lateinit var tournamentGroupsAdapter: TournamentGroupsAdapter
    lateinit var lobbyService: LobbyService
    lateinit var currentClassicGroup: LobbyService.ClassicGroup
    lateinit var currentSoloGroup: LobbyService.SoloGroup
    lateinit var currentCoopGroup: LobbyService.CoopGroup
    lateinit var currentTournamentGroup: LobbyService.TournamentGroup

    var lobbyFragmentVisibility = true
    var groupsFragmentVisibility = false
    var classicModeLobbyFragmentVisibility = false
    var soloModeLobbyFragmentVisibility = false
    var coopModeLobbyFragmentVisibility = false
    var tournamentModeLobbyFragmentVisibility = false


    @Suppress("UNCHECKED_CAST")
    private val lobbyHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            if (lobbyService.currentGroupModeList == GameMode.CLASSIC) {
                val groups = message.obj as Array<LobbyService.ClassicGroup>
                classicGroupsAdapter.updateClassicList(groups)
            }
            if (lobbyService.currentGroupModeList == GameMode.SOLO) {
                val groups = message.obj as Array<LobbyService.SoloGroup>
                soloGroupsAdapter.updateSoloList(groups)
            }
            if (lobbyService.currentGroupModeList == GameMode.COOP) {
                val groups = message.obj as Array<LobbyService.CoopGroup>
                coopGroupsAdapter.updateCoopList(groups)
            }
            if (lobbyService.currentGroupModeList == GameMode.TOURNAMENT) {
                val groups = message.obj as Array<LobbyService.TournamentGroup>
                tournamentGroupsAdapter.updateTournamentList(groups)
            }
        }
    }

    //implements what to do when user login
    override fun login(app: Application) {
        this.restart(app)
    }

    //implements what to do when application restarts
    override fun restart(app: Application) {
        lobbyFragmentVisibility = true
        groupsFragmentVisibility = false
        classicModeLobbyFragmentVisibility = false
        soloModeLobbyFragmentVisibility = false
        coopModeLobbyFragmentVisibility = false
        tournamentModeLobbyFragmentVisibility = false
        lobbyService = LobbyService(app.socket, lobbyHandler)
        classicGroupsAdapter =
            ClassicGroupsAdapter(
                arrayListOf(),
                this
            )
        coopGroupsAdapter = CoopGroupsAdapter(
            arrayListOf(),
            this
        )
        soloGroupsAdapter = SoloGroupsAdapter(
            arrayListOf(),
            this
        )
        tournamentGroupsAdapter = TournamentGroupsAdapter(
                arrayListOf(),
        this
        )
    }
}