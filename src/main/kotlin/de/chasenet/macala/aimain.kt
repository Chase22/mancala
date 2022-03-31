package de.chasenet.macala

import java.util.concurrent.*

fun main() {
    TestGame(::AI, ::AI).play(10000).run {
        println("Agent1: $agent1Wins, $ratioAgent1%")
        println("Agent2: $agent2Wins, $ratioAgent2%")
        println("Ties: $ties, $ratioTies%")
    }
}

class TestGame(private val agent1: (Game, String) -> PlayerEvents, private val agent2: (Game, String) -> PlayerEvents) {
    private val executor: ExecutorService = Executors.newFixedThreadPool(50)

    data class Result(
        val agent1Wins: Int,
        val agent2Wins: Int,
        val ties: Int,
    ) {
        val gamesPlayed: Int = agent1Wins + agent2Wins + ties
        val ratioAgent1: Double = (agent1Wins.toDouble() / gamesPlayed) * 100
        val ratioAgent2: Double = (agent2Wins.toDouble() / gamesPlayed) * 100
        val ratioTies: Double = (ties.toDouble() / gamesPlayed) * 100
    }

    private fun playGame(): CompletableFuture<Int> {
        val future = CompletableFuture<Int>()

        executor.execute {
            Game().apply {
                agent1(this, "Agent1")
                agent2(this, "Agent2")
                observe(0, object : PlayerEvents {
                    override fun onGameEnd(game: Game, winningPlayer: Int?) {
                        future.complete(winningPlayer?.let { it + 1 } ?: 0)
                    }
                })
                startGame()
            }
        }
        return future
    }

    fun play(plays: Int): Result {
        return Array(plays) { playGame() }
            .map { it.join() }
            .groupingBy { it }.eachCount()
            .let {
                Result(
                    it[1] ?: 0,
                    it[2] ?: 0,
                    it[0] ?: 0
                )
            }.also {
                executor.shutdownNow()
            }
    }

}

class AI(game: Game, name: String) : PlayerEvents {
    private val player = game.registerPlayer(this, name)

    override fun onTurn(game: Game) {
        val randomPit =
            player.boardPerspective.ownPits
                .mapIndexed { index, value -> index to value.get() }
                .filter { it.second > 0 }
                .random().first

        game.move(player.playerId, randomPit)
    }
}