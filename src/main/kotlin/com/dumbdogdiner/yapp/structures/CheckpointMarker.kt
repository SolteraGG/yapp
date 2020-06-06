package com.dumbdogdiner.yapp.structures

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.entity.Player

/**
 * A marker that shows players where the next checkpoint is.
 */
class CheckpointMarker(loc: Location) : Pad(loc, Particle.PORTAL) {
    override fun trigger(player: Player) {}
}
