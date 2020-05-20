package com.dumbdogdiner.parkour.structures

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect

/**
 * A pad that applies a potion effect when stepped on.
 */
class EffectPad(
    location: Location,
    vararg effects: PotionEffect
) : Pad(location, particle) {
    private val effects = effects

    /**
     * Apply a velocity to the player in the specified direction.
     */
    override fun trigger(player: Player) {
        effects.map {
            player.addPotionEffect(it)
        }
    }

    companion object {
        /**
         * The particle effect of this pad.
         */
        val particle = Particle.SPELL
    }
}