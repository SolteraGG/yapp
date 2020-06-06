package com.dumbdogdiner.yapp.listeners

import com.dumbdogdiner.yapp.Base
import com.dumbdogdiner.yapp.utils.Utils
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockRedstoneEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent

class PlayerSessionListener : Listener, Base {
    /**
     * Handle a player stepping on a pressure plate while they are in a parkour session.
     */
    @EventHandler
    fun onHandleCheckpoint(e: PlayerInteractEvent) {
        val block = e.clickedBlock ?: return
        if (e.action != Action.PHYSICAL || !Utils.isPressurePlate(block.type)) { return }
        sessionManager.handleCheckpointInteraction(e)
    }

    /**
     * Listen for redstone events on pressure plates.
     */
    @EventHandler
    fun onHandleCheckpointRedstoneUpdate(e: BlockRedstoneEvent) {
        if (!Utils.isPressurePlate(e.block.type)) { return }
        sessionManager.handleCheckpointRedstoneUpdate(e)
    }

    /**
     * Prevent the player from flying during courses.
     * TODO: Check if this is drastically slow. Edit: seems ok, might jump to coroutines at some point.
     */
    @EventHandler
    fun onPlayerMove(e: PlayerMoveEvent) {
        if (e.player.isFlying && sessionManager.isPlayerInSession(e.player)) {
            sessionManager.endSession(e.player, returnToStart = false, escapeRecord = true)
        }
    }
}
