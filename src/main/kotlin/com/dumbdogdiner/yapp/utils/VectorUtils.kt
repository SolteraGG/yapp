package com.dumbdogdiner.yapp.utils

import org.bukkit.Location
import org.bukkit.util.Vector
import kotlin.math.cos
import kotlin.math.sin

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
}