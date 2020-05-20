package com.dumbdogdiner.parkour.players

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.ParkourPlugin
import com.dumbdogdiner.parkour.courses.Course
import org.bukkit.entity.Player

class SessionManager() : Base {
    private val sessions = HashMap<Player, Session>()
    private val editingSessions = HashMap<Player, EditingSession>()

    public val storage = SessionStorage()

    /**
     * Create a new player session.
     */
    fun createSession(player: Player, course: Course): Session? {
        if (isPlayerInSession(player)) {
            endSession(player, false)
        }

        if (isPlayerInEditingSession(player)) {
            return null
        }

        val session = Session(this, player, course)
        sessions[player] = session

        logger.info("Created editing session for player '${player.uniqueId}'.")
        return session
    }

    /**
     * Get a player's current session.
     */
    fun getSession(player: Player): Session? {
        return sessions[player]
    }

    /**
     * End a player's current session.
     */
    fun endSession(player: Player, returnToStart: Boolean) {
        getSession(player)?.end(returnToStart)
        sessions.remove(player)
        logger.info("Ended session for player '${player.uniqueId}'.")
    }

    /**
     * Check if the player is currently in a session.
     */
    fun isPlayerInSession(player: Player): Boolean {
        if (getSession(player) != null) {
            return true
        }
        return false
    }

    /**
     * Create an editing session of a given type.
     */
    fun createEditingSession(player: Player, course: Course, type: EditingSession.Type): EditingSession {
        if (isPlayerInEditingSession(player)) {
            endEditingSession(player)
        }

        if (isPlayerInSession(player)) {
            endSession(player, false)
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
        getEditingSession(player)?.end()
        editingSessions.remove(player)
        logger.info("Ended editing session for player '${player.uniqueId}'.")
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
