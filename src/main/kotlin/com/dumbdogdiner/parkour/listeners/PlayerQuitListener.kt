package com.dumbdogdiner.parkour.listeners

import com.dumbdogdiner.parkour.Base

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener : Listener, Base {
    @EventHandler
    fun onJoin(e: PlayerQuitEvent) {
        if (plugin.sessionManager.isPlayerInSession(e.player)) {
            plugin.sessionManager.endSession(e.player, false)
        } else if (plugin.editingSessionManager.isPlayerInEditingSession((e.player))) {
            plugin.editingSessionManager.endEditingSession(e.player)
        }
    }
}