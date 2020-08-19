package com.example.light_client.src.core

enum class SocketEvents(val ev: String) {
    NEW_MESSAGE("new message"),
    INIT_CHAT("init chat"),
    ENTER_CHANNEL("enter channel"),
    LIST_GROUPS("list groups"),
    NEW_GROUP("new group"),
    ADD_PLAYER("add player"),
    PLAYER_READY("player ready"),
    STATE_UPDATE("state update"),
    CLUE("clue"),
    GUESS("guess"),
    DRAW("draw"),
    GAME_FINISHED("game finished"),
    ROUND_FINISHED("round finished"),
    TOURNAMENT_STATE("tournament state"),
    LEAVE_GROUP("leave group")
}