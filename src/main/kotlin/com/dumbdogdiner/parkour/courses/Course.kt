package com.dumbdogdiner.parkour.courses

import org.bukkit.Location

class Course(private val manager: CourseManager) {

    var id = -1
    var name = ""
    var description = ""

    // Object accesses are quicker than lists?
    private val checkpoints = mutableListOf<Location>()

    fun addCheckpoint(loc: Location) {
        checkpoints.add(loc)
        save()
    }

    /**
     * Remove a checkpoint given its index.
     */
    fun removeCheckpoint(index: Int): Location {
        val checkpoint = checkpoints.removeAt(index)

        save()
        return checkpoint
    }

    /**
     * Remove a checkpoint at a given location.
     */
    fun removeCheckpoint(loc: Location): Boolean {
        val checkpoint = findCheckpoint(loc) ?: return false
        checkpoints.remove(checkpoint)

        save()
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

    /**
     * Save this course to storage.
     */
    fun save() {
        manager.storage.saveCourse(this)
    }
}