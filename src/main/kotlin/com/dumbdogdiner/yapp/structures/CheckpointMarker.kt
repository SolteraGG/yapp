package com.dumbdogdiner.yapp.structures

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

/**
 * A marker that shows players where the next checkpoint is.
 */
class CheckpointMarker(loc: Location) : Pad(loc, Particle.PORTAL) {
    override val type = Pad.Type.MARKER
    override fun trigger(player: Player) {}

    override fun serialize(config: FileConfiguration, path: String) {}
}
