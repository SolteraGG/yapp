package com.dumbdogdiner.parkour.editor

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.courses.Course
import com.dumbdogdiner.parkour.utils.Language
import com.dumbdogdiner.parkour.utils.SoundUtils
import com.dumbdogdiner.parkour.utils.Utils
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
            sessionManager.endSession(player, returnToStart = false, escapeRecord = true)
        }

        val editingSession = EditingSession(player, course, type)
        editingSessions[player] = editingSession

        Utils.log("Created editing session for player '${player.uniqueId}'.")
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
        val course = session.end()
        editingSessions.remove(session.player)

        if (!dropProgress) {
            plugin.courseManager.addCourse(course)
            session.player.sendMessage(Language.courseSaved)
            SoundUtils.success(session.player)
        } else {
            session.player.sendMessage(Language.exitSession)
            SoundUtils.error(session.player)
        }

        Utils.log("Ended editing session for player '${session.player.uniqueId}'.")
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