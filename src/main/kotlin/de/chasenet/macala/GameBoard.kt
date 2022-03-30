package de.chasenet.macala

import java.util.concurrent.atomic.AtomicInteger

class GameBoard {
    val players = Array(2) { PlayerBoard(PlayerId(it)) }

    fun hasPlayerStones(player: PlayerId) = players[player.ownId].hasStones

    /**
     * @return If the current player gets to play another turn
     */
    fun move(player: PlayerId, pit: Int): Boolean {
        var board = players.getOrNull(player.ownId) ?: throw IllegalArgumentException("No player $player")
        var stones = board.pits.getOrNull(pit)?.get() ?: throw IllegalArgumentException("No pit $pit")

        if (stones == 0) throw IllegalArgumentException("Moving an empty pit")

        var currentPitIdx = pit
        board.pits[currentPitIdx].set(0)

        while (stones > 0) {
            currentPitIdx += 1

            if (currentPitIdx == 6) {
                if (board.player == player) {
                    board.home.addAndGet(1)
                    stones -= 1
                    if (stones == 0) return true
                }
                board = switchBoards(player)
                currentPitIdx = -1
            } else {
                board.pits[currentPitIdx].getAndAdd(1)
                stones -= 1
            }
        }
        val currentPit = board.pits[currentPitIdx]

        if (board.player == player && currentPit.get() == 1) {
            val enemyBoard = switchBoards(board)
            val enemyPit = enemyBoard.pits[5 - currentPitIdx]

            board.home.addAndGet(currentPit.get())
            board.home.addAndGet(enemyPit.get())
            currentPit.set(0)
            enemyPit.set(0)
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

class PlayerPerspective(private val player: Int, private val gameBoard: GameBoard) {
    val ownBoard: PlayerBoard
        get() = gameBoard.players[player]

    val ownPits: Array<AtomicInteger>
        get() = ownBoard.pits

    var ownHome: AtomicInteger
        get() = ownBoard.home
        set(value) {
            ownBoard.home = value
        }

    val enemyBoard: PlayerBoard
        get() = gameBoard.players[Game.switchPlayer(player)]

    val enemyPits: Array<AtomicInteger>
        get() = enemyBoard.pits

    var enemyHome: AtomicInteger
        get() = enemyBoard.home
        set(value) {
            enemyBoard.home = value
        }
}

class PlayerBoard(val player: PlayerId) {
    var home: AtomicInteger = AtomicInteger(0)
    val pits: Array<AtomicInteger> = Array(6) { AtomicInteger(4) }

    val hasStones: Boolean
        get() = pits.sumInt() > 0

    override fun toString(): String {
        return "de.chasenet.macala.ui.PlayerBoard(player=$player, home=$home, pits=${pits.contentToString()})"
    }

    fun moveAllToHome() {
        home = pits.sum()
        pits.forEach { it.set(0) }
    }
}

fun Array<AtomicInteger>.sum(): AtomicInteger = AtomicInteger(sumInt())
fun Array<AtomicInteger>.sumInt(): Int = sumOf { it.get() }