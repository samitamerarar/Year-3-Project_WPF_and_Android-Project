package com.example.light_client.ui.game

import android.annotation.SuppressLint
import android.graphics.*
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import com.example.light_client.R
import com.example.light_client.ui.core.CoreFragment
import com.example.light_client.src.services.GameService
import com.example.light_client.src.services.PlayerRole
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import android.media.MediaPlayer
import android.os.Handler
import android.view.inputmethod.EditorInfo
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.light_client.MainActivity
import com.shashank.sony.fancygifdialoglib.FancyGifDialog
import nl.dionsegijn.konfetti.KonfettiView
import android.widget.TextView
import com.example.light_client.Application
import com.google.android.material.textfield.TextInputEditText
import com.sdsmdg.tastytoast.TastyToast


class CoopFragment : CoreFragment<CoopState>("coop", CoopState::class.java, R.layout.coop_all_fragments) {
    private lateinit var coopViewModel: CoopViewModel
    private var gameModeCountDownTimer: GameModeCountDownTimer = GameModeCountDownTimer(1,1, null)
    lateinit var allGameModesState: AllGameModesState

    lateinit var mp: MediaPlayer


    override fun onCreateFragment() {
        coopViewModel = ViewModelProviders.of(this).get(CoopViewModel::class.java)
        initGuessView()

        val app = activity!!.application as Application
        allGameModesState = app.stateManager.get("gamemodes", AllGameModesState::class.java)
    }

    @SuppressLint("SetTextI18n")
    private fun updateRoundText(gameState: GameService.GameState, score: TextView, scoreOther: TextView, currentRound: TextView, attempts: TextView) {
        score.text = "Score: " + gameState.points.toString()
        scoreOther.text = "Score adversaire: " + gameState.otherPoints.toString()
        currentRound.text = "Dessin: " + (gameState.currentRound + 1).toString()
        attempts.text = "Essais restants: " + gameState.attemptsLeft.toString()
    }

    private inner class GameModeCountDownTimer(duration: Long, interval: Long, private var timer: TextView?) : CountDownTimer(duration, interval) {
        init { start() }

        override fun onTick(duration: Long) {
            if (context == null) gameModeCountDownTimer.cancel()
            else if (timer != null) {
                this.timer!!.text = "Temps: " + (duration/1000).toString()
                if ((duration/1000) == 10.toLong()) playsSoundEffect(10, R.raw.clock_ticking)
            }
        }

        override fun onFinish() { gameModeCountDownTimer.cancel() }
    }

    private fun resetAllViews() {
        resetGuessView()
        initGuessView()
    }

    fun showEndGameView(winner: GameService.Winner) {
        stopMediaPlayer()
        if (winner.winner) {
            initKonfettiViewForWinnerTeam(coopViewModel.konfettiViewGuess)
            buildEndGameDialog(true)
        }
        else {
            buildEndGameDialog(false)
        }
    }

    @SuppressLint("SetTextI18n")
    fun initProperPlayerView(gameState: GameService.GameState) {
        (activity as MainActivity).hideKeyboard(activity)
        playsSoundEffect(1, R.raw.computer_error_alert_soundbiblecom)
        if (gameState.clear) resetAllViews()
        if (gameState.role == PlayerRole.GUESS) {
            this.state.guessFragmentVisibility = true
            with (coopViewModel) {
                updateRoundText(gameState, scoreGuessMyTeam, scoreGuessOtherTeam, currentRoundGuess, attemptLeftGuess)
                if (gameState.startTimer != null) {
                    gameModeCountDownTimer.cancel()
                    gameModeCountDownTimer = GameModeCountDownTimer(gameState.startTimer!!.toLong(), 1000, timerGuess)
                }
            }
        }
        refreshView()
    }

    /* View for Guess Player */

    private fun initGuessView() {
        with(coopViewModel) {
            viewGuess = root.findViewById(R.id.progressive_draw_guess) as ProgressiveDraw
            scoreGuessMyTeam = root.findViewById(R.id.score_guess_my_team) as TextView
            scoreGuessOtherTeam = root.findViewById(R.id.score_guess_other_team) as TextView
            currentRoundGuess = root.findViewById(R.id.current_round_guess) as TextView
            timerGuess = root.findViewById(R.id.timer_guess) as TextView
            konfettiViewGuess = root.findViewById(R.id.viewKonfetti_guess)
            attemptLeftGuess = root.findViewById(R.id.attempts_guess) as TextView
            counterGuess = root.findViewById(R.id.is_counter_guess) as TextView
            counterGuess.visibility = View.GONE
            scoreGuessOtherTeam.visibility = View.GONE

            guessWord = root.findViewById(R.id.word_guessed_field) as TextInputEditText
            guessWord.setOnEditorActionListener { _, actionId, _ ->
                if(actionId == EditorInfo.IME_ACTION_SEND){
                    guess()
                    true } else { false } }
            guess = root.findViewById(R.id.send_guessed_word) as Button
            val guessClickListener = View.OnClickListener { root ->
                when (root.id) { R.id.send_guessed_word -> guess() } }
            guess.setOnClickListener(guessClickListener)

            askClue = root.findViewById(R.id.ask_for_clue) as Button
            val askClueClickListener = View.OnClickListener { root ->
                when (root.id) { R.id.ask_for_clue -> askClue() } }
            askClue.setOnClickListener(askClueClickListener)
        }
    }

