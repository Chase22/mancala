package de.chasenet.macala

import de.chasenet.macala.ui.MainFrame

fun main() {
    val game = Game()

    val mainFrame = MainFrame(game)
    val mainFrame2 = MainFrame(game)

    mainFrame.player = game.registerPlayer(mainFrame, "Player")
    mainFrame.title = "Player"

    mainFrame2.player = game.registerPlayer(mainFrame2, "Player2")
    mainFrame2.title = "Player2"

    game.startGame()

}