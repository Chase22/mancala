package de.chasenet.macala.ui

import de.chasenet.macala.Game
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JPanel

class GameContainer : JPanel(GridBagLayout()) {
    private val enemyHome: PitButton = PitButton("enemyHome")
    private val ownHome = PitButton("home")
    private val enemyPits: Array<PitButton>
    private val ownPits: Array<PitButton>

    init {
        add(enemyHome, constraints(0, 0, 2))

        enemyPits = Array(6) { idx ->
            PitButton(idx.toString()).also { add(it, constraints(idx + 1, 0)) }
        }
        enemyPits.reverse()
        enemyPits.forEachIndexed { idx, button -> button.text = idx.toString() }

        ownPits = Array(6) { idx ->
            PitButton(idx.toString()).also { add(it, constraints(idx + 1, 1, 1)) }
        }

        add(ownHome, constraints(7, 0, 2))
    }

    private fun constraints(x: Int, y: Int, height: Int = 1) = GridBagConstraints().apply {
        gridx = x
        gridy = y
        gridheight = height
        insets = Insets(5, 5, 5, 5)
    }

    fun bind(game: Game, gameOver: Boolean = false) {
        with(game.perspective.ownBoard) {
            pits.forEachIndexed { idx, i ->
                val pitButton = ownPits[idx]
                pitButton.text = i.toString()
                pitButton.removeActionListener(pitButton.actionListeners.firstOrNull())

                if (!gameOver) pitButton.addActionListener { game.move(idx) }
            }
            ownHome.text = home.toString()
        }
        with(game.perspective.enemyBoard) {
            pits.forEachIndexed { idx, i -> enemyPits[idx].text = i.toString() }
            enemyHome.text = home.toString()
        }
    }
}