package com.dumbdogdiner.parkour.listeners

import com.dumbdogdiner.parkour.Base

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

/**
 * Handle a player leaving the server during a parkour session.
 */
class PlayerQuitListener : Listener, Base {
    @EventHandler
    fun onPlayerQuit(e: PlayerQuitEvent) {
        if (plugin.sessionManager.isPlayerInSession(e.player)) {
            // Dont allow players to store records by leaving sessions.
            plugin.sessionManager.endSession(e.player, returnToStart = false, escapeRecord = true)
        } else if (plugin.editingSessionManager.isPlayerInEditingSession((e.player))) {
            // Drop editing progress on player quit.
            // TODO: This might be annoying.
            plugin.editingSessionManager.endEditingSession(e.player, dropProgress = true)
        }
    }
}