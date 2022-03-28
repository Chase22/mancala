package de.chasenet.macala

class GameBoard {
    val players = Array(2) { PlayerBoard(PlayerId(it)) }

    fun hasPlayerStones(player: PlayerId) = players[player.ownId].hasStones

    /**
     * @return If the current player gets to play another turn
     */
    fun move(player: PlayerId, pit: Int): Boolean {
        var board = players.getOrNull(player.ownId) ?: throw IllegalArgumentException("No player $player")
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

    fun switchBoards(player: PlayerId) = players[player.enemy.ownId]
    private fun switchBoards(board: PlayerBoard) = switchBoards(board.player)

    override fun toString(): String {
        return "de.chasenet.macala.ui.GameBoard(players=${players.contentToString()})"
    }
}

@JvmInline
value class PlayerId(
    private val id: Int
) {
    val ownId: Int
        get() = id
    val enemy: PlayerId
        get() = if (id == 0) PlayerId(1) else PlayerId(0)
}

class PlayerPerspective(private val player: PlayerId, private val gameBoard: GameBoard) {
    val ownBoard: PlayerBoard
        get() = gameBoard.players[player.ownId]

    val ownPits: Array<Int>
        get() = ownBoard.pits

    var ownHome: Int
        get() = ownBoard.home
        set(value) {
            ownBoard.home = value
        }

    val enemyBoard: PlayerBoard
        get() = gameBoard.players[player.enemy.ownId]

    val enemyPits: Array<Int>
        get() = enemyBoard.pits

    var enemyHome: Int
        get() = enemyBoard.home
        set(value) {
            enemyBoard.home = value
        }
}

class PlayerBoard(val player: PlayerId) {
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