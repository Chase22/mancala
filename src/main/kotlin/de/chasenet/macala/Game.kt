package de.chasenet.macala

class Game(
    private val onTurn: (Game) -> Unit,
    private val onGameOver: (Game) -> Unit
) {
    private val gameBoard = GameBoard()
    private var activePlayer = PlayerId(0)

    val perspective: PlayerPerspective
        get() = PlayerPerspective(activePlayer, gameBoard)

    private fun takeTurn() {
        activePlayer = activePlayer.enemy

        if (!gameBoard.hasPlayerStones(activePlayer)) {
            perspective.enemyBoard.moveAllToHome()
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