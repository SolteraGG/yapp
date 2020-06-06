package com.dumbdogdiner.yapp.courses

import com.dumbdogdiner.yapp.Base
import org.bukkit.Location

class Course : Base {
    var name = ""
    var description = ""

    // Object accesses are quicker than lists?
    private val checkpoints = mutableListOf<Checkpoint>()

    /**
     * Create and add a checkpoint to this course at the given location.
     */
    fun addCheckpointAtLocation(loc: Location) {
        addCheckpoint(Checkpoint(loc))
    }

    /**
     * Add a checkpoint to this course.
     */
    fun addCheckpoint(checkpoint: Checkpoint) {
        checkpoints.add(checkpoint)
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
}
