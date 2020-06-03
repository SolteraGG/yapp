package com.dumbdogdiner.yapp.session

import com.dumbdogdiner.yapp.courses.Course

import org.bukkit.OfflinePlayer

/**
 * A utility class to hold session information.
 */
class StoredSession {
    lateinit var player: OfflinePlayer
    lateinit var course: Course

    var time: Double = -1.0
}