    private fun guess() {
        val theWordGuessed = coopViewModel.guessWord.text.toString()
        allGameModesState.gameService.guess(theWordGuessed)
        coopViewModel.guessWord.text!!.clear()
    }

    private fun askClue() {
        allGameModesState.gameService.askClue()
        // TastyToast.makeText(context, "Regarde tes messages", TastyToast.LENGTH_LONG, TastyToast.INFO)
    }

    private fun resetGuessView() {
        with(coopViewModel) {
            viewGuess.clearCanvas()
            viewGuess.svgPath = Path()
            guessWord.text!!.clear()
        }
    }

    // DRAW on Guess View

    fun addPath(points: Array<GameService.DrawPoint>) {
        coopViewModel.viewGuess.isSVG = false
        points.forEach {point ->
            if (point.action == GameService.DrawAction.ADD.action) {
                val color = Color.parseColor("#" + point.pointParams.color!!.substring(3))
                val strokeWidth = if (point.pointParams.width != null) point.pointParams.width else 10f
                var cap = Paint.Cap.ROUND
                if (point.pointParams.pointNature == GameService.PointNature.rectangle.name) cap = Paint.Cap.SQUARE
                with(coopViewModel) {
                    viewGuess.addPointToList(point.id, point.point)
                    viewGuess.addStyleToSegment(point.id, Triple(color, strokeWidth!!, cap))
                }
            }
            else if (point.action == GameService.DrawAction.DEL.action) {
                val path = Path()
                path.moveTo(0f, 0f)
                path.lineTo(0f, 0f)
                with (coopViewModel) {
                    viewGuess.segmentList[coopViewModel.viewGuess.idSegmentToPositionMap[point.id]!!] = arrayListOf(path)
                    viewGuess.addStyleToSegment(point.id, Triple(Color.WHITE, 0f, Paint.Cap.ROUND))
                }
            }
            coopViewModel.viewGuess.invalidate()
        }
    }

    fun addPathAsSVGCommand(commands: Array<GameService.SvgCommand>) {
        with(coopViewModel) {
            viewGuess.isSVG = true
            commands.forEach { command ->
                when (command.command) {
                    "M" -> {
                        viewGuess.svgPath.moveTo(command.args[0], command.args[1])
                    }
                    "L" -> {
                        viewGuess.svgPath.lineTo(command.args[0], command.args[1])
                    }
                    "C" -> {
                        viewGuess.svgPath.cubicTo(command.args[0], command.args[1], command.args[2], command.args[3], command.args[4], command.args[5])
                    }
                }
                viewGuess.invalidate()
            }
        }
    }

    override fun refreshView() {
        if (!state.guessFragmentVisibility) {
            root.findViewById<CoordinatorLayout>(R.id.fragment_guess_parent_coop).visibility = View.GONE
        } else {
            root.findViewById<CoordinatorLayout>(R.id.fragment_guess_parent_coop).visibility = View.VISIBLE
        }
    }

    private fun initKonfettiViewForWinnerTeam(konfettiView: KonfettiView) {
        konfettiView.bringToFront()
        konfettiView.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 360.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.RECT, Shape.CIRCLE)
            .addSizes(Size(12))
            .setPosition(context!!.resources.displayMetrics.widthPixels.toFloat() / 2, 0f)
            .streamFor(450, 25000L)
    }

    private fun playsSoundEffect(seconds: Long, mp3: Int) {
        stopMediaPlayer()
        mp = MediaPlayer.create(context, mp3)
        mp.start()
        mp.isLooping = true
        val h = Handler()
        val stopPlaybackRun = Runnable { stopMediaPlayer() }
        h.postDelayed(stopPlaybackRun, seconds * 1000)
    }

    private fun stopMediaPlayer() {
        try {
            if (mp.isPlaying) {
                mp.stop()
                mp.reset()
                mp.release()
            }
        } catch (e: Exception) {
            // media player is not initialized
        }
    }

    private fun buildEndGameDialog(winner: Boolean) {
        val title: String; val gif: Int
        val message = "Cliquez sur OK pour retourner au menu principal."
        if (winner) {
            title = "Temps écoulé! Partie terminée."
            gif = R.drawable.gif_game_over
        }
        else {
            title = "Vous avez perdu..."
            gif = R.drawable.gif_naruto_crying
        }
        FancyGifDialog.Builder(activity)
            .setTitle(title)
            .setMessage(message)
            .setPositiveBtnText("OK")
            .setGifResource(gif)
            .isCancellable(false)
            .OnPositiveClicked {
                (activity as MainActivity).backToLobby()
            }
            .build()
    }
}