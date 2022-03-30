package de.chasenet.macala

interface PlayerEvents {
    fun onGameStart(game: Game, players: List<Player>)
    fun onTurn(game: Game)
    fun onBoardUpdated(game: Game)
    fun onGameEnd(game: Game, winningPlayer: Int)
}

data class Player(
    val playerId: Int,
    val playerName: String,
    val boardPerspective: PlayerPerspective,
    val events: PlayerEvents
): PlayerEvents by events