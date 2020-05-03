package com.dumbdogdiner.parkour.courses

import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.Location

class Course() {
    // Object accesses are quicker than lists?
    private var checkpoints = HashMap<String, Location>()
    private var checkpointArray = mutableListOf<Location>()

    fun addCheckpoint(loc: Location) {
        checkpoints[Utils.makeShortCoords(loc)] = loc
        checkpointArray.add(loc)
    }

    fun removeCheckpoint(id: Int) {
        var checkpoint = checkpointArray.removeAt(id)
        checkpoints.remove(Utils.makeShortCoords(checkpoint))
    }

    fun removeCheckpoint(loc: Location): Boolean {
        val id = getCheckpointId(loc)
        if (id != null) {
            removeCheckpoint(id)
            return true
        }
        return false
    }

    fun getCheckpoints(): HashMap<String, Location> {
        return checkpoints
    }

    fun getCheckpointId(loc: Location): Int? {
        return checkpointArray.indexOf(checkpoints[Utils.makeShortCoords(loc)])
    }

    fun getOrderedCheckpoints(): MutableList<Location> {
        return checkpointArray
    }

    fun save() {}
}