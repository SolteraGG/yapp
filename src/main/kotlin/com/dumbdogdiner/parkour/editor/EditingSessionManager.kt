package com.dumbdogdiner.parkour.editor

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.courses.Course
import org.bukkit.entity.Player

class EditingSessionManager : Base {
    /**
     * Currently active editing sessions.
     */
    private val editingSessions = HashMap<Player, EditingSession>()

    /**
     * Create an editing session of a given type.
     */
    fun createEditingSession(player: Player, course: Course, type: EditingSession.Type): EditingSession {
        if (isPlayerInEditingSession(player)) {
            endEditingSession(player)
        }

        if (sessionManager.isPlayerInSession(player)) {
            sessionManager.endSession(player, false)
        }

        val editingSession = EditingSession(player, course, type)
        editingSessions[player] = editingSession

        logger.info("Created editing session for player '${player.uniqueId}'.")
        return editingSession
    }

    /**
     * Get a player's current editing session.
     */
    fun getEditingSession(player: Player): EditingSession? {
        return editingSessions[player]
    }

    /**
     * End a player's current editing session.
     */
    fun endEditingSession(player: Player, dropProgress: Boolean = false) {
        val session: EditingSession = getEditingSession(player) ?: return
        endEditingSession(session, dropProgress)
    }

    /**
     * End a editor session.
     */
    fun endEditingSession(session: EditingSession, dropProgress: Boolean = false) {
        val course = session.end(dropProgress)
        editingSessions.remove(session.player)

        if (!dropProgress) {
            plugin.courseManager.addCourse(course)
        }

        logger.info("Ended editing session for player '${session.player.uniqueId}'.")
    }

    /**
     * Check if a player is currently in an editing session.
     */
    fun isPlayerInEditingSession(player: Player): Boolean {
        if (getEditingSession(player) != null) {
            return true
        }
        return false
    }
}