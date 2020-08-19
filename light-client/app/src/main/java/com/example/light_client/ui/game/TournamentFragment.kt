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


class TournamentFragment : CoreFragment<TournamentState>("tournament", TournamentState::class.java, R.layout.tournament_all_fragments) {
    private lateinit var tournamentViewModel: TournamentViewModel
    private var gameModeCountDownTimer: GameModeCountDownTimer = GameModeCountDownTimer(1,1, null)
    lateinit var allGameModesState: AllGameModesState

    lateinit var mp: MediaPlayer
    private var gameFinished = false


    override fun onCreateFragment() {
        tournamentViewModel = ViewModelProviders.of(this).get(TournamentViewModel::class.java)
        initGuessView()
        initPassiveView()
        initWaitingView()

        val app = activity!!.application as Application
        allGameModesState = app.stateManager.get("gamemodes", AllGameModesState::class.java)
    }

    @SuppressLint("SetTextI18n")
    private fun updateRoundText(gameState: GameService.GameState, score: TextView, scoreOther: TextView, currentRound: TextView, attempts: TextView) {
        score.text = "Score: " + gameState.points.toString()
        scoreOther.text = "Score adversaire: " + gameState.otherPoints.toString()
        currentRound.text = "Manche: " + (gameState.currentRound + 1).toString()
        attempts.text = "Essais restants: ∞"// + gameState.attemptsLeft.toString()
    }

