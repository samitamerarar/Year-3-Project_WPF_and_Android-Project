package com.example.light_client.ui.tutorial

import android.graphics.*
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import com.example.light_client.R
import com.example.light_client.ui.core.CoreFragment
import com.example.light_client.lib.CanvasView
import com.pes.androidmaterialcolorpickerdialog.ColorPicker
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import androidx.coordinatorlayout.widget.CoordinatorLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.light_client.MainActivity
import com.example.light_client.src.services.GameMode
import com.example.light_client.ui.game.ProgressiveDraw
import com.google.android.material.textfield.TextInputEditText
import com.sdsmdg.tastytoast.TastyToast
import com.shashank.sony.fancygifdialoglib.FancyGifDialog
import kotlinx.android.synthetic.main.classic_guess_fragment.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType


class TutorialFragment : CoreFragment<TutorialState>("tutorial", TutorialState::class.java, R.layout.tutorial_all_fragments) {
    private lateinit var tutorialViewModel: TutorialViewModel
    private var gameModeCountDownTimer: GameModeCountDownTimer = GameModeCountDownTimer(1,1, null)

    // black color
    private var colorHexRed = 0
    private var colorHexGreen = 0
    private var colorHexBlue = 0

    var step = 0
    private var gameMode: GameMode? = null

    override fun onCreateFragment() {
        tutorialViewModel = ViewModelProviders.of(this).get(TutorialViewModel::class.java)
        initLobbyView()
        initPaintView()
        initGuessView()
        initPassiveView()
        nextStepLobby()
    }

    private fun nextStepLobby() {
        state.paintFragmentVisibility = false
        state.guessFragmentVisibility = false
        state.passiveFragmentVisibility = false
        state.lobbyFragmentVisibility = true
        refreshView()
        with(tutorialViewModel) {
            when (step) {
                1 -> putOnThatTarget(R.id.paint_title_dessinateur,"Bienvenue ${application.user.username} à Fais-moi un dessin!",
                    "Tappez pour continuer.", true)
                2 -> putOnThatTarget(R.id.text_lobby,"Le jeu comporte 4 modes:\nle mode Classique," +
                        "\nle mode Sprint Solo,\nle mode Sprint Coopératif\n et le mode Tournoi.",
                    "Tappez pour continuer.", true)
                3 -> {
                    selectClassicButton.setOnClickListener {
                        startClassicTutorial()
                    }
                    putOnThatTarget(R.id.mode_classique_button,"Nous commencerons par vous montrer le mode Classique.",
                        "Tappez sur le bouton pour continuer.", false)
                }
                4 -> {
                    selectSprintSoloButton.setOnClickListener {
                        startSoloTutorial()
                    }
                    putOnThatTarget(R.id.mode_sprint_solo_button,"Il est temps de vous montrer le mode Sprint Solo.",
                        "Tappez sur le bouton pour continuer.", false)
                }
                5 -> {
                    putOnThatTarget(R.id.mode_sprint_cooperatif_button,"Le mode Sprint Coopératif se joue exactement comme le mode Sprint Solo.\n " +
                            "Par contre, vous allez être dans une équipe de 2 à 4 joueurs.",
                        "Tappez sur le bouton pour continuer.", false)
                }
                6 -> {
                    application.user.tutorials!!.coop = false
                    application.authService.setTutorial(application.user.username, "coop")
                    putOnThatTarget(R.id.mode_tournament_button,"Le mode Tournoi se joue comme le mode Sprint Solo.\n" +
                            "Vous jouerez contre un seul adversaire à chaque partie.\n Celui qui devine le plus rapidement la série de dessins est le gagnant de la partie.\n" +
                            "Le gagnant du tournoi est celui qui gagne toutes ses parties.",
                        "Tappez sur le bouton pour continuer.", false)
                }
                7 -> {
                    application.user.tutorials!!.tournament = false
                    application.authService.setTutorial(application.user.username, "tournament")
                    putOnThatTarget(R.id.paint_title_dessinateur,"Félicitations! Vous avez fini le tutoriel.\nVous pouvez maintenant jouer!", "Tappez pour continuer.", true)
                }
                8 -> (activity as MainActivity).backToLobby()
            }
        }
    }

    private fun startClassicTutorial() {
        step = 0
        gameMode = GameMode.CLASSIC
        state.paintFragmentVisibility = true
        state.guessFragmentVisibility = false
        state.passiveFragmentVisibility = false
        state.lobbyFragmentVisibility = false
        refreshView()
        nextStepClassic()
    }

