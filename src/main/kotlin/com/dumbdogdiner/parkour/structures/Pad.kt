package com.dumbdogdiner.parkour.structures

import com.dumbdogdiner.parkour.Base

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.collections.HashMap

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player

import com.okkero.skedule.BukkitDispatcher

/**
 * Represents a pressure plate that the player can step on, causing something interesting to happen owo~
 */
abstract class Pad(private val location: Location, private val particle: Particle) : Base {
    /**
     * Whether the pad is currently spawning particle effects.
     */
    private var isActive = false

    /**
     * The async coroutine controlling the spawning of particle effects.
     */
    private lateinit var job: Job

    /**
     * A map of players this pad should be active for.
     */
    private val players = HashMap<String, Player>()

    /**
     * Called when the player steps on the pressure plate.
     */
    abstract fun trigger(player: Player)

    /**
     * Called when a player enters the course containing this pad.
     */
    fun init(player: Player) {
        players[player.uniqueId.toString()] = player

        if (!isActive) {
            startParticles()
        }
    }

    /**
     * Called when a given player leaves the course containing this pad.
     */
    fun uninit(player: Player) {
        players.remove(player.uniqueId.toString(), player)

        if (players.size == 0 && isActive) {
            stopParticles()
        }
    }

    /**
     * Spawns a coroutine that repeatedly creates particles above the pad.
     * TODO: Add config option to disable particles.
     */
    private fun startParticles() {
        isActive = true
        job = GlobalScope.launch(BukkitDispatcher(plugin)) {
            while (isActive) {
                location.world.spawnParticle(particle, location.clone().add(0.0,0.5,0.0),20)
                delay(1000)
            }
        }
    }

    /**
     * Stop the coroutine spawning particles.
     */
    private fun stopParticles() {
        isActive = false
        job.cancel()
    }
}