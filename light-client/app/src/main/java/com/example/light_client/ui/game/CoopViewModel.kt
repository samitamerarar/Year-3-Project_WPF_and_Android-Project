package com.example.light_client.ui.game

import android.widget.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputEditText
import nl.dionsegijn.konfetti.KonfettiView

class CoopViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is coop Fragment"
    }
    val text: LiveData<String> = _text

    lateinit var viewGuess: ProgressiveDraw
    lateinit var scoreGuessMyTeam: TextView
    lateinit var scoreGuessOtherTeam: TextView
    lateinit var currentRoundGuess: TextView
    lateinit var counterGuess: TextView
    lateinit var timerGuess: TextView
    lateinit var guess: Button
    lateinit var guessWord: TextInputEditText
    lateinit var askClue: Button
    lateinit var attemptLeftGuess: TextView

    lateinit var konfettiViewGuess: KonfettiView
}