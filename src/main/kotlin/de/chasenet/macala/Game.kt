package de.chasenet.macala

class Game(
    private val onTurn: (Game) -> Unit,
    private val onGameOver: (Game) -> Unit
) {
    private val gameBoard = GameBoard()
    var activePlayer = 0

    val activeBoard
        get() = gameBoard.players[activePlayer]

    val inactiveBoard
        get() = gameBoard.switchBoards(activePlayer)

    private fun takeTurn() {
        activePlayer = if (activePlayer == 0) 1 else 0

        if (!gameBoard.hasPlayerStones(activePlayer)) {
            inactiveBoard.moveAllToHome()
            onGameOver(this)
        }
    }

    fun move(pit: Int) {
        if (!gameBoard.move(activePlayer, pit)) {
            takeTurn()
        }
        onTurn(this)
    }
}