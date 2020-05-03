package com.dumbdogdiner.parkour.listeners

import com.dumbdogdiner.parkour.ParkourPlugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent

/**
 * Prevent players from dropping the tools.
 *
 * TODO: Finish editing session on drop editor tool.
 */
class PlayerDropItemListener(private var plugin: ParkourPlugin) : Listener {
    @EventHandler
    fun onPlayerItemDrop(e: PlayerDropItemEvent) {
        if (plugin.sessionManager.isPlayerInSession(e.player)) {
            plugin.sessionManager.getSession(e.player)?.handleDropEvent(e)
        } else if (plugin.sessionManager.isPlayerInEditingSession(e.player)) {
            plugin.sessionManager.getEditingSession(e.player)?.handleDropEvent(e)
        }
    }
}