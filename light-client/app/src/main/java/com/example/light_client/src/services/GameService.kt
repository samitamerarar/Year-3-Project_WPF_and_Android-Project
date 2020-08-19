package com.example.light_client.src.services

import android.os.Handler
import com.example.light_client.lib.CanvasView
import com.example.light_client.src.core.*
import org.json.JSONArray
import org.json.JSONObject


class GameService(private val socket: IOSocket, private val gameHandler: Handler, private val drawHandler: Handler,
                  private val winnerHandler: Handler, private val tournamentHandler: Handler, private val tournamentRoundsHandler:Handler) {
    data class BracketStatus(var username: String, var status: TournamentPlayerStatusType)
    data class TournamentState(var state: Array<Array<BracketStatus>>, var startTimer: Int?)
    data class GameState(var id: String, var role: PlayerRole, var answer: String?, var points: Int, var otherPoints: Int?, var attemptsLeft: Int,
                         var clear: Boolean, var currentRound: Int, var counter: Boolean, var startTimer: Int?, var timerDirection: TimerDirection?)

    data class PointParams(var pointNature: String, var color: String?, var width: Float?, var height: Float?)
    data class SvgCommand(var type: String, var command: String, var args: Array<Float>)
    data class DrawPoint(var type: String, var action: String, var id: Int, var pointParams: PointParams, var point: CanvasView.Point)
    data class Winner(var winner: Boolean, var score: Float)

    init {
        socket.on(SocketEvents.STATE_UPDATE) {
            val json = (it[0] as JSONObject).toString()
            gameHandler.sendMessage(gameHandler.obtainMessage(200, Json.parse<GameState>(json)))
        }

        socket.on(SocketEvents.TOURNAMENT_STATE) {
            val json = (it[0] as JSONObject).toString()
            tournamentHandler.sendMessage(tournamentHandler.obtainMessage(200, Json.parse<TournamentState>(json)))
        }

        socket.on(SocketEvents.ROUND_FINISHED) {
            val json = (it[0] as JSONObject).toString()
            tournamentRoundsHandler.sendMessage(tournamentRoundsHandler.obtainMessage(200, Json.parse<Winner>(json)))
        }

        socket.on(SocketEvents.DRAW) {
            val json = (it[0] as JSONArray).toString()
            when {
                json.contains("\"type\":\"PATH\"") -> {
                    drawHandler.sendMessage(drawHandler.obtainMessage(200, Json.parse<Array<SvgCommand>>(json)))
                }
                json.contains("\"type\":\"DRAW\"") -> {
                    drawHandler.sendMessage(drawHandler.obtainMessage(100, Json.parse<Array<DrawPoint>>(json)))
                }
            }
        }

        socket.on(SocketEvents.GAME_FINISHED) {
            val json = (it[0] as JSONObject).toString()
            println("RECEIVED GAME FINISHED")
            winnerHandler.sendMessage(winnerHandler.obtainMessage(200, Json.parse<Winner>(json)))
        }
    }

    fun sendPoint(action: DrawAction, id: Int, pointParams: PointParams, point: CanvasView.Point) {
        socket.emit(SocketEvents.DRAW, DrawPoint("DRAW", action.action, id, pointParams, point))
    }

    fun askClue() {
        socket.emit(SocketEvents.CLUE, 0)
    }

    fun guess(word: String) {
        socket.emit(SocketEvents.GUESS, word)
    }

    enum class DrawAction(val action: String) {
        ADD("ADD"),
        DEL("DEL")
    }

    @Suppress("unused", "EnumEntryName")
    enum class PointNature(val pointNature: String) {
        ellipse("ellipse"),
        rectangle("rectangle")
    }

    @Suppress("unused")
    enum class TimerDirection(val timerDirection: String) {
        INC("INC"),
        DEC("DEC")
    }

    enum class TournamentPlayerStatusType(val tournamentPlayerStatusType: String){
        WAITING("WAITING"),
        PLAYING("PLAYING"),
        WINNER("WINNER"),
        LOSER("LOSER")
    }
}
