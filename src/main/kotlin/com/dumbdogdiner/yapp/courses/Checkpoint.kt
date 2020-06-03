package com.dumbdogdiner.yapp.courses

import com.dumbdogdiner.yapp.structures.CheckpointMarker
import com.dumbdogdiner.yapp.utils.Utils
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox

/**
 * Represents a course checkpoint
 */
class Checkpoint(private var endCheckpoint: Location) {
    private val marker = CheckpointMarker(endCheckpoint)
    private val boundaries = mutableListOf<BoundingBox>()

    /**
     * Initialize this checkpoint segment with the given player.
     * Called when the player steps on the previous checkpoint's end pressure plate.
     */
    fun playerDidStart(player: Player) {
        // Assume player is the first to enter this checkpoint - particles
        // will only trigger on the first player anyway.

        marker.init(player)
        Utils.log("Initialized particles for player '${player.uniqueId}'.")
    }

    /**
     * Deinitialize this checkpoint segment under the given player.
     * Called when the player steps on this checkpoint's end pressure plate.
     */
    fun playerDidFinish(player: Player) {
        marker.uninit(player)
    }

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