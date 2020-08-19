package com.example.light_client

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.light_client.src.services.AuthService
import com.example.light_client.src.services.GameMode
import com.example.light_client.ui.chat.ChatFragment
import com.example.light_client.ui.game.AllGameModesFragment
import com.example.light_client.ui.lobby.LobbyFragment
import com.example.light_client.ui.profile.ProfileFragment
import com.example.light_client.ui.tutorial.TutorialFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sdsmdg.tastytoast.TastyToast


class MainActivity : AppCompatActivity() {

    internal var fragmentProfile: ProfileFragment = ProfileFragment()
    internal var fragmentLobby: LobbyFragment = LobbyFragment()
    internal var fragmentGameMode: AllGameModesFragment = AllGameModesFragment()
    internal var fragmentTutorial: TutorialFragment = TutorialFragment()
    var fragmentChat: ChatFragment = ChatFragment()
    internal var activeFragment: Fragment = fragmentLobby
    internal val sfm = supportFragmentManager
    private lateinit var navView: BottomNavigationView

    var groupIdLobbyJoined = "0"
    var currentGameMode: GameMode = GameMode.OTHER
    var userAvatarId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // title bar removal but not working
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // fullscreen
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(itemSelectedListener)

        sfm.beginTransaction().add(R.id.content_main, fragmentProfile, "home").hide(fragmentProfile).commit()
        sfm.beginTransaction().add(R.id.content_main, fragmentGameMode, "gamemode").hide(fragmentGameMode).commit()
        sfm.beginTransaction().add(R.id.content_main, fragmentChat, "chat").hide(fragmentChat).commit()

        val tutorials = (this.application as Application).user.tutorials!!
        if ((tutorials.classic || tutorials.solo || tutorials.coop || tutorials.tournament)) {
            activateTutorial(navView, tutorials)
        }
        else {
            sfm.beginTransaction().add(R.id.content_main, fragmentTutorial, "tutorial").hide(fragmentTutorial).commit()
            sfm.beginTransaction().add(R.id.content_main, fragmentLobby, "lobby").commit()
            navView.menu.findItem(R.id.navigation_lobby).isChecked = true
            navView.menu.findItem(R.id.navigation_tutorial).isVisible = false
        }

        // dont show any game mode yet
        navView.menu.findItem(R.id.navigation_game_mode).isVisible = false

