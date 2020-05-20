package com.dumbdogdiner.parkour.courses

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.structures.Pad

import org.bukkit.Location
import java.util.*

class Course : Base {

    var id: UUID = UUID.randomUUID()
    var name = ""
    var description = ""

    // Object accesses are quicker than lists?
    private val checkpoints = mutableListOf<Location>()
    private val features = mutableListOf<Pad>()

    /**
     * Add a checkpoint to this course.
     */
    fun addCheckpoint(loc: Location) {
        checkpoints.add(loc)
    }

    /**
     * Remove a checkpoint given its index.
     */
    fun removeCheckpoint(index: Int): Location {
        return checkpoints.removeAt(index)
    }

    /**
     * Remove a checkpoint at a given location.
     */
    fun removeCheckpoint(loc: Location): Boolean {
        val checkpoint = findCheckpoint(loc) ?: return false
        checkpoints.remove(checkpoint)
        return false
    }

    /**
     * Find a checkpoint with the given location.
     */
    fun findCheckpoint(loc: Location): Location? {
        return checkpoints.find { it == loc }
    }

    /**
     * Return an ordered array of checkpoints.
     */
    fun getCheckpoints(): MutableList<Location> {
        return checkpoints
    }
}
