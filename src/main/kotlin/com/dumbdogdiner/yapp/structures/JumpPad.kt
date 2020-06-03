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
        var direction: Vector,
        val magnitude: Int
) : Pad(location, particle) {
    /**
     * Apply a velocity to the player in the specified direction.
     */
    override fun trigger(player: Player) {
        player.velocity.add(direction.multiply(magnitude))
    }

    companion object {
        /**
         * The particle effect of this pad.
         */
        val particle = Particle.VILLAGER_HAPPY
    }
}