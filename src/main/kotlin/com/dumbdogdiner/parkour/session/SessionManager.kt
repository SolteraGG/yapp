package com.dumbdogdiner.parkour.session

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.courses.Course
import com.dumbdogdiner.parkour.utils.Language
import com.dumbdogdiner.parkour.utils.SoundUtils
import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.Bukkit

import org.bukkit.entity.Player

class SessionManager : Base {
    private val sessions = HashMap<Player, Session>()
    val storage = SessionStorage()

    /**
     * Create a new player session.
     */
    fun createSession(player: Player, course: Course): Session? {
        if (isPlayerInSession(player)) {
            endSession(player, returnToStart = false, escapeRecord = true)
        }

        // Prevent creation of sessions if player is in an editing session.
        if (editingSessionManager.isPlayerInEditingSession(player)) {
            return null
        }

        val session = Session(player, course)
        sessions[player] = session

        Utils.log("Created parkour session for player '${player.uniqueId}'.")
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
    fun endSession(player: Player, returnToStart: Boolean, escapeRecord: Boolean = false) {
        getSession(player)?.let { endSession(it, returnToStart, escapeRecord) }
    }

    /**
     * End the given session.
     * TODO: PlaceholderAPI
     */
    fun endSession(session: Session, returnToStart: Boolean, escapeRecord: Boolean = false) {
        session.end(returnToStart)
        sessions.remove(session.player)

        Utils.log("Ended parkour session for player '${session.player.uniqueId}'.")

        if (escapeRecord) {
            session.player.sendMessage(Language.exitSession)
            return SoundUtils.error(session.player)
        }

        val storedSession = StoredSession()
        val player = session.player
        val course = session.course

        storedSession.course = course
        storedSession.player = player
        storedSession.time = session.end(false).toDouble() / 1000

        player.sendMessage(Language.finishCourse.replace("%COURSE%", course.name, ignoreCase = true))

        val previous = sessionManager.storage.getPlayerBest(player, course)

        // Check for personal best.
        if (previous > storedSession.time) {
            sessionManager.storage.storePlayerSession(storedSession)

            // Fetch server record.
            val record = sessionManager.storage.fetchRecord(course)

            if (record > storedSession.time) {
                Bukkit.broadcastMessage(
                    Language.newRecord
                        .replace("%TIME%", storedSession.time.toString(), ignoreCase = true)
                        .replace("%COURSE%", course.name, ignoreCase = true)
                        .replace("%PLAYER%", player.name)
                )
                sessionManager.storage.storeRecordSession(storedSession)
                SoundUtils.OWO(player)

                // Broadcast sound to rest of server
                plugin.server.onlinePlayers.forEach { if (it == player) { return } else { SoundUtils.success(it) } }
            }
            // If the player has set a personal record.
            player.sendMessage(Language.newBestTime.replace("%TIME%", storedSession.time.toString(), ignoreCase = true))
            return SoundUtils.awesome(player)
        }

        // Play standard success sound if player hasn't done anything special.
        SoundUtils.success(player)
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
