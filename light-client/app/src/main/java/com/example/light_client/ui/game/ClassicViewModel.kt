package com.example.light_client.ui.game

import android.widget.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.light_client.lib.CanvasView
import com.google.android.material.textfield.TextInputEditText
import com.otaliastudios.zoom.ZoomLayout
import nl.dionsegijn.konfetti.KonfettiView

class ClassicViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is classic Fragment"
    }
    val text: LiveData<String> = _text

    lateinit var canvas: CanvasView
    lateinit var penCircle: ImageView
    lateinit var penSquare: ImageView
    lateinit var eraserDraw: ImageView
    lateinit var eraseSegment: ImageView
    lateinit var colorPicker: ImageView
    lateinit var seekbarSegmentWidth: SeekBar
    lateinit var scorePaintMyTeam: TextView
    lateinit var scorePaintOtherTeam: TextView
    lateinit var wordToGuess: TextView
    lateinit var currentRoundPaint: TextView
    lateinit var counterPaint: TextView
    lateinit var timerPaint: TextView
    lateinit var attemptLeftPaint: TextView

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

    lateinit var viewPassive: ProgressiveDraw
    lateinit var scorePassiveMyTeam: TextView
    lateinit var scorePassiveOtherTeam: TextView
    lateinit var currentRoundPassive: TextView
    lateinit var counterPassive: TextView
    lateinit var timerPassive: TextView
    lateinit var attemptLeftPassive: TextView

    lateinit var konfettiViewPaint: KonfettiView
    lateinit var konfettiViewGuess: KonfettiView
    lateinit var konfettiViewPassive: KonfettiView
}