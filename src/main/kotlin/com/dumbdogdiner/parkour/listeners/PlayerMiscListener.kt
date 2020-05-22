package com.dumbdogdiner.parkour.listeners

import com.dumbdogdiner.parkour.Base
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent

/**
 * Miscellaneous listeners to stop things from breaking.
 */
class PlayerMiscListener : Listener, Base {

    /**
     * Stop silly boys from messing everything up by somehow dying during editing.
     */
    @EventHandler
    fun onPlayerDeath(e: PlayerDeathEvent) {
        if (sessionManager.isPlayerInSession(e.entity)) {
            sessionManager.endSession(e.entity, false)
        }

        val session = editingSessionManager.getEditingSession(e.entity) ?: return
        session.handleEditorDeath(e)
    }

    /**
     * Prevent silly boys from taking fall damage while on a parkour course.
     */
    @EventHandler
    fun onFallDamage(e: EntityDamageEvent) {
        if (e.entity !is Player || e.cause != EntityDamageEvent.DamageCause.FALL) {
            return
        }

        if (
            sessionManager.getSession(e.entity as Player) != null ||
            editingSessionManager.getEditingSession(e.entity as Player) != null
        ) {
            e.isCancelled = true
            return
        }
    }

    /**
     * Prevent naughty boys (like me uwu~) from breaking checkpoint markers.
     */
    fun onBreakBlock(e: BlockBreakEvent) {}
}
