package de.chasenet.macala.ui

import de.chasenet.macala.Game
import java.awt.Dimension
import javax.swing.JFrame

class MainFrame(private val game: Game) : JFrame() {
    val gameContainer = GameContainer()

    init {
        size = Dimension(800, 600)

        gameContainer.bind(game)
        add(gameContainer)

        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true

    }
}