    private fun startSoloTutorial() {
        step = 0
        gameMode = GameMode.SOLO
        state.paintFragmentVisibility = false
        state.guessFragmentVisibility = true
        state.passiveFragmentVisibility = false
        state.lobbyFragmentVisibility = false
        refreshView()
        nextStepSolo()
    }

    private fun nextStepClassic() {
        with(tutorialViewModel) {
            when (step) {
                1 -> {
                    scoreGuessOtherTeam.visibility = View.VISIBLE
                    wordToGuess.text = "Dessine: banane"
                    scorePaintMyTeam.text = "Score: 0"
                    scorePaintOtherTeam.text = "Score adversaire: 0"
                    currentRoundPaint.text = "Manche: 1"
                    attemptLeftPaint.text = "Essais restants: 15"
                    scoreGuessMyTeam.text = "Score: 0"
                    scoreGuessOtherTeam.text = "Score adversaire: 0"
                    currentRoundGuess.text = "Manche: 1"
                    attemptLeftGuess.text = "Essais restants: 15"
                    putOnThatTarget(R.id.canvas, "Bienvenue au mode Classique!", "Tappez pour continuer.", true)
                }
                2 -> putOnThatTarget(R.id.canvas, "Vous serez dans une équipe de 2 personnes.\nVous jouerez contre une autre équipe de 2 personnes.\n" +
                        "Chaque joueur alterne entre 2 rôle: Dessinateur ou Devineur.", "Tappez pour continuer.", true)
                3 -> putOnThatTarget(R.id.word_to_guess_paint, "Vous commencerez par être Dessinateur.\nDessinez ce mot pour que votre coéquipier le devine en regardant votre dessin", "Tappez pour continuer.", true)
                4 -> putOnThatTarget(R.id.canvas, "Vous allez dessiner dans cette interface de dessin.", "Tappez pour continuer.", true)
                5 -> putOnThatTarget(R.id.draw_tools, "Cette boite à outils permet de dessiner votre dessin.", "Tappez pour continuer.", true)
                6 -> putOnThatTarget(R.id.image_draw_pen_circle, "Pour dessiner des traits ronds.", "Tappez pour continuer.", true)
                7 -> putOnThatTarget(R.id.image_draw_pen_square, "Pour dessiner des traits carrés.", "Tappez pour continuer.", true)
                8 -> putOnThatTarget(R.id.image_eraser_draw, "Pour effacer.", "Tappez pour continuer.", true)
                9 -> putOnThatTarget(R.id.image_eraser_segment, "Pour effacer en sélectionnant un trait déjà dessiné.", "Tappez pour continuer.", true)
                10 -> putOnThatTarget(R.id.image_color_picker, "Pour changer la couleur de la pointe.", "Tappez pour continuer.", true)
                11 -> putOnThatTarget(R.id.seekbar_width, "Pour changer la taille de la pointe.", "Tappez pour continuer.", true)
                12 -> putOnThatTarget(R.id.canvas, "Maintenant dessinez une banane.\nFamiliarisez vous avec la boite à outils.\nVous avez 30 secondes.", "Tappez pour continuer.", true)
                13 -> gameModeCountDownTimer = GameModeCountDownTimer(60000, 1000, timerPaint)
                14 -> {
                    scoreGuessMyTeam.text = "Score: 0"
                    scoreGuessOtherTeam.text = "Score adversaire: 0"
                    currentRoundGuess.text = "Manche: 1"
                    attemptLeftGuess.text = "Essais restants: 15"
                    timerGuess.text = "Temps: 30"
                    putOnThatTarget(R.id.guess_title_devineur, "Vous prenez maintenant le rôle de votre coéquipier qui doit deviner votre dessin.", "Tappez pour continuer.", true)
                }
                15 -> putOnThatTarget(R.id.attempts_guess, "Chaque fois que vous devinez mal, vos essais restants sont déduits.", "Tappez pour continuer.", true)
                16 -> putOnThatTarget(R.id.ask_for_clue, "Vous pouvez demander un nombre illimité d'indices.", "Tappez sur le bouton.", false)
                17 -> {
                    TastyToast.makeText(context, "NOUVEAU MESSAGE!\n\n" + "Indice: fruit", TastyToast.LENGTH_LONG, TastyToast.INFO)
                        .setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                    step++
                    nextStepClassic()
                }
                18 -> putOnThatTarget(R.id.word_guessed_field, "Vous devez écrire le mot à deviner ici.", "Tappez sur le champ de texte.", false)
                19 -> {
                    guessWord.setText("banane")
                    putOnThatTarget(R.id.word_guessed_field, "Le mot banane a été saisi pour vous.", "Tappez pour continuer.", true)
                }
                20 -> putOnThatTarget(R.id.send_guessed_word, "Vous pouvez maintenant deviner!", "Tappez sur le bouton \"Deviner\".", false)
                21 -> {
                    scoreGuessMyTeam.text = "Score: 1"
                    currentRoundGuess.text = "Manche: 2"
                    putOnThatTarget(R.id.score_guess_my_team, "Votre équipe a gagné un point!", "Tappez pour continuer.", true)
                }
                22 -> putOnThatTarget(R.id.current_round_guess, "On passe à la manche 2!", "Tappez pour continuer.", true)
                23 -> putOnThatTarget(R.id.timer_guess, "Chaque manche dure 60 secondes.\nChaque équipe joue 2 manches avant\nde passer au tour de l'autre équipe\nqui joueront aussi 2 manches et ainsi de suite.", "Tappez pour continuer.", true)
                24 -> putOnThatTarget(R.id.score_guess_my_team, "La première équipe qui atteint 5 points gagne la partie.", "Tappez pour continuer.", true)
                25 -> {
                    scorePassiveMyTeam.text = "Score: 0"
                    scorePassiveOtherTeam.text = "Score adversaire: 1"
                    currentRoundPassive.text = "Manche: 2"
                    attemptLeftPassive.text = "Essais restants: 15"
                    timerPassive.text = "Temps: 30"
                    state.paintFragmentVisibility = false
                    state.guessFragmentVisibility = false
                    state.passiveFragmentVisibility = true
                    state.lobbyFragmentVisibility = false
                    refreshView()
                    putOnThatTarget(R.id.passive_title_observateur, "Pendant ce temps, l'autre équipe regarde le match.", "Tappez pour continuer.", true)
                }
                26 -> {
                    scoreGuessMyTeam.text = "Score: 0"
                    scoreGuessOtherTeam.text = "Score adversaire: 1"
                    attemptLeftGuess.text = "Essais restants: 1"
                    timerGuess.text = "Temps: 10"
                    state.paintFragmentVisibility = false
                    state.guessFragmentVisibility = true
                    state.passiveFragmentVisibility = false
                    state.lobbyFragmentVisibility = false
                    refreshView()
                    counterGuess.visibility = View.VISIBLE
                    putOnThatTarget(R.id.is_counter_guess, "Si une équipe écoule toutes ses tentatives\nou son temps alloué sans avoir bien deviné,\nl'autre équipe a 10 secondes pour deviner à leur place.\nL'autre équipe a donc une chance de remporter le point.", "Tappez pour continuer.", true)
                }
                27 -> putOnThatTarget(R.id.attempts_guess, "Attention, en mode riposte, vous n'avez le droit qu'à 1 tentative!", "Tappez pour continuer.", true)
                28 -> putOnThatTarget(R.id.is_counter_passive, "Félicitations! Vous avez fini le tutoriel pour le mode Classique.", "Tappez pour continuer.", true)
                29 -> {
                    (activity as MainActivity).backToLobby()
                    application.user.tutorials!!.classic = false
                    application.authService.setTutorial(application.user.username, "classic")
                }
            }
        }
    }

