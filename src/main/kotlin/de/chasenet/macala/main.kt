package de.chasenet.macala

import de.chasenet.macala.ui.MainFrame
import javax.swing.SwingUtilities

fun main() {
    lateinit var mainFrame: MainFrame

    val game = Game({
        SwingUtilities.invokeLater {
            mainFrame.gameContainer.bind(it)
        }
    }) {
        SwingUtilities.invokeLater {
            mainFrame.gameContainer.bind(it, true)
        }
    }

    mainFrame = MainFrame(game)
}