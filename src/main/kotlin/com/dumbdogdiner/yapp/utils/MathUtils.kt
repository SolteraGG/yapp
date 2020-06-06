package com.dumbdogdiner.yapp.utils

/**
 * Utility methods to deal with MATH OWO
 */
object MathUtils {
    /**
     * Find the parameter t that would give the linear interpolation result
     * v between a and b.
     */
    fun inverseLerp(a: Float, b: Float, v: Float): Float {
        return (v - a) / (b - a)
    }
}
