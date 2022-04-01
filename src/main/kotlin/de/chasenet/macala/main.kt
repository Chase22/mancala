package de.chasenet.macala

import de.chasenet.macala.ai.RandomAgent
import de.chasenet.macala.ui.MainFrame

fun main() {
    val game = Game()

    val mainFrame = MainFrame(game)

    mainFrame.player = game.registerPlayer(mainFrame, "Player")
    mainFrame.title = "Player"

    RandomAgent(game, "Player2")

    game.startGame()

}