package com.example.light_client

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.*
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.light_client.src.services.AuthService
import com.google.android.material.textfield.TextInputLayout
import com.sdsmdg.tastytoast.TastyToast


class LoginActivity : AppCompatActivity() {

    private val usernameWarning = "Le champ \"Pseudonyme\" doit contenir au moins 1 caractère."
    private val passwordWarning = "Le champ \"Mot de passe\" doit avoir au minimum 4 caractères \net contenir au moins 1 lettre majuscule, 1 lettre minuscule et 1 chiffre."

    private lateinit var progressDialog: ProgressDialog

    private val authHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            val passwordField: TextInputLayout = findViewById((R.id.auth_password))
            val res = message.obj as AuthService.AuthResponse
            if (message.what == 200) {
                (application as Application).login(res.user)
                loadingBar()
                displayMainActivity()
            }
            else if (message.what == 401) {
                YoYo.with(Techniques.Shake).duration(1500).playOn(passwordField)
                passwordField.boxStrokeColor = Color.RED
            }

            val toast = Toast.makeText(this@LoginActivity, res.msg, Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
            toast.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initAuth()

        YoYo.with(Techniques.RollIn)
            .duration(2500)
            .playOn(findViewById(R.id.application_logo_login))

        requestAppPermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog.dismiss()
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

    private fun loadingBar() {
        progressDialog = ProgressDialog.show(this, "", "Chargement. Veuillez patienter...", true)
        progressDialog.setCancelable(true)
    }

    private fun initAuth() {
        val loginButton: Button = findViewById((R.id.login_button))
        val passwordInputField = findViewById<TextInputLayout>(R.id.auth_password).editText!!

        loginButton.setOnClickListener {
            val username = findViewById<TextInputLayout>(R.id.auth_username).editText?.text.toString()
            val password = passwordInputField.text.toString()
            if (!isValidUsername(username)) {
                TastyToast.makeText(this, usernameWarning, TastyToast.LENGTH_LONG, TastyToast.WARNING)
            } else if (!isValidPassword(password)) {
                val text = findViewById<TextView>(R.id.text_warning_password)
                text.visibility = View.VISIBLE
                TastyToast.makeText(this, passwordWarning, TastyToast.LENGTH_LONG, TastyToast.WARNING)
            }
            else {
                (application as Application).authService.auth(username, password, this.authHandler)
                hideKeyboard(this)
            }
        }

        passwordInputField.setOnEditorActionListener { _, actionId, _ ->
            if(actionId == EditorInfo.IME_ACTION_SEND){
                loginButton.performClick()
                true } else { false } }

        val yoyo = YoYo.with(Techniques.ZoomIn) //or RotateInDownLeft
            .duration(1500)
        yoyo.playOn(findViewById(R.id.auth_username))
        yoyo.playOn(findViewById(R.id.auth_password))
        YoYo.with(Techniques.ZoomIn).duration(1500).playOn(loginButton)
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

    private fun hideKeyboard(activity: Activity?) {
        if (activity != null && activity.window != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.window.decorView.windowToken, 0)
        }
    }

    private fun isValidUsername(string: String): Boolean {
        return string.isNotEmpty()
        // return string.matches("^\\w*\$".toRegex()) // at least 1 alphanumeric character
    }

    private fun isValidPassword(string: String): Boolean {
        if (string.length < 4) return false
        return string.matches("(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*".toRegex())
    }

    private fun displayMainActivity() {
        (application as Application).restart()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}