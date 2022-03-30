package de.chasenet.macala.ui

import de.chasenet.macala.Game
import de.chasenet.macala.Player
import de.chasenet.macala.PlayerEvents
import java.awt.Dimension
import javax.swing.JFrame

class MainFrame(private val game: Game) : JFrame(), PlayerEvents {
    val gameContainer = GameContainer()
    lateinit var player: Player

    init {
        size = Dimension(800, 600)

        add(gameContainer)

        defaultCloseOperation = EXIT_ON_CLOSE
        isVisible = true

    }

    override fun onGameStart(game: Game, players: List<Player>) = gameContainer.bind(player, game)
    override fun onTurn(game: Game) = gameContainer.bind(player, game)
    override fun onBoardUpdated(game: Game) = gameContainer.bind(player, game)

    override fun onGameEnd(game: Game, winningPlayer: Int) {
        gameContainer.bind(player, game, true)
    }
}