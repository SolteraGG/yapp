package com.dumbdogdiner.yapp.structures

import com.dumbdogdiner.yapp.utils.Utils
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

/**
 * A pad that applies a potion effect when stepped on.
 * TODO: Serialized locations can be manually edited to add effects not intended for use.
 */
class EffectPad(
    val location: Location,
    private val effects: MutableList<PotionEffect>
) : Pad(location, particle) {
    override val type = Pad.Type.EFFECT

    /**
     * Apply all potion effects attached to this pad to the player.
     */
    override fun trigger(player: Player) {
        // Remove effects if empty.
        if (effects.size == 0) {
            for (effect in player.activePotionEffects) {
                player.removePotionEffect(effect.type)
            }
            return
        }

        effects.map {
            player.addPotionEffect(it)
        }
    }

    /**
     * Clear all effects from this pad - this will make it clear all user effects when it
     * is triggered.
     */
    fun clearEffects() {
        effects.clear()
    }

    /**
     * Add an effect to this pad.
     *
     * If multiple effects of the same type are added,
     * the strongest effect will be added. If they are of the same strength, then
     * the longest will be added.
     */
    fun addEffect(effect: Effect, duration: Int, amplifier: Int): Boolean {
        val oldEffect = effects.find { it.type == getEffect(effect) }

        if (oldEffect == null) {
            return effects.add(PotionEffect(getEffect(effect), duration, amplifier))
        }

        // Add if amplifier is greater.
        if (amplifier > oldEffect.amplifier) {
            effects.remove(oldEffect)
            return effects.add(PotionEffect(getEffect(effect), duration, amplifier))
        }

        // Add if amplifier is equal, but duration is longer.
        if (amplifier == oldEffect.amplifier && duration > oldEffect.duration) {
            effects.remove(oldEffect)
            return effects.add(PotionEffect(getEffect(effect), duration, amplifier))
        }

        return false
    }

    /** Serialize */
    override fun serialize(config: FileConfiguration, path: String) {
        val section = config.getConfigurationSection(path) ?: config.createSection(path)

        section.set("type", Pad.Type.JUMP)
        section.set("location", Utils.serializeLocation(location))

        val effectsSection = section.createSection("effects")

        for ((i, v) in effects.withIndex()) {
            val effectSection = effectsSection.createSection("$i")
            effectSection.set("type", v.type)
            effectSection.set("duration", v.duration)
            effectSection.set("amplifier", v.amplifier)
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

        /**
         * Deserialize an effect pad.
         * TODO: Better to just let FileConfiguration serialize the classes?
         */
        fun deserialize(config: FileConfiguration, path: String): EffectPad? {
            val section = config.getConfigurationSection(path) ?: return null
            val loc = Utils.deserializeLocation(section.getString("location") ?: return null) ?: return null

            val effectsSection = section.getConfigurationSection("effects") ?: return null
            val effects = mutableListOf<PotionEffect>()

            for (k in effectsSection.getKeys(false)) {
                val type = PotionEffectType.values()[effectsSection.getInt("$k.duration", 5)]
                val duration = effectsSection.getInt("$k.duration", 5)
                val amplifier = effectsSection.getInt("$k.amplifier", 5)

                effects.add(PotionEffect(type, duration, amplifier))
            }

            return EffectPad(loc, effects)
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
