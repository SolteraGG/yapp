package com.dumbdogdiner.yapp.structures

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * A pad that applies a potion effect when stepped on.
 * TODO: Add a clear effect option.
 */
class EffectPad(
    location: Location,
    val effects: MutableList<PotionEffect>
) : Pad(location, particle) {
    /**
     * Apply all potion effects attached to this pad to the player.
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

        /**
         * Convert an EffectPad.Effect into a PotionEffectType.
         */
        fun getEffect(effect: Effect): PotionEffectType {
            return when (effect) {
                Effect.SPEED -> PotionEffectType.SPEED
                Effect.JUMP -> PotionEffectType.JUMP
                Effect.SLOW -> PotionEffectType.SLOW
                Effect.SLOW_FALLING -> PotionEffectType.SLOW_FALLING
                Effect.LEVITATION -> PotionEffectType.LEVITATION
            }
        }
    }

    /**
     * An enum of potion effect values that feel sensible.
     */
    enum class Effect {
        SPEED,
        JUMP,
        SLOW,
        SLOW_FALLING,
        LEVITATION
    }
}
