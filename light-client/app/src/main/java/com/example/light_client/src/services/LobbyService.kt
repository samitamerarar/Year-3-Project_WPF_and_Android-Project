package com.example.light_client.src.services

import android.os.Handler
import com.example.light_client.src.core.IOSocket
import com.example.light_client.src.core.Json
import com.example.light_client.src.core.SocketEvents
import org.json.JSONArray
import java.net.Socket


class LobbyService(private val socket: IOSocket, private val handler: Handler) {

    lateinit var currentGroupModeList: GameMode

    data class User(var username: String?, var avatar: String?)
    data class GroupPlayer(var type: String, var user: User, var role: String, var ready: Boolean)
    data class ClassicGroupTeam(var role: String, var A: GroupPlayer?, var B: GroupPlayer?, var points: Int)
    data class ClassicGroup(override var id: String, override var name: String, override var difficulty: String,
                            var A: ClassicGroupTeam, var B: ClassicGroupTeam, override var mode: String): BaseGroup()

    data class CoopGroupTeam(var points: Int, var A: GroupPlayer?, var B: GroupPlayer?, var C: GroupPlayer?, var D: GroupPlayer?)
    data class CoopGroup(override var mode: String, override var id: String, override var name: String,
                         override var difficulty: String, var A: CoopGroupTeam): BaseGroup()
    data class SoloGroup(override var mode: String, override var id: String, override var name: String,
                         override var difficulty: String, var A: CoopGroupTeam): BaseGroup()
    data class TournamentGroup(override var mode: String, var players: Array<GroupPlayer>, override var id: String,
                               override var name: String, override var difficulty: String): BaseGroup()

    abstract class BaseGroup {
        abstract var id: String
        abstract var name: String
        abstract var difficulty: String
        abstract var mode: String
    }

    data class GroupForm(var mode: String, var difficulty: String, var name: String?)
    data class AddPlayerForm(var gid: String, var pos: String, var type: String, var username: String, var avatar: String?)
    data class PlayerReadyForm(var gid: String, var pos: String)

    init {
        socket.on(SocketEvents.LIST_GROUPS) {
            val json = (it[0] as JSONArray).toString()
            when {
                json.contains("\"mode\":\"CLASSIC\"") -> {
                    currentGroupModeList = GameMode.CLASSIC
                    handler.sendMessage(handler.obtainMessage(200, Json.parse<Array<ClassicGroup>>(json)))
                }
                json.contains("\"mode\":\"SOLO\"") -> {
                    println(json)
                    currentGroupModeList = GameMode.SOLO
                    handler.sendMessage(handler.obtainMessage(200, Json.parse<Array<SoloGroup>>(json)))
                }
                json.contains("\"mode\":\"COOP\"") -> {
                    currentGroupModeList = GameMode.COOP
                    handler.sendMessage(handler.obtainMessage(200, Json.parse<Array<CoopGroup>>(json)))
                }
                json.contains("\"mode\":\"TOURNAMENT\"") -> {
                    currentGroupModeList = GameMode.TOURNAMENT
                    handler.sendMessage(handler.obtainMessage(200, Json.parse<Array<TournamentGroup>>(json)))
                }
            }
        }
    }

    fun listGroups(mode: GameMode, difficulty: GameDifficulty) {
        socket.emit(SocketEvents.LIST_GROUPS, GroupForm(mode.mode, difficulty.difficulty, null))
    }

    fun newGroup(mode: GameMode, difficulty: GameDifficulty, groupName: String) {
        socket.emit(SocketEvents.NEW_GROUP, GroupForm(mode.mode, difficulty.difficulty, groupName))
    }

    fun addPlayer(gid: String, pos: PlayerPosition, username: String, type: PlayerType, avatar: String?) {
        socket.emit(SocketEvents.ADD_PLAYER, AddPlayerForm(gid, pos.pos, type.type, username, avatar))
    }

    fun playerReady(gid: String, pos: PlayerPosition) {
        socket.emit(SocketEvents.PLAYER_READY, PlayerReadyForm(gid, pos.pos))
    }

    fun leaveGroup() {
        socket.emit(SocketEvents.LEAVE_GROUP, null)
    }
}

enum class GameMode(val mode: String) {
    CLASSIC("CLASSIC"),
    COOP("COOP"),
    SOLO("SOLO"),
    TOURNAMENT("TOURNAMENT"),
    TOURNAMENT_ROUND("TOURNAMENT_ROUND"),
    OTHER("OTHER")
}

enum class GameDifficulty(val difficulty: String) {
    EASY("EASY"),
    INTERMEDIATE("INTERMEDIATE"),
    HARD("HARD")
}

enum class PlayerRole(val type: String) {
    WRITE("WRITE"),
    GUESS("GUESS"),
    PASSIVE("PASSIVE")
}

enum class PlayerType(val type: String) {
    REAL("REAL"),
    VIRTUAL("VIRTUAL")
}

enum class PlayerPosition(val pos: String) {
    AA("A.A"),
    AB("A.B"),
    BA("B.A"),
    BB("B.B"),
    // for coop
    AC("A.C"),
    AD("A.D")
}