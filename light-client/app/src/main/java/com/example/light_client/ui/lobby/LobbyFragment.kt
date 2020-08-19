package com.example.light_client.ui.lobby

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.*
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.light_client.R
import com.example.light_client.ui.core.CoreFragment
import android.text.InputFilter
import com.example.light_client.MainActivity
import com.example.light_client.src.services.*
import com.google.android.material.button.MaterialButton
import com.example.flatdialoglibrary.dialog.FlatDialog
import com.shashank.sony.fancygifdialoglib.FancyGifDialog
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.MainThread
import com.example.light_client.src.core.Constants
import com.squareup.picasso.Picasso


class LobbyFragment : CoreFragment<LobbyState>("lobby", LobbyState::class.java, R.layout.lobby_all_fragments) {
    private lateinit var lobbyViewModel: LobbyViewModel
    private lateinit var selectedGameMode: GameMode
    private lateinit var selectedDifficulty: GameDifficulty

    private var playerHasJoinedAGame = false


    override fun onCreateFragment() {
        lobbyViewModel = ViewModelProviders.of(this).get(LobbyViewModel::class.java)
        initializeDifficultySelection(root)
        initializeRecyclers()
        initializeClassicLobby()
        initializeSoloLobby()
        initializeCoopLobby()
        initializeTournamentLobby()
    }

    override fun refreshView() {
        if (!state.lobbyFragmentVisibility) {
            root.findViewById<LinearLayoutCompat>(R.id.fragment_lobby_parent).visibility = View.GONE
        } else {
            root.findViewById<LinearLayoutCompat>(R.id.fragment_lobby_parent).visibility = View.VISIBLE
        }

        if (!state.groupsFragmentVisibility) {
            root.findViewById<ConstraintLayout>(R.id.fragment_groups_parent).visibility = View.GONE
        } else {
            root.findViewById<ConstraintLayout>(R.id.fragment_groups_parent).visibility = View.VISIBLE
        }

        if (!state.classicModeLobbyFragmentVisibility) {
            root.findViewById<LinearLayout>(R.id.fragment_classic_mode_lobby_parent).visibility = View.GONE
        } else {
            root.findViewById<LinearLayout>(R.id.fragment_classic_mode_lobby_parent).visibility = View.VISIBLE
        }

        if (!state.soloModeLobbyFragmentVisibility) {
            root.findViewById<LinearLayout>(R.id.fragment_solo_mode_lobby_parent).visibility = View.GONE
        } else {
            root.findViewById<LinearLayout>(R.id.fragment_solo_mode_lobby_parent).visibility = View.VISIBLE
        }

        if (!state.coopModeLobbyFragmentVisibility) {
            root.findViewById<LinearLayout>(R.id.fragment_coop_mode_lobby_parent).visibility = View.GONE
        } else {
            root.findViewById<LinearLayout>(R.id.fragment_coop_mode_lobby_parent).visibility = View.VISIBLE
        }

        if (!state.tournamentModeLobbyFragmentVisibility) {
            root.findViewById<LinearLayout>(R.id.fragment_tournament_mode_lobby_parent).visibility = View.GONE
        } else {
            root.findViewById<LinearLayout>(R.id.fragment_tournament_mode_lobby_parent).visibility = View.VISIBLE
        }
    }

    /* View for Game Mode and Difficulty Selection */

    private fun initializeDifficultySelection(root: View) {
        val clickListener = View.OnClickListener {root ->
            when (root.id) {
                R.id.mode_classique_button -> selectDifficulty("CLASSIC")
                R.id.mode_sprint_solo_button -> selectDifficulty("SOLO")
                R.id.mode_sprint_cooperatif_button -> selectDifficulty("COOP")
                R.id.mode_tournament_button -> selectDifficulty("TOURNAMENT")
            }
        }
        with(lobbyViewModel) {
            selectClassicButton = root.findViewById(R.id.mode_classique_button)
            selectSprintSoloButton = root.findViewById(R.id.mode_sprint_solo_button)
            selectSprintCooperativeButton = root.findViewById(R.id.mode_sprint_cooperatif_button)
            selectTournamentButton = root.findViewById(R.id.mode_tournament_button)
            selectClassicButton.setOnClickListener(clickListener)
            selectSprintSoloButton.setOnClickListener(clickListener)
            selectSprintCooperativeButton.setOnClickListener(clickListener)
            selectTournamentButton.setOnClickListener(clickListener)
        }
    }