    private fun nextStepSolo() {
        with(tutorialViewModel) {
            when (step) {
                1 -> {
                    state.paintFragmentVisibility = false
                    state.guessFragmentVisibility = true
                    state.passiveFragmentVisibility = false
                    state.lobbyFragmentVisibility = false
                    refreshView()
                    scoreGuessMyTeam.text = "Score: 0"
                    currentRoundGuess.text = "Dessin: 1"
                    attemptLeftGuess.text = "Essais restants: 15"
                    scoreGuessOtherTeam.visibility = View.GONE
                    putOnThatTarget(R.id.paint_title_dessinateur, "Bienvenue au mode Sprint Solo!", "Tappez pour continuer.",  true)
                }
                2 -> putOnThatTarget(R.id.paint_title_dessinateur, "Le but est de deviner le plus de dessins dans un temps alloué.", "Tappez pour continuer.", true)
                3 -> {
                    progressive_draw_guess.initTutorialView()
                    gameModeCountDownTimer = GameModeCountDownTimer(60000, 1000, timerGuess)
                }
                4 -> putOnThatTarget(R.id.progressive_draw_guess, "Mais qu'est-ce que c'est?", "Tappez pour continuer.", true)
                5 -> putOnThatTarget(R.id.attempts_guess, "Chaque fois que vous devinez mal, vos essais restants sont déduits.", "Tappez pour continuer.", true)
                6 -> putOnThatTarget(R.id.ask_for_clue, "Vous pouvez demander un nombre illimité d'indices.", "Tappez sur le bouton.", false)
                7 ->{
                    TastyToast.makeText(context, "NOUVEAU MESSAGE!\n\n" + "Indice: un meuble d'une cuisine", TastyToast.LENGTH_LONG, TastyToast.INFO)
                        .setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                    step++
                    nextStepSolo()
                }
                8 -> putOnThatTarget(R.id.progressive_draw_guess, "C'est certainement une table!", "Tappez pour continuer.", true)
                9 -> putOnThatTarget(R.id.word_guessed_field, "Vous devez écrire le mot à deviner ici.", "Tappez sur le champ de texte.", false)
                10 -> {
                    guessWord.setText("table")
                    putOnThatTarget(R.id.word_guessed_field, "Le mot table a été saisi pour vous.", "Tappez pour continuer.", true)
                }
                11 -> putOnThatTarget(R.id.send_guessed_word, "Vous pouvez maintenant deviner!", "Tappez sur le bouton \"Deviner\".", false)
                12 -> {
                    scoreGuessMyTeam.text = "Score: 1"
                    currentRoundGuess.text = "Dessin: 2"
                    attemptLeftGuess.text = "Essais restants: 15"
                    timerGuess.text = "Temps: 65"
                    putOnThatTarget(R.id.score_guess_my_team, "Vous avez gagné un point!", "Tappez pour continuer.", true)
                }
                13 -> putOnThatTarget(R.id.timer_guess, "Vous avez gagné du temps supplémentaire!", "Tappez pour continuer.", true)
                14 -> putOnThatTarget(R.id.current_round_guess, "On passe au dessin 2!", "Tappez pour continuer.", true)
                15 -> putOnThatTarget(R.id.attempts_guess, "Si toutes vos tentatives sont utilisées,\nvous passez au prochain dessin.", "Tappez pour continuer.", true)
                16 -> putOnThatTarget(R.id.timer_guess, "La partie finit lorsque tout le temps est écoulé", "Tappez pour continuer.", true)
                17 -> putOnThatTarget(R.id.zoom_layout_guess, "Félicitations! Vous avez fini le tutoriel pour le mode Sprint Solo.", "Tappez pour revenir au menu principal.", true)
                18 -> {
                    (activity as MainActivity).backToLobby()
                    application.user.tutorials!!.solo = false
                    application.authService.setTutorial(application.user.username, "solo")
                }
            }
        }
    }

