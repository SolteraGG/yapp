package com.dumbdogdiner.yapp.courses

import com.dumbdogdiner.yapp.Base
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.BoundingBox

class Course : Base {
    var name = ""
    var description = ""

    // Object accesses are quicker than lists?
    private val checkpoints = mutableListOf<Checkpoint>()

    // Boundaries
    private val boundaries = mutableListOf<BoundingBox>()

    /**
     * Create and add a checkpoint to this course at the given location.
     */
    fun addCheckpointAtLocation(loc: Location): Course {
        addCheckpoint(Checkpoint(loc))
        return this
    }

    /**
     * Add a checkpoint to this course.
     */
    fun addCheckpoint(checkpoint: Checkpoint): Course {
        checkpoints.add(checkpoint)
        return this
    }

    /**
     * Remove a checkpoint given its index.
     */
    fun removeCheckpoint(index: Int): Checkpoint {
        return checkpoints.removeAt(index)
    }

    /**
     * Remove a checkpoint.
     */
    fun removeCheckpoint(checkpoint: Checkpoint): Boolean {
        return checkpoints.remove(checkpoint)
    }

    /**
     * Remove a checkpoint at a given location.
     */
    fun removeCheckpointAtLocation(loc: Location): Boolean {
        val checkpoint = findCheckpointAtLocation(loc) ?: return false
        checkpoints.remove(checkpoint)
        return false
    }

    /**
     * Find a checkpoint with the given location.
     */
    fun findCheckpointAtLocation(loc: Location): Checkpoint? {
        return checkpoints.find { it.getEndCheckpoint() == loc } ?: return null
    }

    /**
     * Return an ordered list of checkpoints.
     */
    fun getCheckpoints(): List<Checkpoint> {
        return checkpoints
    }

    /**
     * Return an ordered list of checkpoint end locations.
     */
    fun getCheckpointLocations(): List<Location> {
        return checkpoints.map { it.getEndCheckpoint() }
    }

    /**
     * Add a bounding box to this checkpoint.
     * Chainable.
     */
    fun addBoundaries(box: BoundingBox): Course {
        boundaries.add(box)
        return this
    }

    /**
     * Check whether the given player is inside the course's boundary.
     */
    fun isPlayerInBoundary(player: Player): Boolean {
        if (boundaries.size == 0) {
            return true
        }

        for (boundary in boundaries) {
            if (boundary.contains(player.location.toVector())) {
                return true
            }
        }
        return false
    }
}