    @SuppressLint("DefaultLocale")
    private fun selectDifficulty(gameMode: String) {
        val flatDialog = FlatDialog(context)
        flatDialog.setTitle("Difficulté")
            .setSubtitle("Choisir la difficulté du mode")
            .setFirstButtonText("FACILE")
            .setSecondButtonText("INTERMÉDIAIRE")
            .setThirdButtonText("DIFFICILE")
            .withFirstButtonListner {
                selectedDifficulty = GameDifficulty.EASY
                switchToGroupView(selectedDifficulty, gameMode)
                flatDialog.dismiss()
            }
            .withSecondButtonListner {
                selectedDifficulty = GameDifficulty.INTERMEDIATE
                switchToGroupView(selectedDifficulty, gameMode)
                flatDialog.dismiss()
            }
            .withThirdButtonListner {
                selectedDifficulty = GameDifficulty.HARD
                switchToGroupView(selectedDifficulty, gameMode)
                flatDialog.dismiss()
            }
            .setFirstButtonColor(Color.parseColor("#22A699"))
            .setSecondButtonColor(Color.parseColor("#E6C230"))
            .setThirdButtonColor(Color.parseColor("#DE8E2D"))
            .setBackgroundColor(Color.argb(255,41,73,127))
            .isCancelable(true)
            .show()
    }

    private fun switchToGroupView(selectedDifficulty: GameDifficulty, gameMode: String) {
        // Ask server for groups list
        selectedGameMode = GameMode.valueOf(gameMode.toUpperCase())

        with(lobbyViewModel) {
            when (selectedGameMode) {
                GameMode.CLASSIC -> {
                    groupsRecyclerView = root.findViewById<RecyclerView>(R.id.fragment_groups_recycler_view).apply {
                        layoutManager = groupsViewManager
                        adapter = state.classicGroupsAdapter
                    }
                    state.classicGroupsAdapter.updateClassicList(null)
                    (activity as MainActivity).currentGameMode = GameMode.CLASSIC
                }
                GameMode.SOLO -> {
                    groupsRecyclerView = root.findViewById<RecyclerView>(R.id.fragment_groups_recycler_view).apply {
                        layoutManager = groupsViewManager
                        adapter = state.soloGroupsAdapter
                    }
                    state.soloGroupsAdapter.updateSoloList(null)
                    (activity as MainActivity).currentGameMode = GameMode.COOP
                }
                GameMode.COOP -> {
                    groupsRecyclerView = root.findViewById<RecyclerView>(R.id.fragment_groups_recycler_view).apply {
                        layoutManager = groupsViewManager
                        adapter = state.coopGroupsAdapter
                    }
                    state.coopGroupsAdapter.updateCoopList(null)
                    (activity as MainActivity).currentGameMode = GameMode.COOP
                }
                GameMode.TOURNAMENT -> {
                    groupsRecyclerView = root.findViewById<RecyclerView>(R.id.fragment_groups_recycler_view).apply {
                        layoutManager = groupsViewManager
                        adapter = state.tournamentGroupsAdapter
                    }
                    state.tournamentGroupsAdapter.updateTournamentList(null)
                    (activity as MainActivity).currentGameMode = GameMode.TOURNAMENT
                }
            }
        }
        state.lobbyService.listGroups(selectedGameMode, selectedDifficulty)

        state.lobbyFragmentVisibility = false
        state.groupsFragmentVisibility = true
        state.classicModeLobbyFragmentVisibility = false
        state.soloModeLobbyFragmentVisibility = false
        state.coopModeLobbyFragmentVisibility = false
        state.tournamentModeLobbyFragmentVisibility = false
        refreshView()
    }

    /* View for Groups available List or Create Group */