    private inner class GameModeCountDownTimer(duration: Long, interval: Long, private var timer: TextView?) : CountDownTimer(duration, interval) {
        init { start() }

        override fun onTick(duration: Long) {
            if (context == null) gameModeCountDownTimer.cancel()
            else if (gameFinished) gameModeCountDownTimer.cancel()
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
        resetPassiveView()
        initPassiveView()
    }

    fun showEndGameView(winner: GameService.Winner) {
        stopMediaPlayer()
        gameFinished = true
        if (winner.winner) {
            initKonfettiViewForWinnerTeam(tournamentViewModel.konfettiViewGuess)
            initKonfettiViewForWinnerTeam(tournamentViewModel.konfettiViewWaiting)
            buildEndGameDialog(true)
        }
        else {
            buildEndGameDialog(false)
        }
    }

    private fun initWaitingView() {
        tournamentViewModel.timerWaitingTournament = root.findViewById(R.id.timer_waiting_tournament) as TextView
        tournamentViewModel.waitingInfoText = root.findViewById(R.id.waiting_view_info_text_tournament) as TextView
        tournamentViewModel.konfettiViewWaiting = root.findViewById(R.id.viewKonfetti_waiting)
        tournamentViewModel.firstColumnBrackets = root.findViewById(R.id.first_column_brackets_tournament) as RelativeLayout
        tournamentViewModel.firstColumnBrackets.visibility = View.GONE
        tournamentViewModel.secondColumnBrackets = root.findViewById(R.id.second_column_brackets_tournament) as RelativeLayout
        tournamentViewModel.secondColumnBrackets.visibility = View.GONE
        tournamentViewModel.thirdColumnBrackets = root.findViewById(R.id.third_column_brackets_tournament) as RelativeLayout
        tournamentViewModel.thirdColumnBrackets.visibility = View.GONE
        tournamentViewModel.fourColumnBrackets = root.findViewById(R.id.four_column_brackets_tournament) as RelativeLayout
        tournamentViewModel.fourColumnBrackets.visibility = View.GONE
        tournamentViewModel.tableLayout_1_1 = root.findViewById(R.id.brackets_tableLayout_1_1_tournament) as TableLayout
        tournamentViewModel.tableLayout_1_2 = root.findViewById(R.id.brackets_tableLayout_1_2_tournament) as TableLayout
        tournamentViewModel.tableLayout_1_3 = root.findViewById(R.id.brackets_tableLayout_1_3_tournament) as TableLayout
        tournamentViewModel.tableLayout_1_4 = root.findViewById(R.id.brackets_tableLayout_1_4_tournament) as TableLayout
        tournamentViewModel.tableLayout_1_5 = root.findViewById(R.id.brackets_tableLayout_1_5_tournament) as TableLayout
        tournamentViewModel.tableLayout_1_6 = root.findViewById(R.id.brackets_tableLayout_1_6_tournament) as TableLayout
        tournamentViewModel.tableLayout_1_7 = root.findViewById(R.id.brackets_tableLayout_1_7_tournament) as TableLayout
        tournamentViewModel.tableLayout_1_8 = root.findViewById(R.id.brackets_tableLayout_1_8_tournament) as TableLayout
        tournamentViewModel.tableLayout_2_1 = root.findViewById(R.id.brackets_tableLayout_2_1_tournament) as TableLayout
        tournamentViewModel.tableLayout_2_2 = root.findViewById(R.id.brackets_tableLayout_2_2_tournament) as TableLayout
        tournamentViewModel.tableLayout_2_3 = root.findViewById(R.id.brackets_tableLayout_2_3_tournament) as TableLayout
        tournamentViewModel.tableLayout_2_4 = root.findViewById(R.id.brackets_tableLayout_2_4_tournament) as TableLayout
        tournamentViewModel.tableLayout_3_1 = root.findViewById(R.id.brackets_tableLayout_3_1_tournament) as TableLayout
        tournamentViewModel.tableLayout_3_2 = root.findViewById(R.id.brackets_tableLayout_3_2_tournament) as TableLayout
        tournamentViewModel.tableLayout_4_1 = root.findViewById(R.id.brackets_tableLayout_4_1_tournament) as TableLayout
    }

    private fun colorRow(rowToColor: TableRow, status: GameService.TournamentPlayerStatusType) {
        when (status) {
            GameService.TournamentPlayerStatusType.PLAYING -> rowToColor.setBackgroundColor(Color.parseColor("#ffff4d"))
            GameService.TournamentPlayerStatusType.WAITING -> rowToColor.setBackgroundColor(Color.parseColor("#4da6ff"))
            GameService.TournamentPlayerStatusType.LOSER -> rowToColor.setBackgroundColor(Color.parseColor("#ff4d4d"))
            GameService.TournamentPlayerStatusType.WINNER -> rowToColor.setBackgroundColor(Color.parseColor("#4dff4d"))
        }
    }

    private fun parseBracketsToProperTable(table: TableLayout, oneTableBracketState: Array<GameService.BracketStatus>, i: Int) {
        (((table.getChildAt(0)) as TableRow).getChildAt(0) as TextView).text = oneTableBracketState[i].username
        (((table.getChildAt(0)) as TableRow).getChildAt(1) as TextView).text = translateEnglishtoFrench(oneTableBracketState[i].status.toString())
        colorRow(((table.getChildAt(0)) as TableRow), oneTableBracketState[i].status)
        (((table.getChildAt(1)) as TableRow).getChildAt(0) as TextView).text = oneTableBracketState[i+1].username
        (((table.getChildAt(1)) as TableRow).getChildAt(1) as TextView).text = translateEnglishtoFrench(oneTableBracketState[i+1].status.toString())
        colorRow(((table.getChildAt(1)) as TableRow), oneTableBracketState[i+1].status)
    }

    private fun parseBracketsToProperColumn(states: Array<GameService.BracketStatus>) {
        when (states.size) {
            2 -> {
                parseBracketsToProperTable(tournamentViewModel.tableLayout_4_1, states, 0)
                tournamentViewModel.fourColumnBrackets.visibility = View.VISIBLE
            }
            4 -> {
                tournamentViewModel.thirdColumnBrackets.visibility = View.VISIBLE
                parseBracketsToProperTable(tournamentViewModel.tableLayout_3_1, states, 0)
                parseBracketsToProperTable(tournamentViewModel.tableLayout_3_2, states, 2)
                tournamentViewModel.thirdColumnBrackets.visibility = View.VISIBLE
                tournamentViewModel.fourColumnBrackets.visibility = View.VISIBLE
            }
            8 -> {
                tournamentViewModel.secondColumnBrackets.visibility = View.VISIBLE
                parseBracketsToProperTable(tournamentViewModel.tableLayout_2_1, states, 0)
                parseBracketsToProperTable(tournamentViewModel.tableLayout_2_2, states, 2)
                parseBracketsToProperTable(tournamentViewModel.tableLayout_2_3, states, 4)
                parseBracketsToProperTable(tournamentViewModel.tableLayout_2_4, states, 6)
                tournamentViewModel.secondColumnBrackets.visibility = View.VISIBLE
                tournamentViewModel.thirdColumnBrackets.visibility = View.VISIBLE
                tournamentViewModel.fourColumnBrackets.visibility = View.VISIBLE
            }
            16 -> {
                parseBracketsToProperTable(tournamentViewModel.tableLayout_1_1, states, 0)
                parseBracketsToProperTable(tournamentViewModel.tableLayout_1_2, states, 2)
                parseBracketsToProperTable(tournamentViewModel.tableLayout_1_3, states, 4)
                parseBracketsToProperTable(tournamentViewModel.tableLayout_1_4, states, 6)
                parseBracketsToProperTable(tournamentViewModel.tableLayout_1_5, states, 8)
                parseBracketsToProperTable(tournamentViewModel.tableLayout_1_6, states, 10)
                parseBracketsToProperTable(tournamentViewModel.tableLayout_1_7, states, 12)
                parseBracketsToProperTable(tournamentViewModel.tableLayout_1_8, states, 14)
                tournamentViewModel.firstColumnBrackets.visibility = View.VISIBLE
                tournamentViewModel.secondColumnBrackets.visibility = View.VISIBLE
                tournamentViewModel.thirdColumnBrackets.visibility = View.VISIBLE
                tournamentViewModel.fourColumnBrackets.visibility = View.VISIBLE
            }
        }
    }

    private fun showHideTimer(timer: Int?) {
        if (timer != null && timer > 5) {
            tournamentViewModel.timerWaitingTournament.visibility = View.VISIBLE
        } else {
            tournamentViewModel.timerWaitingTournament.visibility = View.GONE
            gameModeCountDownTimer.cancel()
        }
    }

    @SuppressLint("SetTextI18n")
    fun initProperPlayerView(gameState: GameService.GameState?, bracketsState: GameService.TournamentState?) {
        (activity as MainActivity).hideKeyboard(activity)
        playsSoundEffect(1, R.raw.computer_error_alert_soundbiblecom)
        if (bracketsState != null) {
            showHideTimer(bracketsState.startTimer)
            with(tournamentViewModel) {
                state.guessFragmentVisibility = false
                state.passiveFragmentVisibility = false
                state.waitingFragmentVisibility = true

                for (i in bracketsState.state.indices) {
                    parseBracketsToProperColumn(bracketsState.state[i])
                }

                if (bracketsState.startTimer != null) {
                    gameModeCountDownTimer.cancel()
                    gameModeCountDownTimer = GameModeCountDownTimer(bracketsState.startTimer!!.toLong(), 1000, timerWaitingTournament)
                }
            }
        }
        else if (gameState != null) {
            if (gameState.clear) resetAllViews()
            when (gameState.role) {
                PlayerRole.GUESS -> {
                    this.state.guessFragmentVisibility = true
                    this.state.passiveFragmentVisibility = false
                    this.state.waitingFragmentVisibility = false
                    with (tournamentViewModel) {
                        updateRoundText(gameState, scoreGuessMyTeam, scoreGuessOtherTeam, currentRoundGuess, attemptLeftGuess)
                        if (gameState.counter) counterGuess.visibility = View.VISIBLE
                        if (gameState.startTimer != null) {
                            gameModeCountDownTimer.cancel()
                            gameModeCountDownTimer = GameModeCountDownTimer(gameState.startTimer!!.toLong(), 1000, timerGuess)
                        }
                    }
                }
                PlayerRole.PASSIVE -> {
                    tournamentViewModel.viewPassive.counter = gameState.counter
                    this.state.guessFragmentVisibility = false
                    this.state.passiveFragmentVisibility = true
                    this.state.waitingFragmentVisibility = false
                    with (tournamentViewModel) {
                        updateRoundText(gameState, scorePassiveMyTeam, scorePassiveOtherTeam, currentRoundPassive, attemptLeftPassive)
                        if (gameState.counter) counterPassive.visibility = View.VISIBLE
                        if (gameState.startTimer != null) {
                            gameModeCountDownTimer.cancel()
                            gameModeCountDownTimer = GameModeCountDownTimer(gameState.startTimer!!.toLong(), 1000, timerPassive)
                        }
                    }
                }
            }
        }
        refreshView()
    }

    /* View for Guess Player */

    private fun initGuessView() {
        with(tournamentViewModel) {
            viewGuess = root.findViewById(R.id.progressive_draw_guess) as ProgressiveDraw
            scoreGuessMyTeam = root.findViewById(R.id.score_guess_my_team) as TextView
            scoreGuessOtherTeam = root.findViewById(R.id.score_guess_other_team) as TextView
            currentRoundGuess = root.findViewById(R.id.current_round_guess) as TextView
            timerGuess = root.findViewById(R.id.timer_guess) as TextView
            konfettiViewGuess = root.findViewById(R.id.viewKonfetti_guess)
            attemptLeftGuess = root.findViewById(R.id.attempts_guess) as TextView
            counterGuess = root.findViewById(R.id.is_counter_guess) as TextView
            counterGuess.visibility = View.GONE

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
        val theWordGuessed = tournamentViewModel.guessWord.text.toString()
        allGameModesState.gameService.guess(theWordGuessed)
        tournamentViewModel.guessWord.text!!.clear()
    }

    private fun askClue() {
        allGameModesState.gameService.askClue()
    }

    private fun resetGuessView() {
        with(tournamentViewModel) {
            viewGuess.clearCanvas()
            viewGuess.svgPath = Path()
            counterGuess.visibility = View.GONE
            guessWord.text!!.clear()
        }
    }

    /* View for Passive Player */

    private fun initPassiveView() {
        with(tournamentViewModel) {
            viewPassive = root.findViewById(R.id.progressive_draw_passive) as ProgressiveDraw
            scorePassiveMyTeam = root.findViewById(R.id.score_passive_my_team) as TextView
            scorePassiveOtherTeam = root.findViewById(R.id.score_passive_other_team) as TextView
            currentRoundPassive = root.findViewById(R.id.current_round_passive) as TextView
            timerPassive = root.findViewById(R.id.timer_passive) as TextView
            attemptLeftPassive = root.findViewById(R.id.attempts_passive) as TextView
            counterPassive = root.findViewById(R.id.is_counter_passive) as TextView
            counterPassive.visibility = View.GONE
        }
    }

    private fun resetPassiveView() {
        with(tournamentViewModel) {
            viewPassive.clearCanvas()
            viewPassive.svgPath = Path()
            counterPassive.visibility = View.GONE
        }
    }

    // DRAW on Guess and Passive Views

    fun addPath(points: Array<GameService.DrawPoint>) {
        tournamentViewModel.viewGuess.isSVG = false
        tournamentViewModel.viewPassive.isSVG = false
        points.forEach {point ->
            if (point.action == GameService.DrawAction.ADD.action) {
                val color = Color.parseColor("#" + point.pointParams.color!!.substring(3))
                val strokeWidth = if (point.pointParams.width != null) point.pointParams.width else 10f
                var cap = Paint.Cap.ROUND
                if (point.pointParams.pointNature == GameService.PointNature.rectangle.name) cap = Paint.Cap.SQUARE
                with(tournamentViewModel) {
                    viewGuess.addPointToList(point.id, point.point)
                    viewGuess.addStyleToSegment(point.id, Triple(color, strokeWidth!!, cap))
                    viewPassive.addPointToList(point.id, point.point)
                    viewPassive.addStyleToSegment(point.id, Triple(color, strokeWidth, cap))
                }
            }
            else if (point.action == GameService.DrawAction.DEL.action) {
                val path = Path()
                path.moveTo(0f, 0f)
                path.lineTo(0f, 0f)
                with (tournamentViewModel) {
                    viewGuess.segmentList[tournamentViewModel.viewGuess.idSegmentToPositionMap[point.id]!!] = arrayListOf(path)
                    viewGuess.addStyleToSegment(point.id, Triple(Color.WHITE, 0f, Paint.Cap.ROUND))
                    viewPassive.segmentList[tournamentViewModel.viewPassive.idSegmentToPositionMap[point.id]!!] = arrayListOf(path)
                    viewPassive.addStyleToSegment(point.id, Triple(Color.WHITE, 0f, Paint.Cap.ROUND))
                }
            }
            tournamentViewModel.viewGuess.invalidate()
            tournamentViewModel.viewPassive.invalidate()
        }
    }

    fun addPathAsSVGCommand(commands: Array<GameService.SvgCommand>) {
        tournamentViewModel.viewGuess.isSVG = true
        tournamentViewModel.viewPassive.isSVG = true
        with(tournamentViewModel) {
            commands.forEach { command ->
                when (command.command) {
                    "M" -> {
                        viewGuess.svgPath.moveTo(command.args[0], command.args[1])
                        viewPassive.svgPath.moveTo(command.args[0], command.args[1])
                    }
                    "L" -> {
                        viewGuess.svgPath.lineTo(command.args[0], command.args[1])
                        viewPassive.svgPath.lineTo(command.args[0], command.args[1])
                    }
                    "C" -> {
                        viewGuess.svgPath.cubicTo(command.args[0], command.args[1], command.args[2], command.args[3], command.args[4], command.args[5])
                        viewPassive.svgPath.cubicTo(command.args[0], command.args[1], command.args[2], command.args[3], command.args[4], command.args[5])
                    }
                }
                viewGuess.invalidate()
                viewPassive.invalidate()
            }
        }
    }

    override fun refreshView() {
        if (!state.guessFragmentVisibility) {
            root.findViewById<CoordinatorLayout>(R.id.fragment_guess_parent_tournament).visibility = View.GONE
        } else {
            root.findViewById<CoordinatorLayout>(R.id.fragment_guess_parent_tournament).visibility = View.VISIBLE
        }

        if (!state.passiveFragmentVisibility) {
            root.findViewById<CoordinatorLayout>(R.id.fragment_passive_parent_tournament).visibility = View.GONE
        } else {
            root.findViewById<CoordinatorLayout>(R.id.fragment_passive_parent_tournament).visibility = View.VISIBLE
        }

        if (!state.waitingFragmentVisibility) {
            root.findViewById<CoordinatorLayout>(R.id.fragment_waiting_parent_tournament).visibility = View.GONE
        } else {
            root.findViewById<CoordinatorLayout>(R.id.fragment_waiting_parent_tournament).visibility = View.VISIBLE
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

    fun proposeToQuitOrToStay(roundEndInfo: GameService.Winner) {
        if (!roundEndInfo.winner) {
            tournamentViewModel.waitingInfoText.text = "Vous avez perdu..."
        }
        else {
            tournamentViewModel.waitingInfoText.text = "Félicitations! Veuillez patienter pendant que les autres joueurs finissent leurs parties..."
        }
    }

    private fun buildEndGameDialog(winner: Boolean) {
        val title: String; val gif: Int
        val message = "Cliquez sur OK pour retourner au menu principal."
        if (winner) {
            title = "Félicitations! Vous avez gagné le tournoi !"
            gif = R.drawable.gif_celebration_cropped
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

    private fun translateEnglishtoFrench(word: String): String {
        when (word) {
            "WAITING" -> return "EN ATTENTE"
            "PLAYING" -> return "EN JEU"
            "WINNER" -> return "GAGNANT"
            "LOSER" -> return "PERDANT"
        }
        return "undefined"
    }
}