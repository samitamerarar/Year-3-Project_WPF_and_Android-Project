package com.example.light_client.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Base64
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.flatdialoglibrary.dialog.FlatDialog
import com.example.light_client.LoginActivity
import com.example.light_client.MainActivity
import com.example.light_client.R
import com.example.light_client.src.core.Constants
import com.example.light_client.src.services.AuthService
import com.example.light_client.src.services.GameMode
import com.example.light_client.src.services.ImageService
import com.example.light_client.ui.core.CoreFragment
import com.example.light_client.ui.game.AllGameModesState
import com.google.android.material.button.MaterialButton
import com.sdsmdg.tastytoast.TastyToast
import com.shashank.sony.fancygifdialoglib.FancyGifDialog
import com.squareup.picasso.Picasso
import de.codecrafters.tableview.TableView
import de.codecrafters.tableview.model.TableColumnDpWidthModel
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class ProfileFragment : CoreFragment<ProfileState>("profile", ProfileState::class.java, R.layout.profile_all_fragments) {
    lateinit var profileViewModel: ProfileViewModel
    var imageListBitmap = ArrayList<Bitmap>()
    private lateinit var allGameModesState: AllGameModesState

    private val disconnectHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            if (context != null) {
                TastyToast.makeText(context, "Vous avez été déconnecté du serveur.", Toast.LENGTH_LONG, TastyToast.WARNING)
                    .setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                logout(false)
            }
        }
    }
    override fun onCreateFragment() {
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        state.fragmentProfile = this
        state.profileFragmentVisibility = true
        state.avatarPickerFragmentVisibility = false
        initProfileView()
        initAvatarPickerView()
        refreshView()
        application.authService.getProfile(application.user.username, state.profileHandler)
        application.socket.onDisconnect(disconnectHandler)


        allGameModesState = application.stateManager.get("gamemodes", AllGameModesState::class.java)
    }

    fun applyUserProfile(userProfile: AuthService.UserProfile) {
        with(profileViewModel) {
            usernameProfile.text = application.user.username
            if (userProfile.avatar != null) { Picasso.get().load(Constants.BASE_URL+"/image/"+userProfile.avatar).into(profilePicture) }
            else { Picasso.get().load(R.drawable.profile_no_image_available).into(profilePicture) }
            if (userProfile.firstName != null && userProfile.lastName != null) {
                profileViewModel.nameLastnameProfile.text = getFirstAndLastName(userProfile.firstName!!, userProfile.lastName!!)
            }
            gamesPlayedProfile.text = ((userProfile.matchsPlayed).toInt()).toString()
            victoryPercentageProfile.text = "${(((userProfile.victoryPercent).toInt())*100)}%"
            averageTimePlayedProfile.text = getDurationInHoursMinutesSeconds(userProfile.averageMatchDuration.toLong())
            totalTimePayedProfile.text = getDurationInHoursMinutesSeconds(userProfile.totalMatchDuration.toLong())
            bestScoreSoloProfile.text = ((userProfile.soloHighestScore).toInt()).toString()
            bestScoreCoopProfile.text = ((userProfile.coopHighestScore).toInt()).toString()
            tournamentsWonProfile.text = ((userProfile.tournamentsWon).toInt().toString())
        }
        fillLoginLogoutStatsTable(userProfile)
        fillGameStatsHistoryTable(userProfile)
    }

    private fun getFirstAndLastName(firstname: String, lastname: String): String {
        return if ((firstname+lastname) == "") "(Prénom et Nom)" else "$firstname $lastname"
    }

    private fun initProfileView() {
        with(profileViewModel) {
            logoutButton = root.findViewById(R.id.logout_button) as MaterialButton
            logoutButton.setOnClickListener {
                showConfirmationDialogLogout()
            }
            activateTutorial = root.findViewById(R.id.activate_tutorial_button) as MaterialButton
            activateTutorial.setOnClickListener {
                showConfirmationDialogTutorial()
            }
            deleteProfile = root.findViewById(R.id.delete_profile_button) as MaterialButton
            deleteProfile.setOnClickListener {
                showConfirmationDialogDeleteUser()
            }
            profilePicture = root.findViewById(R.id.picture_profile) as CircleImageView
            profilePicture.setOnClickListener {
                openChooseAvatarDialog()
            }
            usernameProfile = root.findViewById(R.id.username_profile) as TextView
            nameLastnameProfile = root.findViewById(R.id.name_lastname_profile) as TextView
            editNameLastnameProfile = root.findViewById(R.id.edit_name_lastname_profile) as ImageView
            editNameLastnameProfile.setOnClickListener {
                openSetFirstNameAndLastNameDialog()
            }

            gamesPlayedProfile = root.findViewById(R.id.number_games_played_profile) as TextView
            victoryPercentageProfile = root.findViewById(R.id.victory_percentage_profile) as TextView
            averageTimePlayedProfile = root.findViewById(R.id.average_time_played_profile) as TextView
            totalTimePayedProfile = root.findViewById(R.id.total_time_played_profile) as TextView
            bestScoreSoloProfile = root.findViewById(R.id.best_score_solo_profile) as TextView
            bestScoreCoopProfile = root.findViewById(R.id.best_score_coop_profile) as TextView
            tournamentsWonProfile = root.findViewById(R.id.number_tournament_won) as TextView

            loginLogoutStatsTable = root.findViewById(R.id.login_logout_history_table) as TableView<Array<String>>
            loginLogoutStatsTable.columnCount = 2
            loginLogoutStatsTable.headerAdapter = SimpleTableHeaderAdapter(context, "Dates et heures de connexions", "Dates et heures de déconnexions")
            gameStatsHistoryTable = root.findViewById(R.id.history_games_stats_table) as TableView<Array<String>>
            gameStatsHistoryTable.columnCount = 6
            gameStatsHistoryTable.headerAdapter = SimpleTableHeaderAdapter(context, "Mode joué", "Début", "Fin", "Durée", "Résultats", "Participants")
            val columnModel = TableColumnDpWidthModel(context, 6, 200)
            columnModel.setColumnWidth(0, 175)
            columnModel.setColumnWidth(1, 275)
            columnModel.setColumnWidth(2, 275)
            columnModel.setColumnWidth(3, 175)
            columnModel.setColumnWidth(4, 500)
            columnModel.setColumnWidth(5, 500)
            gameStatsHistoryTable.columnModel = columnModel

            loginLogoutStatsTable.visibility = View.VISIBLE
            gameStatsHistoryTable.visibility = View.GONE
            togglerChangeTable = root.findViewById(R.id.toggler_change_table_profile)
            togglerChangeTable.setOnToggleSwitchChangeListener { position, _ ->
                when (position) {
                    0 -> {
                        loginLogoutStatsTable.visibility = View.VISIBLE
                        gameStatsHistoryTable.visibility = View.GONE
                    }
                    1 -> {
                        loginLogoutStatsTable.visibility = View.GONE
                        gameStatsHistoryTable.visibility = View.VISIBLE
                    }
                }}

            loginLogoutStatsTable.addDataClickListener { _, clickedData ->
                AlertDialog.Builder(context)
                    .setMessage(clickedData.joinToString(separator = "\n"))
                    .setCancelable(true)
                    .show()
            }
            gameStatsHistoryTable.addDataClickListener { _, clickedData ->
                AlertDialog.Builder(context)
                    .setMessage(clickedData.joinToString(separator = "\n"))
                    .setCancelable(true)
                    .show()
            }
        }
    }

    private fun fillLoginLogoutStatsTable(userProfile: AuthService.UserProfile) {
        with(profileViewModel) {
            val loginActivityData = ArrayList<Array<String>>()
            if (userProfile.history != null && userProfile.history.isNotEmpty()) {
                for (i in userProfile.history) {
                    loginActivityData.add(arrayOf(i.login, i.logout))
                }
                val simpleTableDataAdapter = SimpleTableDataAdapter(context, loginActivityData)
                loginLogoutStatsTable.dataAdapter = simpleTableDataAdapter
            }
        }
    }

    private fun fillGameStatsHistoryTable(userProfile: AuthService.UserProfile) {
        with(profileViewModel) {
            val gamesActivityData = ArrayList<Array<String>>()
            if (userProfile.gameHistory != null && userProfile.gameHistory.isNotEmpty()) {
                for (i in userProfile.gameHistory) {
                    var participants: String = i.participants.contentToString()
                    participants = participants.substring(1, participants.lastIndex)
                    val duration = getDurationInHoursMinutesSeconds(i.duration.toLong())
                    var results = ""
                    if (i.mode != null) {
                        if (i.mode == GameMode.TOURNAMENT || i.mode == GameMode.TOURNAMENT_ROUND || i.mode == GameMode.CLASSIC) {
                            var winners: String = i.resultClassic.w.contentToString()
                            winners = winners.substring(1, winners.lastIndex)
                            var losers: String = i.resultClassic.l.contentToString()
                            losers = losers.substring(1, losers.lastIndex)
                            results = "Gagnant(s): $winners, Perdant(s): $losers"
                        }
                        when (i.mode) {
                            GameMode.CLASSIC -> gamesActivityData.add(arrayOf("Classique", i.localStart, i.localEnd, duration, results, participants))
                            GameMode.SOLO -> gamesActivityData.add(arrayOf("Sprint Solo", i.localStart, i.localEnd, duration, ((i.resultCoop).toInt()).toString(), participants))
                            GameMode.COOP -> gamesActivityData.add(arrayOf("Sprint Coopératif", i.localStart, i.localEnd, duration, ((i.resultCoop).toInt()).toString(), participants))
                            GameMode.TOURNAMENT -> gamesActivityData.add(arrayOf("Tournoi", i.localStart, i.localEnd, duration, results, participants))
                            GameMode.TOURNAMENT_ROUND -> gamesActivityData.add(arrayOf("Tournoi (1 match)", i.localStart, i.localEnd, duration, results, participants))
                        }
                    }
                }
                val simpleTableDataAdapter = SimpleTableDataAdapter(context, gamesActivityData)
                gameStatsHistoryTable.dataAdapter = simpleTableDataAdapter
            }
        }
    }

    private fun deleteProfile() {
        application.authService.deleteUser(application.user.username)
        logout()
    }

    private fun openChooseAvatarDialog() {
        val flatDialog = FlatDialog(context)
        flatDialog.setTitle("Changer Avatar")
            .setSubtitle("Sélectionner une option pour changer votre Avatar")
            .setFirstButtonText("À partir d'une liste existante")
            .setSecondButtonText("À partir de la gallerie")
            .withFirstButtonListner {
                state.profileFragmentVisibility = false
                state.avatarPickerFragmentVisibility = true
                refreshView()
                flatDialog.dismiss()
            }
            .withSecondButtonListner {
                (activity as MainActivity).openDeviceGallery()
                flatDialog.dismiss()
            }
            .setFirstButtonColor(Color.parseColor("#22A699"))
            .setSecondButtonColor(R.color.colorAccent)
            .setBackgroundColor(Color.argb(255,41,73,127))
            .isCancelable(true)
            .show()
    }

    fun uploadCustomAvatar(bitmap: Bitmap) {
        val imageBase64 = encodeImageTobase64(bitmap)
        ImageService.upload(imageBase64!!, application.user.username, application.authService, state.profileHandler)
    }

    private fun openSetFirstNameAndLastNameDialog() {
        val flatDialog = FlatDialog(context)
        flatDialog.setTitle("Modifier votre nom")
            .setSubtitle("Entrer votre prénom et nom")
            .setFirstTextFieldHint("Prénom")
            .setSecondTextFieldHint("Nom")
            .setFirstButtonText("Confirmer")
            .setSecondButtonText("Annuler")
            .withFirstButtonListner {
                sendNewName(flatDialog)
            }
            .withSecondButtonListner {
                flatDialog.dismiss()
            }
            .setFirstButtonColor(Color.parseColor("#22A699"))
            .setSecondButtonColor(R.color.colorAccent)
            .setBackgroundColor(Color.argb(255,41,73,127))
            .isCancelable(true)
            .show()
    }

    private fun sendNewName(flatDialog: FlatDialog) {
        val firstname = flatDialog.firstTextField
        val lastname = flatDialog.secondTextField
        if (firstname.isNotEmpty() && lastname.isNotEmpty()) {
            application.authService.updateProfile(application.user.username, AuthService.ProfileUpdate(firstname, lastname, null) , state.profileHandler)
            flatDialog.dismiss()
        }
        else {
            TastyToast.makeText(context, "Les champs ne doivent pas être vides", TastyToast.LENGTH_LONG, TastyToast.ERROR)
        }
    }

    private fun initAvatarPickerView() {
        profileViewModel.avatarPickerRecyclerView = root.findViewById(R.id.square_avatar_recycler_view) as RecyclerView

        profileViewModel.backButtonOnAvatarPickerPage = root.findViewById(R.id.back_avatar_picker_button) as MaterialButton
        profileViewModel.backButtonOnAvatarPickerPage.setOnClickListener {
            state.profileFragmentVisibility = true
            state.avatarPickerFragmentVisibility = false
            refreshView()
        }

        val sGridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        profileViewModel.avatarPickerRecyclerView.layoutManager = sGridLayoutManager
        val imageList = ArrayList<String>()
        imageList.add(getURLForDrawable(R.drawable.profile_1))
        imageList.add(getURLForDrawable(R.drawable.profile_2))
        imageList.add(getURLForDrawable(R.drawable.profile_3))
        imageList.add(getURLForDrawable(R.drawable.profile_4))
        imageList.add(getURLForDrawable(R.drawable.profile_5))
        imageList.add(getURLForDrawable(R.drawable.profile_6))
        imageList.add(getURLForDrawable(R.drawable.profile_7))
        imageList.add(getURLForDrawable(R.drawable.profile_8))
        val imageGridAdapter = ImageGridAdapter(context!!, imageList)
        profileViewModel.avatarPickerRecyclerView.adapter = imageGridAdapter
        alsoStoreImagesAsBitmapForFutureReference()
        imageGridAdapter.root = this
    }

    private fun alsoStoreImagesAsBitmapForFutureReference() {
        imageListBitmap.add(BitmapFactory.decodeResource(resources, R.drawable.profile_1))
        imageListBitmap.add(BitmapFactory.decodeResource(resources, R.drawable.profile_2))
        imageListBitmap.add(BitmapFactory.decodeResource(resources, R.drawable.profile_3))
        imageListBitmap.add(BitmapFactory.decodeResource(resources, R.drawable.profile_4))
        imageListBitmap.add(BitmapFactory.decodeResource(resources, R.drawable.profile_5))
        imageListBitmap.add(BitmapFactory.decodeResource(resources, R.drawable.profile_6))
        imageListBitmap.add(BitmapFactory.decodeResource(resources, R.drawable.profile_7))
        imageListBitmap.add(BitmapFactory.decodeResource(resources, R.drawable.profile_8))
    }

    private fun getURLForDrawable(resourceId: Int): String {
        return Uri.parse("android.resource://" + R::class.java.getPackage()!!.name + "/" + resourceId)
            .toString()
    }

    override fun refreshView() {
        if (!state.profileFragmentVisibility) {
            root.findViewById<LinearLayout>(R.id.fragment_profile_parent).visibility = View.GONE
        } else {
            root.findViewById<LinearLayout>(R.id.fragment_profile_parent).visibility = View.VISIBLE
        }

        if (!state.avatarPickerFragmentVisibility) {
            root.findViewById<LinearLayout>(R.id.fragment_profile_avatar_picker_parent).visibility = View.GONE
        } else {
            root.findViewById<LinearLayout>(R.id.fragment_profile_avatar_picker_parent).visibility = View.VISIBLE
        }
    }

    private fun logout(disconnect: Boolean = true) {
        allGameModesState.alreadySwitchedToTournament = false
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        if (disconnect) application.socket.disconnect()
        activity!!.finish()
    }

    private fun getDurationInHoursMinutesSeconds(ms: Long): String {
        val hoursminutesseconds = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(ms),
                TimeUnit.MILLISECONDS.toMinutes(ms) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(ms)),
                TimeUnit.MILLISECONDS.toSeconds(ms) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(ms)));
        return hoursminutesseconds
    }

    fun encodeImageTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b: ByteArray = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun showConfirmationDialogTutorial() {
        FancyGifDialog.Builder(activity)
            .setTitle("Activer le tutoriel de nouveau?")
            .setMessage("Si vous l'activez, vous aurez à le compléter avant de pouvoir jouer à un mode.")
            .setPositiveBtnText("Oui")
            .setNegativeBtnText("Non")
            .setGifResource(R.drawable.gif_confused)
            .isCancellable(true)
            .OnPositiveClicked {
                application.user.tutorials!!.classic = true
                application.user.tutorials!!.solo = true
                application.user.tutorials!!.coop = true
                application.user.tutorials!!.tournament = true
                (activity as MainActivity).backToLobby()
            }
            .build()
    }

    private fun showConfirmationDialogLogout() {
        FancyGifDialog.Builder(activity)
            .setTitle("Se déconnecter?")
            .setMessage("Si vous êtes dans une partie en cours, vous allez perdre.")
            .setPositiveBtnText("Oui")
            .setNegativeBtnText("Non")
            .setGifResource(R.drawable.gif_rick_morty_no)
            .isCancellable(true)
            .OnPositiveClicked {
                logout()
            }
            .build()
    }

    private fun showConfirmationDialogDeleteUser() {
        FancyGifDialog.Builder(activity)
            .setTitle("Supprimer le compte?")
            .setMessage("Toutes vos données seront perdues.")
            .setPositiveBtnText("Oui")
            .setNegativeBtnText("Non")
            .setGifResource(R.drawable.gif_homer_delete_account)
            .isCancellable(true)
            .OnPositiveClicked {
                deleteProfile()
            }
            .build()
    }
}
