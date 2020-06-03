package com.dumbdogdiner.yapp.session

import com.dumbdogdiner.yapp.Base
import com.dumbdogdiner.yapp.courses.Course
import com.dumbdogdiner.yapp.utils.Language
import com.dumbdogdiner.yapp.utils.SoundUtils
import com.dumbdogdiner.yapp.utils.Utils
import com.okkero.skedule.BukkitDispatcher
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.bukkit.Bukkit
import org.bukkit.Location

import org.bukkit.entity.Player
import org.bukkit.event.block.BlockRedstoneEvent
import org.bukkit.event.player.PlayerInteractEvent

class SessionManager : Base {
    val storage = SessionStorage()

    private val redstoneUpdates = HashMap<Location, BlockRedstoneEvent>()
    private val playerInteractions = HashMap<Location, PlayerInteractEvent>()

    private val sessions = HashMap<Player, Session>()


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

    /**
     * Handle a player checkpoint interaction.
     */
    fun handleCheckpointInteraction(e: PlayerInteractEvent) {
        val block = e.clickedBlock ?: return

        // If recieved both events, trigger.
        val redstoneUpdate = redstoneUpdates[block.location]
        if (redstoneUpdate != null) {
            return handleCombined(e, redstoneUpdate)
        }

        playerInteractions[block.location] = e

        // Remove after 1 second to prevent memory leaks
        GlobalScope.launch(BukkitDispatcher(plugin)) {
            delay(1000)
            playerInteractions.remove(block.location)
        }
    }

    /**
     * Handle a pressure plate redstone update.
     */
    fun handleCheckpointRedstoneUpdate(e: BlockRedstoneEvent) {
        // Remove when deactivated to prevent memory leaks
        if (e.newCurrent == 0) {
            redstoneUpdates.remove(e.block.location)
            return
        }

        // If the signal updates, but has already been recorded, don't do anything.
        if (redstoneUpdates.contains(e.block.location)) {
            return
        }

        // If recieved both events, trigger.
        val interaction = playerInteractions[e.block.location]
        if (interaction != null) {
            return handleCombined(interaction, e)
        }

        redstoneUpdates[e.block.location] = e
    }

    /**
     * When both the redstone update and player interaction have occured.
     */
    private fun handleCombined(interaction: PlayerInteractEvent, redstoneUpdate: BlockRedstoneEvent) {
        val player = interaction.player
        val block = redstoneUpdate.block

        // Remove from memory.
        playerInteractions.remove(interaction.clickedBlock?.location)
        redstoneUpdates.remove(block.location)

        // If the player isn't in a session, and respective block is the first checkpoint
        val course = courseManager.findCourseFromStart(block.location)
        if (course != null && !isPlayerInSession(player)) {
            createSession(player, course)
            return
        }

        // If the player is in a session
        val session = plugin.sessionManager.getSession(player) ?: return
        Utils.log("Handling checkpoint interaction for '${player.uniqueId}' at ${Utils.serializeLocation(block.location)}")
        session.handleCheckpoint(interaction)
    }
}
