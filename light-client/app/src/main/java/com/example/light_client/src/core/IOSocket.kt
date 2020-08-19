package com.example.light_client.src.core
import android.os.Handler
import android.view.Gravity
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.sdsmdg.tastytoast.TastyToast
import java.net.URISyntaxException

class IOSocket {
    private lateinit var socket: Socket
    private val events: ArrayList<SocketEvents> = arrayListOf()

    fun init(username: String) {
        try {
            val options = IO.Options()
            options.query = "username=$username"
            socket = IO.socket(Constants.BASE_URL, options)
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        socket.connect().on(Socket.EVENT_CONNECT) {
            socket.emit(SocketEvents.INIT_CHAT.ev, username)
            println(" Socket Connected ")
        }
    }

    fun disconnect() {
        socket.disconnect()
    }
    fun emit(event: SocketEvents, args: Any?) {
        var str = ""
        if (args != null) {
            str = if (args is String) args  else Json.toString(args)
        }
        socket.emit(event.ev, str)
    }

    fun on(event: SocketEvents, handler: (Array<Any>) -> Unit) {
        if (events.contains(event)) socket.off(event.ev)

        socket.on(event.ev) {
            handler(it)
        }

        events.add(event)
    }

    fun onDisconnect(handler: Handler) {
        socket.on(Socket.EVENT_DISCONNECT) {
            handler.sendMessage(handler.obtainMessage(200, null))
        }
    }
}