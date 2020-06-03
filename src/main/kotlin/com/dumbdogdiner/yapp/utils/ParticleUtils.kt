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
    fun drawBoundingBox(world: World, edges: List<Edge>) {
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

    /**
     * Return the edges of a bounding box.
     */
    fun getBoundingBoxEdges(world: World, bounds: BoundingBox): List<Edge> {
        // This feels like a really dumb way of doing it.
        val verts = mutableListOf<Location>()

        for (i in 0..7) {
            verts.add(Location(
                world,
                if (i and 1 == 0) bounds.maxX else bounds.minX,
                if (i and 2 == 0) bounds.maxY else bounds.minY,
                if (i and 4 == 0) bounds.maxZ else bounds.minZ
            ))
        }

        val edges = mutableListOf<Edge>()

        // Edges in X axis.
        for (i in 0..3) {
            edges.add(Edge(verts[i], verts[i + 4]))
        }

        // Edges in Y axis.
        for (i in 0..1) {
            edges.add(Edge(verts[i], verts[i + 2]))
        }
        for (i in 4..5) {
            edges.add(Edge(verts[i], verts[i + 2]))
        }

        // Edges in Z axis.
        for (i in 0..3) {
            edges.add(Edge(verts[2 * i], verts[2 * i + 1]))
        }

        return edges
    }

    /**
     * Utility vector class for storing start and end locations.
     */
    class Edge(val start: Location, val end: Location) {}
}