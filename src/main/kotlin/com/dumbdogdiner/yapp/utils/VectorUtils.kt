package com.dumbdogdiner.yapp.utils

import kotlin.math.cos
import kotlin.math.sin
import org.bukkit.Location
import org.bukkit.util.Vector

/**
 * Utility methods for dealing with vectors.
 */
object VectorUtils {
    /**
     * Convert a pitch and yaw into a vector.
     */
    fun fromPitchYaw(pitch: Float, yaw: Float): Vector {
        return Vector(cos(pitch) * cos(yaw), sin(pitch) * cos(yaw), sin(yaw))
    }

    /**
     * Convert a location into a rotation vector.
     */
    fun rotFromLoc(loc: Location): Vector {
        return fromPitchYaw(loc.pitch, loc.yaw)
    }

    /**
     * Serialize a vector into a string.
     */
    fun serialize(vector: Vector): String {
        return "${vector.x}:${vector.y}${vector.z}"
    }

    /**
     * Deserialize a string into a vector.
     */
    fun deserialize(raw: String): Vector? {
        val delimited = raw.split(":")

        if (delimited.size != 3) {
            Utils.log("Failed to deserilize vector '$raw' - invalid length.")
            return null
        }

        return Vector(delimited[0].toDouble(), delimited[1].toDouble(), delimited[2].toDouble())
    }
}
