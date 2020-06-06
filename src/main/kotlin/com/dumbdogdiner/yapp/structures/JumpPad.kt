package com.dumbdogdiner.yapp.structures

import com.dumbdogdiner.yapp.utils.Utils
import com.dumbdogdiner.yapp.utils.VectorUtils
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.util.Vector

/**
 * A pad that launches a player into the air.
 */
class JumpPad(
    var location: Location,
    private var direction: Vector,
    private val magnitude: Int
) : Pad(location, particle) {
    override val type = Pad.Type.JUMP

    init {
        // Make it behave as expected.
        direction.normalize()
    }

    /**
     * Apply a velocity to the player in the specified direction.
     */
    override fun trigger(player: Player) {
        player.velocity.add(direction.multiply(magnitude))
    }

    /**
     * Serialize a jump pad.
     */
    override fun serialize(config: FileConfiguration, path: String) {
        val section = config.getConfigurationSection(path) ?: config.createSection(path)

        section.set("type", Pad.Type.JUMP)

        section.set("location", Utils.serializeLocation(location))
        section.set("direction", VectorUtils.serialize(direction))
        section.set("magnitude", magnitude)
    }

    companion object {
        /**
         * The particle effect of this pad.
         */
        val particle = Particle.VILLAGER_HAPPY

        /**
         * Deserialize a jump pad.
         * TODO: Better to just let FileConfiguration serialize the classes?
         */
        fun deserialize(config: FileConfiguration, path: String): JumpPad? {
            val section = config.getConfigurationSection(path) ?: return null

            val loc = Utils.deserializeLocation(section.getString("location") ?: return null) ?: return null
            val direction = VectorUtils.deserialize(section.getString("direction") ?: return null) ?: return null
            val magnitude = section.getInt("magnitude", 1)

            return JumpPad(loc, direction, magnitude)
        }
    }
}
