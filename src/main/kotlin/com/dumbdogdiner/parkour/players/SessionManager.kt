package com.dumbdogdiner.parkour.players

import com.dumbdogdiner.parkour.ParkourPlugin
import com.dumbdogdiner.parkour.courses.Course
import org.bukkit.entity.Player

class SessionManager(private val plugin: ParkourPlugin) {
    private val sessions = HashMap<Player, Session>()
    private val editingSessions = HashMap<Player, EditingSession>()

    public val storage = SessionStorage()

    /**
     * Create a new player session.
     */
    fun createSession(player: Player, course: Course) {
        if (isPlayerInSession(player)) {
            endSession(player, false)
        }

        if (isPlayerInEditingSession(player)) {
            return
        }

        val session = Session(this, player, course)
        sessions[player] = session
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
    fun endEditingSession(player: Player) {
        getEditingSession(player)?.end()
        editingSessions.remove(player)
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