package com.dumbdogdiner.parkour.listeners

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.utils.Utils

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

/**
 * Listens for player interaction events e.g. stepping on pressure plates, right-clicking with tools.
 */
class PlayerInteractListener() : Listener, Base {

    @EventHandler
    fun onPlayerInteract(e: PlayerInteractEvent) {
        if (handleNextCheckpoint(e)) {
            return
        }

        if (handleCheckpointClicked(e)) {
            return
        }

        if (handleClickWithControls(e)) {
            return
        }
    }

    /**
     * Handle a player stepping on a pressure plate while they are in a parkour session.
     */
    private fun handleNextCheckpoint(e: PlayerInteractEvent): Boolean {
        val block = e.clickedBlock
        if (e.action != Action.PHYSICAL || block == null || Utils.isPressurePlate(block.type)) { return false }

        // If the player isn't in a session, and respective block is the first checkpoint
        val course = plugin.courseManager.findCourseFromStart(block.location)
        if (course != null) {
            plugin.sessionManager.createSession(e.player, course)
            return true
        }

        // If the player is in a session
        val session = plugin.sessionManager.getSession(e.player) ?: return false
        session.handleCheckpoint(e)

        return true
    }

    /**
     * Handle a checkpoint being clicked during an editing session.
     */
    private fun handleCheckpointClicked(e: PlayerInteractEvent): Boolean {
        val block = e.clickedBlock
        if (e.action != Action.RIGHT_CLICK_BLOCK || block == null) { return false }

        val session = editingSessionManager.getEditingSession(e.player) ?: return false

        session.handleCheckpointClicked(e)
        return true
    }

    /**
     * Handle a player clicking with the parkour controls.
     */
    private fun handleClickWithControls(e: PlayerInteractEvent): Boolean {
        if (e.action != Action.RIGHT_CLICK_BLOCK || e.action != Action.RIGHT_CLICK_AIR || !e.hasItem()) {
            return false
        }

        val session = plugin.sessionManager.getSession(e.player) ?: return false
        session.handleCheckpoint(e)

        return true
    }
}