    private fun initializeRecyclers() {
        lobbyViewModel.groupsViewManager = LinearLayoutManager(activity)
        state.classicGroupsAdapter.root = this@LobbyFragment
        state.soloGroupsAdapter.root = this@LobbyFragment
        state.coopGroupsAdapter.root = this@LobbyFragment
        state.tournamentGroupsAdapter.root = this@LobbyFragment

        // for Back button on groups recycler view layout
        lobbyViewModel.groupsBackButton = root.findViewById(R.id.back_group_button)
        lobbyViewModel.groupsBackButton.setOnClickListener {
            state.lobbyFragmentVisibility = true
            state.groupsFragmentVisibility = false
            state.classicModeLobbyFragmentVisibility = false
            state.soloModeLobbyFragmentVisibility = false
            state.coopModeLobbyFragmentVisibility = false
            state.tournamentModeLobbyFragmentVisibility = false
            (activity as MainActivity).currentGameMode = GameMode.OTHER
            refreshView()
        }

        // for Create Group button (CardView)
        lobbyViewModel.createGroupButton = root.findViewById(R.id.fragment_create_group)
        lobbyViewModel.createGroupButton.setOnClickListener {

            val input = EditText(activity!!)
            input.maxLines = 1
            input.hint = "Entrer le nom du nouveau groupe"
            input.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(30))

            val alert = AlertDialog.Builder(activity!!)
                .setTitle("Nouveau groupe de jeu")
                .setView(input)
                .setPositiveButton(
                    R.string.creer_groupe
                ) { _, _ ->

                    // Ask server to create group
                    state.lobbyService.newGroup(selectedGameMode, selectedDifficulty, input.text.toString()
                    )
                }
                .setNegativeButton(R.string.annuler, null)

            val container = FrameLayout(activity!!)
            val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            val margin = resources.getDimensionPixelSize(R.dimen.dialog_margin)
            params.setMargins(margin, margin, margin, margin)
            input.layoutParams = params
            container.addView(input)
            alert.setView(container)
            alert.show()
        }