        getNotificationBadge(navView)
    }

    fun showBadgeOnNavBar() {
        navView.getOrCreateBadge(R.id.navigation_chat)
    }

    private val itemSelectedListener = object : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(navItem: MenuItem): Boolean {
            when (navItem.itemId) {
                R.id.navigation_home -> {
                    sfm.beginTransaction().hide(activeFragment).show(fragmentProfile).commit()
                    activeFragment = fragmentProfile
                    return true
                }

                R.id.navigation_lobby -> {
                    sfm.beginTransaction().hide(activeFragment).show(fragmentLobby).commit()
                    activeFragment = fragmentLobby
                    return true
                }

                R.id.navigation_game_mode -> {
                    sfm.beginTransaction().hide(activeFragment).show(fragmentGameMode).commit()
                    activeFragment = fragmentGameMode
                    return true
                }

                R.id.navigation_chat -> {
                    sfm.beginTransaction().hide(activeFragment).show(fragmentChat).commit()
                    val navView = findViewById<BottomNavigationView>(R.id.nav_view)
                    navView.removeBadge(R.id.navigation_chat)
                    activeFragment = fragmentChat
                    return true
                }

                R.id.navigation_tutorial -> {
                    sfm.beginTransaction().hide(activeFragment).show(fragmentTutorial).commit()
                    activeFragment = fragmentTutorial
                    return true
                }

            }
            return false
        }
    }

    private fun activateTutorial(navView: BottomNavigationView, tutorials: AuthService.UserTutorials) {
        sfm.beginTransaction().add(R.id.content_main, fragmentTutorial, "tutorial").commit()
        sfm.beginTransaction().add(R.id.content_main, fragmentLobby, "lobby").hide(fragmentLobby).commit()
        navView.menu.findItem(R.id.navigation_tutorial).isChecked = true
        navView.menu.findItem(R.id.navigation_home).isVisible = false
        navView.menu.findItem(R.id.navigation_chat).isVisible = false
        navView.menu.findItem(R.id.navigation_lobby).isVisible = false
        when {
            tutorials.classic -> fragmentTutorial.step = 1
            tutorials.solo -> fragmentTutorial.step = 4
            tutorials.coop -> fragmentTutorial.step = 5
            tutorials.tournament -> fragmentTutorial.step = 6
        }
    }

    fun switchLobbyToClassic() {
        switchLobbyToGameMode()
        fragmentGameMode.initGameModeFragment(GameMode.CLASSIC)
    }

    fun switchLobbyToCoop() {
        switchLobbyToGameMode()
        fragmentGameMode.initGameModeFragment(GameMode.COOP)
    }

    fun switchLobbyToTournament() {
        switchLobbyToGameMode()
        fragmentGameMode.initGameModeFragment(GameMode.TOURNAMENT)
    }

    private fun switchLobbyToGameMode() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.menu.findItem(R.id.navigation_lobby).isVisible = false
        navView.menu.findItem(R.id.navigation_game_mode).isVisible = true
        navView.selectedItemId = R.id.navigation_game_mode
        sfm.beginTransaction().hide(activeFragment).show(fragmentGameMode).commit()
        activeFragment = fragmentGameMode
    }

    fun deactivateTutorialButton() {
        fragmentProfile.profileViewModel.activateTutorial.visibility = View.GONE
    }

    fun backToLobby() {
        (application as Application).restart()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val v = currentFocus

        if (v != null &&
            (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) &&
            v is EditText &&
            !v.javaClass.name.startsWith("android.webkit.")
        ) {
            val scrcoords = IntArray(2)
            v.getLocationOnScreen(scrcoords)
            val x = ev.rawX + v.left - scrcoords[0]
            val y = ev.rawY + v.top - scrcoords[1]

            if (x < v.left || x > v.right || y < v.top || y > v.bottom)
                hideKeyboard(this)
        }
        return super.dispatchTouchEvent(ev)
    }

    fun hideKeyboard(activity: Activity?) {
        if (activity != null && activity.window != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        }
    }

    private fun getNotificationBadge(navView: BottomNavigationView) {
        val tab = navView.findViewById<BottomNavigationItemView>(R.id.navigation_chat)
        val badgeHolder = LayoutInflater.from(this).inflate(R.layout.chat_notification_badge, tab, true)
        val badge = badgeHolder.findViewById<TextView>(R.id.notifications_badge)
        badge.visibility = View.GONE
        (application as Application).chatBadge = badge
    }

    override fun onBackPressed() {
        // disable backpress button (do nothing!!)
    }

    fun openDeviceGallery() {
        intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        val mimeTypes: Array<String> = arrayOf("image/jpeg", "image/png")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        startActivityForResult(intent, 100)
    }

    // get Image selected in gallery
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) when (requestCode) {
            100 -> { // GALLERY_CODE
                val selectedImage = data!!.data
                val filePathColumn =
                    arrayOf(MediaStore.Images.Media.DATA)
                val cursor: Cursor? =
                    contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
                val imgDecodableString: String = cursor.getString(columnIndex)
                cursor.close()

                val bitmapToUpload = BitmapFactory.decodeFile(imgDecodableString)
                if (bitmapToUpload != null) fragmentProfile.uploadCustomAvatar(bitmapToUpload)
                else {
                    TastyToast.makeText(this, "Erreur, image non importée,\nassurez vous de donner les accès à l'application à votre mémoire interne.", TastyToast.LENGTH_LONG, TastyToast.ERROR)
                    requestAppPermissions()
                }
            }
        }
    }

    private fun requestAppPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return
        if (hasReadPermissions() && hasWritePermissions()) return
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 1 // REQUEST_WRITE_STORAGE_REQUEST_CODE
        )
    }

    private fun hasReadPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            baseContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasWritePermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            baseContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }
}