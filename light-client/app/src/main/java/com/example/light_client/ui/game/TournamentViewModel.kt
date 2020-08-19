package com.example.light_client.ui.game

import android.widget.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import nl.dionsegijn.konfetti.KonfettiView


@Suppress("PropertyName")
class TournamentViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is tournament Fragment"
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

    lateinit var viewPassive: ProgressiveDraw
    lateinit var scorePassiveMyTeam: TextView
    lateinit var scorePassiveOtherTeam: TextView
    lateinit var currentRoundPassive: TextView
    lateinit var counterPassive: TextView
    lateinit var timerPassive: TextView
    lateinit var attemptLeftPassive: TextView

    lateinit var konfettiViewGuess: KonfettiView
    lateinit var konfettiViewWaiting: KonfettiView

    lateinit var tableLayout_1_1: TableLayout
    lateinit var tableLayout_1_2: TableLayout
    lateinit var tableLayout_1_3: TableLayout
    lateinit var tableLayout_1_4: TableLayout
    lateinit var tableLayout_1_5: TableLayout
    lateinit var tableLayout_1_6: TableLayout
    lateinit var tableLayout_1_7: TableLayout
    lateinit var tableLayout_1_8: TableLayout
    lateinit var tableLayout_2_1: TableLayout
    lateinit var tableLayout_2_2: TableLayout
    lateinit var tableLayout_2_3: TableLayout
    lateinit var tableLayout_2_4: TableLayout
    lateinit var tableLayout_3_1: TableLayout
    lateinit var tableLayout_3_2: TableLayout
    lateinit var tableLayout_4_1: TableLayout
    lateinit var firstColumnBrackets: RelativeLayout
    lateinit var secondColumnBrackets: RelativeLayout
    lateinit var thirdColumnBrackets: RelativeLayout
    lateinit var fourColumnBrackets: RelativeLayout
    lateinit var timerWaitingTournament: TextView
    lateinit var waitingInfoText: TextView
}