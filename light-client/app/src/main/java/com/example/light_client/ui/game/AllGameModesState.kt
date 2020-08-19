package com.example.light_client.ui.game

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.example.light_client.Application
import com.example.light_client.MainActivity
import com.example.light_client.src.services.GameMode
import com.example.light_client.src.services.GameService
import com.example.light_client.ui.core.StateManager


class AllGameModesState: StateManager.BaseState() {
    lateinit var allGameModesFragment: AllGameModesFragment
    lateinit var gameService: GameService
    var alreadySwitchedToTournament = false

    private val gameHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            val state = message.obj as GameService.GameState
            // update only for joined game
            if ((allGameModesFragment.activity as MainActivity).currentGameMode != null &&
                (allGameModesFragment.activity as MainActivity).currentGameMode != GameMode.OTHER) {
                if ((allGameModesFragment.activity as MainActivity).currentGameMode == GameMode.CLASSIC &&
                    (allGameModesFragment.classicFragment.activity as MainActivity).groupIdLobbyJoined == state.id) {
                    allGameModesFragment.classicFragment.initProperPlayerView(state)
                }
                else if ((allGameModesFragment.activity as MainActivity).currentGameMode == GameMode.COOP &&
                    (allGameModesFragment.coopFragment.activity as MainActivity).groupIdLobbyJoined == state.id) {
                    allGameModesFragment.coopFragment.initProperPlayerView(state)
                }
                else if ((allGameModesFragment.activity as MainActivity).currentGameMode == GameMode.TOURNAMENT) {
                    allGameModesFragment.tournamentFragment.initProperPlayerView(state, null)
                }
            }
        }
    }

    private val drawHandler: Handler = object : Handler(Looper.getMainLooper()) {
        @Suppress("UNCHECKED_CAST")
        override fun handleMessage(message: Message) {
            val commands = message.obj as Array<*>
            if ((allGameModesFragment.activity as MainActivity).currentGameMode != GameMode.OTHER) {
                if ((allGameModesFragment.activity as MainActivity).currentGameMode == GameMode.CLASSIC) {
                    if (message.what == 100)
                        allGameModesFragment.classicFragment.addPath(commands as Array<GameService.DrawPoint>)
                    else if (message.what == 200)
                        allGameModesFragment.classicFragment.addPathAsSVGCommand(commands as Array<GameService.SvgCommand>)
                }
                if ((allGameModesFragment.activity as MainActivity).currentGameMode == GameMode.COOP) {
                    if (message.what == 100)
                        allGameModesFragment.coopFragment.addPath(commands as Array<GameService.DrawPoint>)
                    else if (message.what == 200)
                        allGameModesFragment.coopFragment.addPathAsSVGCommand(commands as Array<GameService.SvgCommand>)
                }
                if ((allGameModesFragment.activity as MainActivity).currentGameMode == GameMode.TOURNAMENT) {
                    if (message.what == 100)
                        allGameModesFragment.tournamentFragment.addPath(commands as Array<GameService.DrawPoint>)
                    else if (message.what == 200)
                        allGameModesFragment.tournamentFragment.addPathAsSVGCommand(commands as Array<GameService.SvgCommand>)
                }
            }
        }
    }

    private val winnerHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            val winner = message.obj as GameService.Winner
            if ((allGameModesFragment.activity as MainActivity).currentGameMode != GameMode.OTHER) {
                if ((allGameModesFragment.activity as MainActivity).currentGameMode == GameMode.CLASSIC) {
                    allGameModesFragment.classicFragment.showEndGameView(winner)
                }
                if ((allGameModesFragment.activity as MainActivity).currentGameMode == GameMode.COOP) {
                    allGameModesFragment.coopFragment.showEndGameView(winner)
                }
                if ((allGameModesFragment.activity as MainActivity).currentGameMode == GameMode.TOURNAMENT) {
                    allGameModesFragment.tournamentFragment.showEndGameView(winner)
                }
            }
            if (alreadySwitchedToTournament) alreadySwitchedToTournament = false
        }
    }

    private val tournamentHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            val tournamentBrackets = message.obj as GameService.TournamentState
            if ((allGameModesFragment.activity as MainActivity).currentGameMode != GameMode.OTHER) {
                if(!alreadySwitchedToTournament) {
                    (allGameModesFragment.activity as MainActivity).switchLobbyToTournament()
                    alreadySwitchedToTournament = true
                }
                if ((allGameModesFragment.activity as MainActivity).currentGameMode == GameMode.TOURNAMENT) {
                    allGameModesFragment.tournamentFragment.initProperPlayerView(null, tournamentBrackets)
                }
            }
        }
    }

    private val tournamentRoundsHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            val roundEndInfo = message.obj as GameService.Winner
            if ((allGameModesFragment.activity as MainActivity).currentGameMode != GameMode.OTHER) {
                if ((allGameModesFragment.activity as MainActivity).currentGameMode == GameMode.TOURNAMENT) {
                    allGameModesFragment.tournamentFragment.proposeToQuitOrToStay(roundEndInfo)
                }
            }
        }
    }

    override fun login(app: Application) {
        this.restart(app)
    }

    override fun restart(app: Application) {
        gameService = GameService(app.socket, gameHandler, drawHandler, winnerHandler, tournamentHandler, tournamentRoundsHandler)
    }
}