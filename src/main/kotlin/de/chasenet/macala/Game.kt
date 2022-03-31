package de.chasenet.macala

class Game(
    var gameState: GameState = GameState.STARTING
) {
    private val gameBoard = GameBoard()
    private var activePlayer = 0

    val players: MutableList<Player> = ArrayList(2)

    fun registerPlayer(events: PlayerEvents, name: String): Player {
        if (gameState != GameState.STARTING) throw IllegalStateException("Cannot change players in a running game")
        val playerId = players.size

        if (playerId > 1) throw IllegalStateException("Too many players")
        val player = Player(playerId, name, PlayerPerspective(playerId, gameBoard), events)
        players.add(player)
        return player
    }

    fun startGame() {
        if (gameState != GameState.STARTING) {
            throw IllegalStateException("Game is already started")
        }
        players.forEach { it.onGameStart(this, players) }
        gameState = GameState.RUNNING
        players[0].onTurn(this)
    }

    fun observe(player: Int, events: PlayerEvents): Player {
        players[player] = players[player].copy(observers = players[player].observers + events)
        return players[player]
    }

    fun move(player: Int, pit: Int) {
        activePlayer = if (gameBoard.move(PlayerId(player), pit)) player else switchPlayer(player)

        players.forEach { it.onBoardUpdated(this) }
        startTurn(activePlayer)
    }

    private fun startTurn(player: Int) {
        if (gameBoard.hasPlayerStones(PlayerId(player))) {
            players[player].onTurn(this)
        } else {
            gameState = GameState.ENDED
            gameBoard.players.forEach { it.moveAllToHome() }


            val homes = players.map { it.playerId to it.boardPerspective.ownHome.get() }
            val winningPlayer =
                if (homes[0].second == homes[1].second) null
                else homes.maxByOrNull { it.second }!!.first

            players.forEach {
                it.onGameEnd(
                    this,
                    winningPlayer
                )
            }
        }
    }

    enum class GameState {
        STARTING, RUNNING, ENDED
    }

    companion object {
        fun switchPlayer(player: Int): Int = if (player == 0) 1 else 0
    }
}