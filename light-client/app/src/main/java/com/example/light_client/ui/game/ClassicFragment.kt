package com.example.light_client.ui.game

import android.annotation.SuppressLint
import android.graphics.*
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import com.example.light_client.R
import com.example.light_client.ui.core.CoreFragment
import com.example.light_client.lib.CanvasView
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
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


class ClassicFragment : CoreFragment<ClassicState>("classic", ClassicState::class.java, R.layout.classic_all_fragments) {
    private lateinit var classicViewModel: ClassicViewModel
    private var gameModeCountDownTimer: GameModeCountDownTimer = GameModeCountDownTimer(1,1, null)
    private var previouslyIWasPainter = false
    lateinit var allGameModesState: AllGameModesState

    // black color
    private var colorHexRed = 0
    private var colorHexGreen = 0
    private var colorHexBlue = 0

    lateinit var mp: MediaPlayer


    override fun onCreateFragment() {
        classicViewModel = ViewModelProviders.of(this).get(ClassicViewModel::class.java)
        initPaintView()
        initGuessView()
        initPassiveView()

        val app = activity!!.application as Application
        allGameModesState = app.stateManager.get("gamemodes", AllGameModesState::class.java)
    }

    @SuppressLint("SetTextI18n")
    private fun updateRoundText(gameState: GameService.GameState, score: TextView, scoreOther: TextView, currentRound: TextView, attempts: TextView) {
        score.text = "Score: " + gameState.points.toString()
        scoreOther.text = "Score adversaire: " + gameState.otherPoints.toString()
        currentRound.text = "Manche: " + (gameState.currentRound + 1).toString()
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
        resetPaintView()
        initPaintView()
        resetGuessView()
        initGuessView()
        resetPassiveView()
        initPassiveView()
    }