    private fun putOnThatTarget(rid: Int, title: String, message: String, dismissable: Boolean) {
        val showcaseview = GuideView.Builder(activity)
            .setTitle(title)
            .setContentText(message)
            .setTargetView(root.findViewById<View>(rid))

        if (dismissable) showcaseview.setDismissType(DismissType.outside)
        else showcaseview.setDismissType(DismissType.targetView)
        showcaseview.setGuideListener {
            step++
            when (gameMode) {
                null -> nextStepLobby()
                GameMode.CLASSIC -> nextStepClassic()
                GameMode.SOLO -> nextStepSolo()
            }
        }
        showcaseview.build().show()
    }

    private inner class GameModeCountDownTimer(duration: Long, interval: Long, private var timer: TextView?) : CountDownTimer(duration, interval) {
        init { start() }

        override fun onTick(duration: Long) {
            if (context == null) gameModeCountDownTimer.cancel()
            else if (timer != null) {
                this.timer!!.text = "Temps: " + (duration/1000).toString()
                if (gameMode == GameMode.CLASSIC) {
                    if ((duration/1000) == 30.toLong()) {
                        gameModeCountDownTimer.cancel()
                        state.paintFragmentVisibility = false
                        state.guessFragmentVisibility = true
                        state.passiveFragmentVisibility = false
                        state.lobbyFragmentVisibility = false
                        tutorialViewModel.viewGuess.counter = true
                        tutorialViewModel.viewGuess.bitmap = tutorialViewModel.canvas.bitmap
                        tutorialViewModel.viewGuess.invalidate()
                        refreshView()
                        step++
                        nextStepClassic()
                    }
                }
                if (gameMode == GameMode.SOLO) {
                    if ((duration/1000) == 50.toLong()) {
                        gameModeCountDownTimer.cancel()
                        step++
                        nextStepSolo()
                    }
                }
            }
        }

        override fun onFinish() { }
    }

