package com.example.light_client.ui.profile

import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import belka.us.androidtoggleswitch.widgets.ToggleSwitch
import com.google.android.material.button.MaterialButton
import de.codecrafters.tableview.TableView
import de.hdodenhof.circleimageview.CircleImageView


class ProfileViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is profile Fragment"
    }
    val text: LiveData<String> = _text


    lateinit var logoutButton: MaterialButton
    lateinit var activateTutorial: MaterialButton
    lateinit var deleteProfile: MaterialButton
    lateinit var profilePicture: CircleImageView
    lateinit var usernameProfile: TextView
    lateinit var nameLastnameProfile: TextView
    lateinit var editNameLastnameProfile: ImageView
    lateinit var gamesPlayedProfile: TextView
    lateinit var victoryPercentageProfile: TextView
    lateinit var averageTimePlayedProfile: TextView
    lateinit var totalTimePayedProfile: TextView
    lateinit var bestScoreSoloProfile: TextView
    lateinit var bestScoreCoopProfile: TextView
    lateinit var tournamentsWonProfile: TextView
    lateinit var togglerChangeTable: ToggleSwitch


    lateinit var loginLogoutStatsTable: TableView<Array<String>>
    lateinit var gameStatsHistoryTable: TableView<Array<String>>

    lateinit var backButtonOnAvatarPickerPage : MaterialButton
    lateinit var avatarPickerRecyclerView: RecyclerView
}