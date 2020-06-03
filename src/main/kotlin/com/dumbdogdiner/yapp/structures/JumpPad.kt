package com.dumbdogdiner.yapp.structures

import kotlin.math.cos
import kotlin.math.sin

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.util.Vector

/**
 * A pad that launches a player into the air.
 */
class JumpPad(
    location: Location,
    private val direction: Vector,
    private val magnitude: Int
) : Pad(location, particle) {
    /**
     * Apply a velocity to the player in the specified direction.
     */
    override fun trigger(player: Player) {
        val v = player.velocity.clone().add(direction.multiply(magnitude))
        player.velocity = v
    }

    companion object {
        /**
         * Convert a pitch and yaw into a vector.
         */
        fun computeVector(pitch: Double, yaw: Double): Vector {
            return Vector(cos(pitch) * cos(yaw), sin(pitch) * cos(yaw), sin(yaw))
        }

        /**
         * The particle effect of this pad.
         */
        val particle = Particle.VILLAGER_HAPPY
    }
}