        lobbyViewModel.createGroupImageView = root.findViewById(R.id.fragment_create_group_image_view)
        Glide.with(activity!!).load(R.drawable.gif_draw).into(lobbyViewModel.createGroupImageView)
    }

    /* Views for Game Modes lobby */

    private fun initializeReadyButtonClassic(readyButton: MaterialButton, playerPosition: PlayerPosition, group: LobbyService.ClassicGroup) {
        if (group.A.A != null && group.A.B != null && group.B.A != null && group.B.B != null) {
            readyButton.setOnClickListener {playerIsReady(playerPosition)}
            readyButton.visibility = View.VISIBLE
        }
    }

    private fun initializeReadyButtonSolo(readyButton: MaterialButton, playerPosition: PlayerPosition, group: LobbyService.SoloGroup) {
        if (group.A.A != null) {
            readyButton.setOnClickListener {playerIsReady(playerPosition)}
            readyButton.visibility = View.VISIBLE
        }
    }

    private fun initializeReadyButtonCoop(readyButton: MaterialButton, playerPosition: PlayerPosition, group: LobbyService.CoopGroup) {
        var playersThere = 0
        if (group.A.A != null) playersThere++
        if (group.A.B != null) playersThere++
        if (group.A.C != null) playersThere++
        if (group.A.D != null) playersThere++
        if (playersThere >= 2) {
            readyButton.setOnClickListener {playerIsReady(playerPosition)}
            readyButton.visibility = View.VISIBLE
        }
    }

    private fun checkIfCurrentUserForThisPosition(player: LobbyService.GroupPlayer?, readyButton: MaterialButton, playerPosition: PlayerPosition): Boolean {
        val currentUser = application.user.username
        if (player != null) {
            if (player.user.username == currentUser) {
                when (state.lobbyService.currentGroupModeList) {
                    GameMode.CLASSIC -> initializeReadyButtonClassic(readyButton, playerPosition, state.currentClassicGroup)
                    GameMode.SOLO -> initializeReadyButtonSolo(readyButton, playerPosition, state.currentSoloGroup)
                    GameMode.COOP -> initializeReadyButtonCoop(readyButton, playerPosition, state.currentCoopGroup)
                }
                return true
            }
        }
        return false
    }

    private fun playerExistsClassic(group: LobbyService.ClassicGroup): Boolean {
        with (lobbyViewModel) {
            // check for each button position if player was added
            if (checkIfCurrentUserForThisPosition(group.A.A, readyButtonClassic, PlayerPosition.AA)) return true
            if (checkIfCurrentUserForThisPosition(group.A.B, readyButtonClassic, PlayerPosition.AB)) return true
            if (checkIfCurrentUserForThisPosition(group.B.A, readyButtonClassic, PlayerPosition.BA)) return true
            if (checkIfCurrentUserForThisPosition(group.B.B, readyButtonClassic, PlayerPosition.BB)) return true
            return false
        }
    }

    private fun playerExistsSolo(group: LobbyService.SoloGroup): Boolean {
        with (lobbyViewModel) {
            // check for each button position if player was added
            if (checkIfCurrentUserForThisPosition(group.A.A, readyButtonSolo, PlayerPosition.AA)) return true
            return false
        }
    }

    private fun playerExistsCoop(group: LobbyService.CoopGroup): Boolean {
        with (lobbyViewModel) {
            // check for each button position if player was added
            if (checkIfCurrentUserForThisPosition(group.A.A, readyButtonCoop, PlayerPosition.AA)) return true
            if (checkIfCurrentUserForThisPosition(group.A.B, readyButtonCoop, PlayerPosition.AB)) return true
            if (checkIfCurrentUserForThisPosition(group.A.C, readyButtonCoop, PlayerPosition.AC)) return true
            if (checkIfCurrentUserForThisPosition(group.A.D, readyButtonCoop, PlayerPosition.AD)) return true
            return false
        }
    }

    private fun loadPublicAvatarAsync(playerAvatarId: String?, playerButton: MaterialButton) {
        Picasso.get().load(Constants.BASE_URL+"/image/"+playerAvatarId).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                var profilePicture = BitmapFactory.decodeResource(resources, R.drawable.profile_no_image_available)
                if (bitmap != null) profilePicture = bitmap
                // transform square picture to circle shape
                val croppedProfilePicture = BitmapDrawable(resources, convertBitmapRectangleToCircle(profilePicture))
                playerButton.icon = croppedProfilePicture
            }
            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                val profilePicture = BitmapFactory.decodeResource(resources, R.drawable.profile_no_image_available)
                playerButton.icon = BitmapDrawable(resources, convertBitmapRectangleToCircle(profilePicture))
            }
        })
    }

    private fun updateButtonStatusPlayer(player: LobbyService.GroupPlayer?, playerButton: MaterialButton) {
        if (player != null) {
            if (player.user.username != null) {
                val username = player.user.username
                playerButton.text = username
                if (player.ready) {
                    val ready = "$username [PRET]"
                    playerButton.text = ready
                }
                loadPublicAvatarAsync(player.user.avatar, playerButton)
            }
            else {
                val joueurVirtuelText = "Joueur Virtuel [PRET]"
                playerButton.text = joueurVirtuelText
                // transform square picture to circle shape
                val profilePicture = BitmapFactory.decodeResource(resources, R.drawable.profile_virtual_player)
                val croppedProfilePicture = BitmapDrawable(resources, convertBitmapRectangleToCircle(profilePicture))
                playerButton.icon = croppedProfilePicture
            }
            playerButton.setOnClickListener(null)
            playerButton.isEnabled = false
            playerButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E8E8B5"))
        }
        else playerButton.text = ""
    }

    @Suppress("DEPRECATION")
    private fun updateButtonStatusAddPlayer(playerButton: MaterialButton, playerPos: PlayerPosition, playerType: PlayerType?, text: String, groupId: String) {
        if (playerButton.text.isEmpty()) {
            playerButton.text = text
            if (playerType != null) {
                // Can't add Virtual Player to Guess role
                if (playerType == PlayerType.VIRTUAL && (playerPos == PlayerPosition.AA || playerPos == PlayerPosition.BA)) {
                    playerButton.isEnabled = false
                }
                else if (playerType == PlayerType.REAL || playerType == PlayerType.VIRTUAL) {
                    playerButton.setOnClickListener{addPlayerToGroup(playerPos, playerType, groupId)}
                    playerButton.isEnabled = true
                }
            }
            else {
                playerButton.isEnabled = false
            }
            playerButton.icon = null
            playerButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
        }
    }

    fun updateClassicLobby(group: LobbyService.ClassicGroup) {
        with (lobbyViewModel) {
            userAA = root.findViewById(R.id.button_aa)
            userAB = root.findViewById(R.id.button_ab)
            userBA = root.findViewById(R.id.button_ba)
            userBB = root.findViewById(R.id.button_bb)

            checkIfEveryoneIsReadyClassic(group)

            updateButtonStatusPlayer(group.A.A, userAA)
            updateButtonStatusPlayer(group.A.B, userAB)
            updateButtonStatusPlayer(group.B.A, userBA)
            updateButtonStatusPlayer(group.B.B, userBB)

            if (playerExistsClassic(group)) {
                updateButtonStatusAddPlayer(userAA, PlayerPosition.AA, PlayerType.VIRTUAL, "AJOUTER JOUEUR VIRTUEL", group.id)
                updateButtonStatusAddPlayer(userAB, PlayerPosition.AB, PlayerType.VIRTUAL, "AJOUTER JOUEUR VIRTUEL", group.id)
                updateButtonStatusAddPlayer(userBA, PlayerPosition.BA, PlayerType.VIRTUAL, "AJOUTER JOUEUR VIRTUEL", group.id)
                updateButtonStatusAddPlayer(userBB, PlayerPosition.BB, PlayerType.VIRTUAL, "AJOUTER JOUEUR VIRTUEL", group.id)
            }
            else {
                updateButtonStatusAddPlayer(userAA, PlayerPosition.AA, PlayerType.REAL, "JOINDRE CETTE POSITION", group.id)
                updateButtonStatusAddPlayer(userAB, PlayerPosition.AB, PlayerType.REAL, "JOINDRE CETTE POSITION", group.id)
                updateButtonStatusAddPlayer(userBA, PlayerPosition.BA, PlayerType.REAL, "JOINDRE CETTE POSITION", group.id)
                updateButtonStatusAddPlayer(userBB, PlayerPosition.BB, PlayerType.REAL, "JOINDRE CETTE POSITION", group.id)
            }
        }
    }

    fun updateSoloLobby(group: LobbyService.SoloGroup) {
        with (lobbyViewModel) {
            virtualPlayerButtonSolo = root.findViewById(R.id.button_virtual_player_solo)
            updateButtonStatusPlayer(LobbyService.GroupPlayer("", LobbyService.User(null, null), "", true), virtualPlayerButtonSolo)
            playerOneSolo = root.findViewById(R.id.button_aa_solo)

            checkIfEveryoneIsReadySolo(group)

            updateButtonStatusPlayer(group.A.A, playerOneSolo)

            if (playerExistsSolo(group)) {
                updateButtonStatusAddPlayer(playerOneSolo, PlayerPosition.AA, null, "PLACE DISPONIBLE", group.id)
            }
            else {
                updateButtonStatusAddPlayer(playerOneSolo, PlayerPosition.AA, PlayerType.REAL, "JOINDRE CETTE POSITION", group.id)
            }
        }
    }

    fun updateCoopLobby(group: LobbyService.CoopGroup) {
        with (lobbyViewModel) {
            virtualPlayerButtonCoop = root.findViewById(R.id.button_virtual_player_coop)
            updateButtonStatusPlayer(LobbyService.GroupPlayer("", LobbyService.User(null, null), "", true), virtualPlayerButtonCoop)
            playerOneCoop = root.findViewById(R.id.button_aa_coop)
            playerTwoCoop = root.findViewById(R.id.button_ab_coop)
            playerThreeCoop = root.findViewById(R.id.button_ac_coop)
            playerFourCoop = root.findViewById(R.id.button_ad_coop)

            checkIfEveryoneIsReadyCoop(group)

            updateButtonStatusPlayer(group.A.A, playerOneCoop)
            updateButtonStatusPlayer(group.A.B, playerTwoCoop)
            updateButtonStatusPlayer(group.A.C, playerThreeCoop)
            updateButtonStatusPlayer(group.A.D, playerFourCoop)

            if (playerExistsCoop(group)) {
                updateButtonStatusAddPlayer(playerOneCoop, PlayerPosition.AA, null, "PLACE DISPONIBLE", group.id)
                updateButtonStatusAddPlayer(playerTwoCoop, PlayerPosition.AB, null, "PLACE DISPONIBLE", group.id)
                updateButtonStatusAddPlayer(playerThreeCoop, PlayerPosition.AC, null, "PLACE DISPONIBLE", group.id)
                updateButtonStatusAddPlayer(playerFourCoop, PlayerPosition.AD, null, "PLACE DISPONIBLE", group.id)
            }
            else {
                updateButtonStatusAddPlayer(playerOneCoop, PlayerPosition.AA, PlayerType.REAL, "JOINDRE CETTE POSITION", group.id)
                updateButtonStatusAddPlayer(playerTwoCoop, PlayerPosition.AB, PlayerType.REAL, "JOINDRE CETTE POSITION", group.id)
                updateButtonStatusAddPlayer(playerThreeCoop, PlayerPosition.AC, PlayerType.REAL, "JOINDRE CETTE POSITION", group.id)
                updateButtonStatusAddPlayer(playerFourCoop, PlayerPosition.AD, PlayerType.REAL, "JOINDRE CETTE POSITION", group.id)
            }
        }
    }

    fun updateTournamentLobby(group: LobbyService.TournamentGroup) {
        with (lobbyViewModel) {
            virtualPlayerButtonTournament = root.findViewById(R.id.button_virtual_player_tournament)
            updateButtonStatusPlayer(LobbyService.GroupPlayer("", LobbyService.User(null, null), "", true), virtualPlayerButtonTournament)
            playerTournament = root.findViewById(R.id.button_player_tournament)
            playerTournament.setOnClickListener{addPlayerToGroup(PlayerPosition.AA, PlayerType.REAL, group.id)}
            playerTournament.isEnabled = true
            playerTournament.icon = null
            playerTournament.text = "JOINDRE LE TOURNOI"
            playerTournament.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
            for (playerButton in playersTournament) {
                playerButton!!.icon = null
                playerButton.text = "place disponible"
                playerButton.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.colorAccent))
            }
            readyButtonTournament.visibility = View.GONE

            val currentUser = application.user.username
            // check if player ready
            if (group.players.isNotEmpty()) {
                for (player in group.players) {
                    if (player.user.username == currentUser) {
                        playerTournament.text = player.user.username
                        playerTournament.setOnClickListener(null)
                        playerTournament.isEnabled = false
                        playerTournament.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E8E8B5"))
                        readyButtonTournament.setOnClickListener {playerIsReady(PlayerPosition.AA)}
                        readyButtonTournament.visibility = View.VISIBLE

                        if (player.ready) {
                            val ready = "$currentUser [PRET]"
                            playerTournament.text = ready
                        }

                        loadPublicAvatarAsync(player.user.avatar, playerTournament)
                    }
                    else {
                        val username = player.user.username
                        for (playerButton in playersTournament) {
                            if ((playerButton != null) && (playerButton.text == username) && (player.ready) && (playerButton.text != "$username [PRET]")) {
                                val ready = "$username [PRET]"
                                playerButton.text = ready
                                break
                            }
                            else if (playerButton != null && (player.ready) && playerButton.text == "$username [PRET]") break
                            else if (playerButton != null && playerButton.text == username) break
                            else if (playerButton != null && playerButton.text == "place disponible") {
                                playerButton.text = player.user.username
                                playerButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#E8E8B5"))

                                loadPublicAvatarAsync(player.user.avatar, playerButton)
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    private fun convertBitmapRectangleToCircle(bitmap: Bitmap): Bitmap {
        val circleOutput = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(circleOutput)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2, bitmap.width.toFloat() / 2, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return circleOutput
    }

    private fun addPlayerToGroup(playerPosition: PlayerPosition, playerType: PlayerType, groupId: String) {
        playerHasJoinedAGame = true
        (activity as MainActivity).groupIdLobbyJoined = groupId
        (activity as MainActivity).deactivateTutorialButton()
        val avatarId = (activity as MainActivity).userAvatarId
        when (state.lobbyService.currentGroupModeList) {
            GameMode.CLASSIC -> state.lobbyService.addPlayer(state.currentClassicGroup.id, playerPosition, application.user.username, playerType, avatarId)
            GameMode.SOLO -> state.lobbyService.addPlayer(state.currentSoloGroup.id, playerPosition, application.user.username, playerType, avatarId)
            GameMode.COOP -> state.lobbyService.addPlayer(state.currentCoopGroup.id, playerPosition, application.user.username, playerType, avatarId)
            GameMode.TOURNAMENT -> state.lobbyService.addPlayer(state.currentTournamentGroup.id, playerPosition, application.user.username, playerType, avatarId)
        }
    }

    private fun playerIsReady(playerPosition: PlayerPosition) {
       when (state.lobbyService.currentGroupModeList) {
           GameMode.CLASSIC -> state.lobbyService.playerReady(state.currentClassicGroup.id, playerPosition)
           GameMode.SOLO -> state.lobbyService.playerReady(state.currentSoloGroup.id, playerPosition)
           GameMode.COOP -> state.lobbyService.playerReady(state.currentCoopGroup.id, playerPosition)
           GameMode.TOURNAMENT -> state.lobbyService.playerReady(state.currentTournamentGroup.id, playerPosition)
        }
        lobbyViewModel.readyButtonClassic.isEnabled = false
        lobbyViewModel.readyButtonSolo.isEnabled = false
        lobbyViewModel.readyButtonCoop.isEnabled = false
        lobbyViewModel.readyButtonTournament.isEnabled = false
    }

    private fun checkIfEveryoneIsReadyClassic(group: LobbyService.ClassicGroup) {
        if (group.A.A != null && group.A.B != null && group.B.A != null && group.B.B != null) {
            if (group.A.A!!.ready && group.A.B!!.ready && group.B.A!!.ready && group.B.B!!.ready) {
                if (group.id == (activity as MainActivity).groupIdLobbyJoined){
                    (activity as MainActivity).switchLobbyToClassic()
                }
            }
        }
    }

    private fun checkIfEveryoneIsReadySolo(group: LobbyService.SoloGroup) {
        if (group.A.A != null && group.A.A!!.ready) {
            if (group.id == (activity as MainActivity).groupIdLobbyJoined) {
                (activity as MainActivity).switchLobbyToCoop()
            }
        }
    }

    private fun checkIfEveryoneIsReadyCoop(group: LobbyService.CoopGroup) {
        var ready = 0
        var playersThere = 0
        if (group.A.A != null) { playersThere++
            if (group.A.A!!.ready) ready++
        }
        if (group.A.B != null) { playersThere++
            if (group.A.B!!.ready) ready++
        }
        if (group.A.C != null) { playersThere++
            if (group.A.C!!.ready) ready++
        }
        if (group.A.D != null) { playersThere++
            if (group.A.D!!.ready) ready++
        }
        if (group.id == (activity as MainActivity).groupIdLobbyJoined) {
            if (ready >= 2 && playersThere == ready) (activity as MainActivity).switchLobbyToCoop()
        }
    }

    private fun initializeClassicLobby(){
        // for Back button in Classic Mode Lobby
        lobbyViewModel.lobbyBackButtonClassic = root.findViewById(R.id.back_lobby_button)
        lobbyViewModel.lobbyBackButtonClassic.setOnClickListener {
            if (playerHasJoinedAGame) { showExitDialogAfterJoiningGame() }
            else { showGroupsSelectionFragment(); refreshView() }
        }

        lobbyViewModel.readyButtonClassic = root.findViewById(R.id.ready_button)
        lobbyViewModel.readyButtonClassic.visibility = View.GONE
    }

    private fun initializeSoloLobby() {
        // for Back button in Classic Mode Lobby
        lobbyViewModel.lobbyBackButtonSolo = root.findViewById(R.id.back_lobby_button_solo)
        lobbyViewModel.lobbyBackButtonSolo.setOnClickListener {
            if (playerHasJoinedAGame) { showExitDialogAfterJoiningGame() }
            else { showGroupsSelectionFragment(); refreshView() }
        }

        lobbyViewModel.readyButtonSolo = root.findViewById(R.id.ready_button_solo)
        lobbyViewModel.readyButtonSolo.visibility = View.GONE
    }

    private fun initializeCoopLobby() {
        // for Back button in Classic Mode Lobby
        lobbyViewModel.lobbyBackButtonCoop = root.findViewById(R.id.back_lobby_button_coop)
        lobbyViewModel.lobbyBackButtonCoop.setOnClickListener {
            if (playerHasJoinedAGame) { showExitDialogAfterJoiningGame() }
            else { showGroupsSelectionFragment(); refreshView() }
        }

        lobbyViewModel.readyButtonCoop = root.findViewById(R.id.ready_button_coop)
        lobbyViewModel.readyButtonCoop.visibility = View.GONE
    }

    private fun initializeTournamentLobby() {
        // for Back button in Classic Mode Lobby
        lobbyViewModel.lobbyBackButtonTournament = root.findViewById(R.id.back_lobby_button_tournament)
        lobbyViewModel.lobbyBackButtonTournament.setOnClickListener {
            if (playerHasJoinedAGame) { showExitDialogAfterJoiningGame() }
            else { showGroupsSelectionFragment(); refreshView() }
        }

        lobbyViewModel.playersTournament[0] = root.findViewById(R.id.button_other_player_01_tournament)
        lobbyViewModel.playersTournament[1] = root.findViewById(R.id.button_other_player_02_tournament)
        lobbyViewModel.playersTournament[2] = root.findViewById(R.id.button_other_player_03_tournament)
        lobbyViewModel.playersTournament[3] = root.findViewById(R.id.button_other_player_04_tournament)
        lobbyViewModel.playersTournament[4] = root.findViewById(R.id.button_other_player_05_tournament)
        lobbyViewModel.playersTournament[5] = root.findViewById(R.id.button_other_player_06_tournament)
        lobbyViewModel.playersTournament[6] = root.findViewById(R.id.button_other_player_07_tournament)
        lobbyViewModel.playersTournament[7] = root.findViewById(R.id.button_other_player_08_tournament)
        lobbyViewModel.playersTournament[8] = root.findViewById(R.id.button_other_player_09_tournament)
        lobbyViewModel.playersTournament[9] = root.findViewById(R.id.button_other_player_10_tournament)
        lobbyViewModel.playersTournament[10] = root.findViewById(R.id.button_other_player_11_tournament)
        lobbyViewModel.playersTournament[11] = root.findViewById(R.id.button_other_player_12_tournament)
        lobbyViewModel.playersTournament[12] = root.findViewById(R.id.button_other_player_13_tournament)
        lobbyViewModel.playersTournament[13] = root.findViewById(R.id.button_other_player_14_tournament)
        lobbyViewModel.playersTournament[14] = root.findViewById(R.id.button_other_player_15_tournament)

        lobbyViewModel.readyButtonTournament = root.findViewById(R.id.ready_button_tournament)
        lobbyViewModel.readyButtonTournament.visibility = View.GONE
    }

    private fun showGroupsSelectionFragment() {
        state.lobbyFragmentVisibility = false
        state.groupsFragmentVisibility = true
        state.classicModeLobbyFragmentVisibility = false
        state.soloModeLobbyFragmentVisibility = false
        state.coopModeLobbyFragmentVisibility = false
        state.tournamentModeLobbyFragmentVisibility = false
    }

    private fun showExitDialogAfterJoiningGame() {
        FancyGifDialog.Builder(activity)
            .setTitle("Quitter la partie?")
            .setMessage("Êtes-vous sûr de vouloir quitter la partie en cours?")
            .setPositiveBtnText("Oui")
            .setNegativeBtnText("Non")
            .setGifResource(R.drawable.gif_homer_lurking)
            .isCancellable(true)
            .OnPositiveClicked {
                state.lobbyService.leaveGroup()
                (activity as MainActivity).backToLobby()
            }
            .build()
    }
}
