package com.example.light_client.ui.game

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.example.light_client.R
import com.example.light_client.src.services.GameMode
import com.example.light_client.ui.core.CoreFragment


class AllGameModesFragment: CoreFragment<AllGameModesState>("gamemodes", AllGameModesState::class.java, R.layout.all_game_modes_fragments) {
    private lateinit var allGameModesViewModel: AllGameModesViewModel
    var classicFragment: ClassicFragment = ClassicFragment()
    var coopFragment: CoopFragment = CoopFragment()
    var tournamentFragment: TournamentFragment = TournamentFragment()
    private lateinit var fm: FragmentManager

    override fun onCreateFragment() {
        allGameModesViewModel = ViewModelProviders.of(this).get(AllGameModesViewModel::class.java)

        fm = activity!!.supportFragmentManager
        fm.beginTransaction().add(R.id.contentFragmentGameMode, classicFragment).hide(classicFragment).commit()
        fm.beginTransaction().add(R.id.contentFragmentGameMode, coopFragment).hide(coopFragment).commit()
        fm.beginTransaction().add(R.id.contentFragmentGameMode, tournamentFragment).hide(tournamentFragment).commit()

        state.allGameModesFragment = this
    }

    override fun refreshView() {
        //
    }

    fun initGameModeFragment(selectedGameMode: GameMode) {
        when (selectedGameMode) {
            GameMode.CLASSIC -> {
                fm.beginTransaction().show(classicFragment).commit()
            }
            GameMode.COOP -> {
                fm.beginTransaction().show(coopFragment).commit()
            }
            GameMode.TOURNAMENT -> {
                fm.beginTransaction().show(tournamentFragment).commit()
            }
        }
    }
}