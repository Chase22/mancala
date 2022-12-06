package de.chasenet.macala

interface PlayerEvents {
    fun onGameStart(game: Game, players: List<Player>) {}
    fun onTurn(game: Game) {}
    fun onBoardUpdated(game: Game) {}
    fun onGameEnd(game: Game, winningPlayer: Int?) {}
}

data class Player(
    val playerId: Int,
    val playerName: String,
    val boardPerspective: PlayerPerspective,
    val events: PlayerEvents,
    val observers: List<PlayerEvents> = emptyList()
) : PlayerEvents {
    override fun onGameStart(game: Game, players: List<Player>) {
        observers.forEach { it.onGameStart(game, players) }
        events.onGameStart(game, players)
    }

    override fun onTurn(game: Game) {
        observers.forEach { it.onTurn(game) }
        events.onTurn(game)
    }

    override fun onBoardUpdated(game: Game) {
        observers.forEach { it.onBoardUpdated(game) }
        events.onBoardUpdated(game)
    }

    override fun onGameEnd(game: Game, winningPlayer: Int?) {
        observers.forEach { it.onGameEnd(game, winningPlayer) }
        events.onGameEnd(game, winningPlayer)
    }
}