package com.dumbdogdiner.parkour.session

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.courses.Course

import org.bukkit.entity.Player

class SessionManager : Base {
    private val sessions = HashMap<Player, Session>()
    val storage = SessionStorage()

    /**
     * Create a new player session.
     */
    fun createSession(player: Player, course: Course): Session? {
        if (isPlayerInSession(player)) {
            endSession(player, false)
        }

        if (editingSessionManager.isPlayerInEditingSession(player)) {
            return null
        }

        val session = Session(this, player, course)
        sessions[player] = session

        logger.info("Created parkour session for player '${player.uniqueId}'.")
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
        logger.info("Ended parkour session for player '${player.uniqueId}'.")
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
}
