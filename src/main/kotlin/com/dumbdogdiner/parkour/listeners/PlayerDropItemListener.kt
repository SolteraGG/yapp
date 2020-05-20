package com.dumbdogdiner.parkour.listeners

import com.dumbdogdiner.parkour.Base

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

/**
 * Prevent players from dropping editor tools, or session controls.
 */
class PlayerDropItemListener : Listener, Base {
    @EventHandler
    fun onPlayerItemDrop(e: PlayerDropItemEvent) {
        if (sessionManager.isPlayerInSession(e.player)) {
            sessionManager.getSession(e.player)?.handleDropEvent(e)
        } else if (editingSessionManager.isPlayerInEditingSession(e.player)) {
            editingSessionManager.getEditingSession(e.player)?.handleDropEvent(e)
        }
    }
}
