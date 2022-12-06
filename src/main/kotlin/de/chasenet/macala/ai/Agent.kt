package de.chasenet.macala.ai

import de.chasenet.macala.Game
import de.chasenet.macala.PlayerEvents

abstract class Agent(
    private val game: Game,
    name: String
) : PlayerEvents {
    val name = "${javaClass.simpleName}-$name"
    val player = game.registerPlayer(this, name)

    protected val ownPitsIndexed: Map<Int, Int>
        get() = player.boardPerspective.ownPits.mapIndexed { idx, pit -> idx to pit.get() }.toMap()

    protected val ownNonEmptyPits: Map<Int, Int>
        get() = ownPitsIndexed.filter { it.value > 0 }

    override fun onTurn(game: Game) = doTurn(game)

    abstract fun doTurn(game: Game)

    fun move(pit: Int) {
        game.move(player.playerId, pit)
    }
}

class RandomAgent(game: Game, name: String) : Agent(game, name) {

    override fun doTurn(game: Game) {
        move(ownNonEmptyPits.toList().random().first)
    }
}

class MinAgent(game: Game, name: String) : Agent(game, name) {
    override fun doTurn(game: Game) {
        val minPit = ownNonEmptyPits.map { it.value }.minByOrNull { it }!!

        move(ownNonEmptyPits.toList().filter { it.second == minPit }.random().first)
    }
}

class MaxAgent(game: Game, name: String) : Agent(game, name) {
    override fun doTurn(game: Game) {
        val minPit = ownNonEmptyPits.map { it.value }.maxByOrNull { it }!!

        move(ownNonEmptyPits.toList().filter { it.second == minPit }.random().first)
    }
}