    fun showEndGameView(winner: GameService.Winner) {
        stopMediaPlayer()
        gameModeCountDownTimer.cancel()
        if (winner.winner) {
            initKonfettiViewForWinnerTeam(classicViewModel.konfettiViewPaint)
            initKonfettiViewForWinnerTeam(classicViewModel.konfettiViewGuess)
            initKonfettiViewForWinnerTeam(classicViewModel.konfettiViewPassive)
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
        if (gameState.clear && !gameState.counter) resetAllViews()
        when (gameState.role) {
            PlayerRole.WRITE -> {
                previouslyIWasPainter = true
                this.state.paintFragmentVisibility = true
                this.state.guessFragmentVisibility = false
                this.state.passiveFragmentVisibility = false
                with (classicViewModel) {
                    wordToGuess.text = "Dessine: " + gameState.answer
                    updateRoundText(gameState, scorePaintMyTeam, scorePaintOtherTeam, currentRoundPaint, attemptLeftPaint)
                    if (gameState.counter) counterPaint.visibility = View.VISIBLE
                    if (gameState.startTimer != null) {
                        gameModeCountDownTimer.cancel()
                        gameModeCountDownTimer = GameModeCountDownTimer(gameState.startTimer!!.toLong(), 1000, timerPaint)
                    }
                }
            }
            PlayerRole.GUESS -> {
                previouslyIWasPainter = false
                this.state.paintFragmentVisibility = false
                this.state.guessFragmentVisibility = true
                this.state.passiveFragmentVisibility = false
                with (classicViewModel) {
                    updateRoundText(gameState, scoreGuessMyTeam, scoreGuessOtherTeam, currentRoundGuess, attemptLeftGuess)
                    if (gameState.counter) counterGuess.visibility = View.VISIBLE
                    if (gameState.startTimer != null) {
                        gameModeCountDownTimer.cancel()
                        gameModeCountDownTimer = GameModeCountDownTimer(gameState.startTimer!!.toLong(), 1000, timerGuess)
                    }
                }
            }
            PlayerRole.PASSIVE -> {
                classicViewModel.viewPassive.counter = gameState.counter
                if (gameState.counter && previouslyIWasPainter) {
                    classicViewModel.viewPassive.bitmap = classicViewModel.canvas.bitmap
                    classicViewModel.viewPassive.invalidate()
                }
                previouslyIWasPainter = false
                this.state.paintFragmentVisibility = false
                this.state.guessFragmentVisibility = false
                this.state.passiveFragmentVisibility = true
                with (classicViewModel) {
                    updateRoundText(gameState, scorePassiveMyTeam, scorePassiveOtherTeam, currentRoundPassive, attemptLeftPassive)
                    if (gameState.counter) counterPassive.visibility = View.VISIBLE
                    if (gameState.startTimer != null) {
                        gameModeCountDownTimer.cancel()
                        gameModeCountDownTimer = GameModeCountDownTimer(gameState.startTimer!!.toLong(), 1000, timerPassive)
                    }
                }
            }
        }
        refreshView()
    }

    /* View for Paint Mode */

    private fun initPaintView() {
        with(classicViewModel) {
            canvas = root.findViewById(R.id.canvas) as CanvasView
            penCircle = root.findViewById(R.id.image_draw_pen_circle) as ImageView
            penSquare = root.findViewById(R.id.image_draw_pen_square) as ImageView
            eraserDraw = root.findViewById(R.id.image_eraser_draw) as ImageView
            eraseSegment = root.findViewById(R.id.image_eraser_segment) as ImageView
            colorPicker = root.findViewById(R.id.image_color_picker) as ImageView
            seekbarSegmentWidth = root.findViewById(R.id.seekbar_width) as SeekBar

            wordToGuess = root.findViewById(R.id.word_to_guess_paint) as TextView

            scorePaintMyTeam = root.findViewById(R.id.score_paint_my_team) as TextView
            scorePaintOtherTeam = root.findViewById(R.id.score_paint_other_team) as TextView
            currentRoundPaint = root.findViewById(R.id.current_round_paint) as TextView
            counterPaint = root.findViewById(R.id.is_counter_paint) as TextView
            timerPaint = root.findViewById(R.id.timer_paint) as TextView
            konfettiViewPaint = root.findViewById(R.id.viewKonfetti_paint)
            attemptLeftPaint = root.findViewById(R.id.attempts_paint) as TextView

            counterPaint.visibility = View.GONE
        }

        val clickListener = View.OnClickListener {root ->
            when (root.id) {
                R.id.image_draw_pen_circle -> changeDrawingTool("PEN_CIRCLE")
                R.id.image_draw_pen_square -> changeDrawingTool("PEN_SQUARE")
                R.id.image_eraser_draw -> changeDrawingTool("ERASER")
                R.id.image_eraser_segment -> changeDrawingTool("ERASER_SEG")
                R.id.image_color_picker -> changeDrawingTool("COLOR")
            }
        }

        with(classicViewModel) {
            penCircle.setOnClickListener(clickListener)
            penSquare.setOnClickListener(clickListener)
            eraserDraw.setOnClickListener(clickListener)
            eraseSegment.setOnClickListener(clickListener)
            colorPicker.setOnClickListener(clickListener)

            seekbarSegmentWidth.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                var newProgress = 0
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    newProgress = progress
                }

                override fun onStartTrackingTouch(seekbar: SeekBar) {
                    // not implemented
                }

                override fun onStopTrackingTouch(seekbar: SeekBar) {
                    canvas.paintStrokeWidth = newProgress.toFloat()
                }
            })
        }
    }

    private fun setStyle(d: CanvasView.Drawer, c: Paint.Cap, col: Int) {
        with(classicViewModel.canvas) {
            drawer = d
            lineCap = c
            paintStrokeColor = col
            paintFillColor = col
            paintStyle = Paint.Style.STROKE
        }
    }

    private fun changeDrawingTool(tool: String) {
        with(classicViewModel.canvas) {
            when(tool) {
                "PEN_CIRCLE" -> {
                    setStyle(CanvasView.Drawer.PEN, Paint.Cap.ROUND, Color.rgb(colorHexRed, colorHexGreen, colorHexBlue))
                }
                "PEN_SQUARE" -> {
                    setStyle(CanvasView.Drawer.PEN, Paint.Cap.SQUARE, Color.rgb(colorHexRed, colorHexGreen, colorHexBlue))
                }
                "ERASER" -> {
                    setStyle(CanvasView.Drawer.PEN, Paint.Cap.ROUND, Color.rgb(255, 255, 255))
                }
                "ERASER_SEG" -> {
                    setStyle(CanvasView.Drawer.ERASER_SEGMENT, Paint.Cap.ROUND, Color.rgb(255, 255, 255))
                }
                "COLOR" -> {
                    val cp = ColorPicker(activity, colorHexRed, colorHexGreen, colorHexBlue)
                    cp.show()
                    cp.setCallback { color ->
                        colorHexRed = Color.red(color)
                        colorHexGreen = Color.green(color)
                        colorHexBlue = Color.blue(color)
                        paintStrokeColor = color
                        paintFillColor = color
                        cp.dismiss()
                    }
                }
            }
        }
    }

    private fun resetPaintView() {
        colorHexRed = 0; colorHexGreen = 0; colorHexBlue = 0
        changeDrawingTool("PEN_CIRCLE")
        with(classicViewModel) {
            counterPaint.visibility = View.GONE
            seekbarSegmentWidth.progress = 10
            canvas.paintStrokeWidth = 10f
            canvas.clear()
            canvas.pointsListOfPathsList = java.util.ArrayList()
            canvas.historyPointer = 0
            canvas.setup()
        }
    }

    /* View for Guess Player */

    private fun initGuessView() {
        with(classicViewModel) {
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
        val theWordGuessed = classicViewModel.guessWord.text.toString()
        allGameModesState.gameService.guess(theWordGuessed)
        classicViewModel.guessWord.text!!.clear()
    }

    private fun askClue() {
        allGameModesState.gameService.askClue()
        // TastyToast.makeText(context, "Regarde tes messages", TastyToast.LENGTH_LONG, TastyToast.INFO)
    }

    private fun resetGuessView() {
        with(classicViewModel) {
            viewGuess.clearCanvas()
            viewGuess.svgPath = Path()
            counterGuess.visibility = View.GONE
            guessWord.text!!.clear()
        }
    }

    /* View for Passive Player */

    private fun initPassiveView() {
        with(classicViewModel) {
            viewPassive = root.findViewById(R.id.progressive_draw_passive) as ProgressiveDraw
            scorePassiveMyTeam = root.findViewById(R.id.score_passive_my_team) as TextView
            scorePassiveOtherTeam = root.findViewById(R.id.score_passive_other_team) as TextView
            currentRoundPassive = root.findViewById(R.id.current_round_passive) as TextView
            timerPassive = root.findViewById(R.id.timer_passive) as TextView
            konfettiViewPassive = root.findViewById(R.id.viewKonfetti_passive)
            attemptLeftPassive = root.findViewById(R.id.attempts_passive) as TextView
            counterPassive = root.findViewById(R.id.is_counter_passive) as TextView
            counterPassive.visibility = View.GONE
        }
    }

    private fun resetPassiveView() {
        with(classicViewModel) {
            viewPassive.clearCanvas()
            viewPassive.svgPath = Path()
            counterPassive.visibility = View.GONE
        }
    }

    // DRAW on Guess and Passive Views

    fun addPath(points: Array<GameService.DrawPoint>) {
        classicViewModel.viewGuess.isSVG = false
        classicViewModel.viewPassive.isSVG = false
        points.forEach {point ->
            if (point.action == GameService.DrawAction.ADD.action) {
                val color = Color.parseColor("#" + point.pointParams.color!!.substring(3))
                val strokeWidth = if (point.pointParams.width != null) point.pointParams.width else 10f
                var cap = Paint.Cap.ROUND
                if (point.pointParams.pointNature == GameService.PointNature.rectangle.name) cap = Paint.Cap.SQUARE
                with(classicViewModel) {
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
                with (classicViewModel) {
                    viewGuess.segmentList[classicViewModel.viewGuess.idSegmentToPositionMap[point.id]!!] = arrayListOf(path)
                    viewGuess.addStyleToSegment(point.id, Triple(Color.WHITE, 0f, Paint.Cap.ROUND))
                    viewPassive.segmentList[classicViewModel.viewPassive.idSegmentToPositionMap[point.id]!!] = arrayListOf(path)
                    viewPassive.addStyleToSegment(point.id, Triple(Color.WHITE, 0f, Paint.Cap.ROUND))
                }
            }
            classicViewModel.viewGuess.invalidate()
            classicViewModel.viewPassive.invalidate()
        }
    }

    fun addPathAsSVGCommand(commands: Array<GameService.SvgCommand>) {
        classicViewModel.viewGuess.isSVG = true
        classicViewModel.viewPassive.isSVG = true
        with(classicViewModel) {
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
        if (!state.paintFragmentVisibility) {
            root.findViewById<CoordinatorLayout>(R.id.fragment_paint_parent).visibility = View.GONE
        } else {
            root.findViewById<CoordinatorLayout>(R.id.fragment_paint_parent).visibility = View.VISIBLE
        }

        if (!state.guessFragmentVisibility) {
            root.findViewById<CoordinatorLayout>(R.id.fragment_guess_parent).visibility = View.GONE
        } else {
            root.findViewById<CoordinatorLayout>(R.id.fragment_guess_parent).visibility = View.VISIBLE
        }

        if (!state.passiveFragmentVisibility) {
            root.findViewById<CoordinatorLayout>(R.id.fragment_passive_parent).visibility = View.GONE
        } else {
            root.findViewById<CoordinatorLayout>(R.id.fragment_passive_parent).visibility = View.VISIBLE
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
        mp = MediaPlayer.create(context, mp3) // sometimes it crashes here for Attempt to invoke
        // virtual method 'android.content.res.Resources android.content.Context.getResources()' on a null object reference
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
            title = "Félicitations! Vous avez gagné !"
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
}