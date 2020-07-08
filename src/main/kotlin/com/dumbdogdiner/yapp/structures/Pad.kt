package com.dumbdogdiner.yapp.structures

import com.dumbdogdiner.yapp.Base
import com.okkero.skedule.BukkitDispatcher
import kotlin.collections.HashMap
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

/**
 * Represents a pressure plate that the player can step on, causing something interesting to happen owo~
 */
abstract class Pad(private val location: Location, private val particle: Particle) : Base {

    /** The type of the pad. */
    enum class Type {
        JUMP,
        EFFECT,
        MARKER
    }

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
     * The type of the pad.
     */
    abstract val type: Type

    /**sc
     * Called when the player steps on the pressure plate.
     */
    abstract fun trigger(player: Player)

    abstract fun serialize(config: FileConfiguration, path: String)

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
     */
    private fun startParticles() {
        if (config.getBoolean("disablePadParticles")) {
            return
        }

        isActive = true
        job = GlobalScope.launch(BukkitDispatcher(plugin)) {
            while (isActive) {
                location.world.spawnParticle(particle, location.clone(), 20)
                delay(1000)
            }
        }
    }

    /**
     * Stop the coroutine spawning particles.
     */
    private fun stopParticles() {
        if (config.getBoolean("disablePadParticles")) {
            return
        }

        isActive = false
        job.cancel()
    }
}
