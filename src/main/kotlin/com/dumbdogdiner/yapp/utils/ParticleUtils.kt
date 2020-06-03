package com.dumbdogdiner.yapp.utils

import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.World
import org.bukkit.util.BoundingBox
import org.bukkit.util.Vector

/**
 * Utility class for drawing particles.
 */
object ParticleUtils {
    val BOUNDING_BOX_PARTICLE = Particle.REDSTONE
    val BOUNDING_BOX_SPACING = 1

    /**
     * Draws the 12 edges of a bounding box using particles.
     */
    fun drawBoundingBox(world: World, edges: List<BoundUtils.Edge>) {
        for (edge in edges) {
            val distance: Double = edge.start.distance(edge.end)
            val start: Vector = edge.start.toVector()
            val end: Vector = edge.end.toVector()

            val vector: Vector = start.clone()
                .subtract(end)
                .normalize()
                .multiply(BOUNDING_BOX_SPACING)

            var length = 0.0
            while (length < distance) {
                world.spawnParticle(Particle.REDSTONE, start.x, start.y, start.z, 1)
                length += BOUNDING_BOX_SPACING
                end.add(vector)
            }
        }
    }
}
