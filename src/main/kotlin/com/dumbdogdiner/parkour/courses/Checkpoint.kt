package com.dumbdogdiner.parkour.courses

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox

/**
 * Represents a course checkpoint
 */
class Checkpoint(private var endCheckpoint: Location) {
    private val boundaries = mutableListOf<BoundingBox>()

    /**
     * Add a bounding box to this checkpoint.
     * Chainable.
     */
    fun addBoundaries(box: BoundingBox): Checkpoint {
        boundaries.add(box)
        return this
    }

    /**
     * Set the end checkpoint.
     * Chainable.
     */
    fun setEndCheckpoint(loc: Location): Checkpoint {
        endCheckpoint = loc
        return this
    }

    /**
     * Get the end checkpoint.
     */
    fun getEndCheckpoint(): Location {
        return endCheckpoint
    }


    /**
     * Check whether the given player is inside the course's boundary.
     */
    fun isPlayerInBoundary(player: Player): Boolean {
        for (boundary in boundaries) {
            if (!boundary.contains(player.location.toVector())) {
                return false
            }
        }
        return true
    }
}