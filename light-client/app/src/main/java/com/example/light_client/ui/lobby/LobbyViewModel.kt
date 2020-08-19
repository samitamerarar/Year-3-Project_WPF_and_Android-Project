package com.example.light_client.ui.lobby

import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class LobbyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is lobby Fragment"
    }
    val text: LiveData<String> = _text

    lateinit var selectClassicButton: MaterialButton
    lateinit var selectSprintSoloButton: MaterialButton
    lateinit var selectSprintCooperativeButton: MaterialButton
    lateinit var selectTournamentButton: MaterialButton

    lateinit var groupsRecyclerView: RecyclerView
    lateinit var groupsViewManager: RecyclerView.LayoutManager
    lateinit var groupsBackButton: MaterialButton
    lateinit var createGroupButton: CardView
    lateinit var createGroupImageView: ImageView

    lateinit var lobbyBackButtonClassic: MaterialButton
    lateinit var readyButtonClassic: MaterialButton
    lateinit var userAA: MaterialButton
    lateinit var userAB: MaterialButton
    lateinit var userBA: MaterialButton
    lateinit var userBB: MaterialButton

    lateinit var lobbyBackButtonSolo: MaterialButton
    lateinit var readyButtonSolo: MaterialButton
    lateinit var virtualPlayerButtonSolo: MaterialButton
    lateinit var playerOneSolo: MaterialButton

    lateinit var lobbyBackButtonCoop: MaterialButton
    lateinit var readyButtonCoop: MaterialButton
    lateinit var virtualPlayerButtonCoop: MaterialButton
    lateinit var playerOneCoop: MaterialButton
    lateinit var playerTwoCoop: MaterialButton
    lateinit var playerThreeCoop: MaterialButton
    lateinit var playerFourCoop: MaterialButton

    lateinit var lobbyBackButtonTournament: MaterialButton
    lateinit var readyButtonTournament: MaterialButton
    lateinit var virtualPlayerButtonTournament: MaterialButton
    lateinit var playerTournament: MaterialButton
    var playersTournament = arrayOfNulls<MaterialButton>(15)
}