    private fun initLobbyView() {
        with(tutorialViewModel) {
            selectClassicButton = root.findViewById(R.id.mode_classique_button)
            selectSprintSoloButton = root.findViewById(R.id.mode_sprint_solo_button)
            selectSprintCooperativeButton = root.findViewById(R.id.mode_sprint_cooperatif_button)
            selectTournamentButton = root.findViewById(R.id.mode_tournament_button)
        }
    }

    /* View for Paint Mode */

    private fun initPaintView() {
        with(tutorialViewModel) {
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

            endTutorialButton = root.findViewById(R.id.paint_skip_tutorial) as TextView
            endTutorialButton.visibility = View.VISIBLE
            endTutorialButton.setOnClickListener {
                showConfirmationDialogTerminateTutorial()
            }
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

        with(tutorialViewModel) {
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
        with(tutorialViewModel.canvas) {
            drawer = d
            lineCap = c
            paintStrokeColor = col
            paintFillColor = col
            paintStyle = Paint.Style.STROKE
        }
    }

    private fun changeDrawingTool(tool: String) {
        with(tutorialViewModel.canvas) {
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

    /* View for Guess Player */

    private fun initGuessView() {
        with(tutorialViewModel) {
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
            guess.setOnClickListener { guess() }
            askClue = root.findViewById(R.id.ask_for_clue) as Button
        }
    }

    /* View for Passive Player */

    private fun initPassiveView() {
        with(tutorialViewModel) {
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

    private fun guess() {
        tutorialViewModel.guessWord.text!!.clear()
    }

    override fun refreshView() {
        if (!state.paintFragmentVisibility) {
            root.findViewById<CoordinatorLayout>(R.id.fragment_paint_parent_tutorial).visibility = View.GONE
        } else {
            root.findViewById<CoordinatorLayout>(R.id.fragment_paint_parent_tutorial).visibility = View.VISIBLE
        }

        if (!state.guessFragmentVisibility) {
            root.findViewById<CoordinatorLayout>(R.id.fragment_guess_parent_tutorial).visibility = View.GONE
        } else {
            root.findViewById<CoordinatorLayout>(R.id.fragment_guess_parent_tutorial).visibility = View.VISIBLE
        }

        if (!state.passiveFragmentVisibility) {
            root.findViewById<CoordinatorLayout>(R.id.fragment_passive_parent_tutorial).visibility = View.GONE
        } else {
            root.findViewById<CoordinatorLayout>(R.id.fragment_passive_parent_tutorial).visibility = View.VISIBLE
        }

        if (!state.lobbyFragmentVisibility) {
            root.findViewById<LinearLayoutCompat>(R.id.fragment_lobby_parent_tutorial).visibility = View.GONE
        } else {
            root.findViewById<LinearLayoutCompat>(R.id.fragment_lobby_parent_tutorial).visibility = View.VISIBLE
        }
    }

    private fun showConfirmationDialogTerminateTutorial() {
        FancyGifDialog.Builder(activity)
            .setTitle("Terminer le tutoriel?")
            .setMessage("Vous pourriez y accéder dans l'onglet \"Profil\".")
            .setPositiveBtnText("Oui")
            .setNegativeBtnText("Non")
            .setGifResource(R.drawable.gif_confused)
            .isCancellable(true)
            .OnPositiveClicked {
                application.user.tutorials!!.classic = false
                application.authService.setTutorial(application.user.username, "classic")
                application.user.tutorials!!.solo = false
                application.authService.setTutorial(application.user.username, "solo")
                application.user.tutorials!!.coop = false
                application.authService.setTutorial(application.user.username, "coop")
                application.user.tutorials!!.tournament = false
                application.authService.setTutorial(application.user.username, "tournament")
                (activity as MainActivity).backToLobby()
            }
            .build()
    }
}