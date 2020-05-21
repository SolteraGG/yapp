package com.dumbdogdiner.parkour.listeners

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.Location

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockRedstoneEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

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
}
