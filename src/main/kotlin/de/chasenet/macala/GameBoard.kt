package de.chasenet.macala

class GameBoard {
    val players = Array(2) { PlayerBoard(it) }

    fun hasPlayerStones(player: Int) = players[player].hasStones

    /**
     * @return If the current player gets to play another turn
     */
    fun move(player: Int, pit: Int): Boolean {
        var board = players.getOrNull(player) ?: throw IllegalArgumentException("No player $player")
        var stones = board.pits.getOrNull(pit) ?: throw IllegalArgumentException("No pit $pit")

        if (stones == 0) throw IllegalArgumentException("Moving an empty pit")

        var currentPit = pit
        board.pits[currentPit] = 0

        while (stones > 0) {
            currentPit += 1
            if (currentPit == 6) {
                if (board.player == player) {
                    board.home += 1
                    stones -= 1
                    if (stones == 0) return true
                }
                board = switchBoards(player)
                currentPit = -1
            } else {
                board.pits[currentPit] += 1
                stones -= 1
            }
        }
        if (board.player == player && board.pits[currentPit] == 1) {
            val enemyBoard = switchBoards(board)
            board.home += board.pits[currentPit] + enemyBoard.pits[5 - currentPit]
            board.pits[currentPit] = 0
            enemyBoard.pits[5 - currentPit] = 0
        }
        return false
    }

    fun switchBoards(player: Int) = if (player == 0) players[1] else players[0]
    fun switchBoards(board: PlayerBoard) = switchBoards(board.player)

    override fun toString(): String {
        return "de.chasenet.macala.ui.GameBoard(players=${players.contentToString()})"
    }


}

class PlayerBoard(val player: Int) {
    var home: Int = 0
    val pits: Array<Int> = Array(6) { 4 }

    val hasStones: Boolean
        get() = pits.sum() > 0

    override fun toString(): String {
        return "de.chasenet.macala.ui.PlayerBoard(player=$player, home=$home, pits=${pits.contentToString()})"
    }

    fun moveAllToHome() {
        home = pits.sum()
        pits.forEachIndexed { idx, _ -> pits[idx] = 0 }
    }
}