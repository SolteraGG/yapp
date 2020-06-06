package com.dumbdogdiner.yapp.listeners

import com.dumbdogdiner.yapp.Base
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

/**
 * Listener for events related to editing sessions.
 */
class PlayerEditorSessionListener : Listener, Base {
    /**
     * Handle a checkpoint being clicked during an editing session.
     */
    @EventHandler
    private fun handleCheckpointClicked(e: PlayerInteractEvent) {
        val block = e.clickedBlock
        if (e.action != Action.RIGHT_CLICK_BLOCK || block == null) { return }

        val session = editingSessionManager.getEditingSession(e.player) ?: return

        session.handleCheckpointClicked(e)
    }

    /**
     * Handle the player dropping the editor tool.
     */
    @EventHandler
    fun onPlayerItemDrop(e: PlayerDropItemEvent) {
        if (editingSessionManager.isPlayerInEditingSession(e.player)) {
            editingSessionManager.getEditingSession(e.player)?.handleDropEvent(e)
        }
    }
}
