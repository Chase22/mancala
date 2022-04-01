package de.chasenet.macala

import de.chasenet.macala.ai.MaxAgent
import de.chasenet.macala.ai.MinAgent
import de.chasenet.macala.ai.RandomAgent

fun main() {
    val agents = listOf(::RandomAgent, ::MaxAgent, ::MinAgent)

    agents.flatMap { agent1 ->
        agents.map { agent2 -> listOf(agent1, agent2) }
    }.forEach {
        TestGame(it[0], it[1]).play(1000).run {
            println("Agent1: $agent1Wins, $ratioAgent1%")
            println("Agent2: $agent2Wins, $ratioAgent2%")
            println("Ties: $ties, $ratioTies%")
        }
    }
}