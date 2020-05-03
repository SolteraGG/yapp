package com.dumbdogdiner.parkour.listeners

import com.dumbdogdiner.parkour.ParkourPlugin
import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class PlayerInteractListener(private val plugin: ParkourPlugin) : Listener {

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        if (handleNextCheckpoint(e)) {
            return
        }

        if (handleCheckpointClicked(e)) {
            return
        }
    }

    /**
     * Handle a player stepping on a pressure plate while they are in a parkour session.
     */
    fun handleNextCheckpoint(e: PlayerInteractEvent): Boolean {
        val session = plugin.sessionManager.getSession(e.player) ?: return false
        val block = e.clickedBlock

        if (e.action != Action.PHYSICAL || block == null || Utils.isPressurePlate(block.type)) { return false }

        if (session.getNextCheckpoint() != block.location) {
            return false
        }

        session.nextCheckpoint()

        return true
    }

    /**
     * Handle a checkpoint being clicked during an editing session.
     */
    fun handleCheckpointClicked(e: PlayerInteractEvent): Boolean {
        val block = e.clickedBlock
        if (e.action != Action.RIGHT_CLICK_BLOCK || block == null || !Utils.isPressurePlate(block.type)) { return false }

        val item = e.item ?: return false
        val session = plugin.sessionManager.getEditingSession(e.player) ?: return false

        if (item.type != Material.BLAZE_ROD || item.itemMeta.displayName != Utils.colorize("&r&6&lCourse Editor")) {
            return false
        }

        session.handleCheckpointClicked(block.location)
        return true
    }
}