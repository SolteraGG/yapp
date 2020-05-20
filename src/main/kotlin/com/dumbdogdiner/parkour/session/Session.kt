package com.dumbdogdiner.parkour.session

import com.dumbdogdiner.parkour.courses.Course
import com.dumbdogdiner.parkour.utils.Language
import com.dumbdogdiner.parkour.utils.Utils

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

class Session(private val manager: SessionManager, private val player: Player, private val course: Course) {
    private var previousCheckpointId = 0
    private var nextCheckpointId = 1

    private val previousCheckpoint
        get() = course.getCheckpoints()[previousCheckpointId]

    private val nextCheckpoint
        get() = course.getCheckpoints()[nextCheckpointId]

    private val startedAt = System.currentTimeMillis()

    init {
        player.inventory.addItem(returnItem.clone())
        player.inventory.addItem(resetItem.clone())
        player.inventory.addItem(exitItem.clone())
    }

    /**
     * Advance the player onto the next checkpoint.
     */
    private fun nextCheckpoint() {
        previousCheckpointId++
        nextCheckpointId++

        if (nextCheckpointId == course.getCheckpoints().size) {
            finish()
        }
    }

    fun revertToLastCheckpoint() {
        player.teleport(previousCheckpoint)
    }

    fun end(returnToStart: Boolean) {
        if (returnToStart) {
            player.teleport(course.getCheckpoints().first())
        }

        player.inventory.remove(returnItem)
        player.inventory.remove(resetItem)
        player.inventory.remove(exitItem)
    }

    private fun finish() {
        end(true)

        val session = StoredSession()
        session.course = course
        session.player = player
        session.time = ((System.currentTimeMillis() - startedAt) / 1000).toDouble()

        val previous = manager.storage.getPlayerBest(player, course)

        if (previous < session.time) {
            player.sendMessage(Language.newBestTime)
            manager.storage.storePlayerSession(session)
        }
    }

    /**
     * Handle the player stepping on a checkpoint pressure plate.
     */
    fun handleCheckpoint(e: PlayerInteractEvent) {
        val block = e.clickedBlock ?: return

        if (nextCheckpoint != block.location) {
            return
        }

        nextCheckpoint()
    }

    /**
     * Reset the editor tool.
     */
    fun handleDropEvent(e: PlayerDropItemEvent) {
        controls.forEach {
            if (
                it.type == e.itemDrop.itemStack.type &&
                it.itemMeta.displayName == e.itemDrop.itemStack.itemMeta.displayName
            ) {
                e.isCancelled = true
            }
        }
    }

    companion object {
        val returnItem = Utils.createItemStack(Material.EMERALD_BLOCK) {
            it.setDisplayName("&aReset");
            it.lore = Utils.colorize(listOf("Right click to return to the previous checkpoint.", "&cYour elapsed time will not reset."));
            it
        }
        val resetItem = Utils.createItemStack(Material.GOLD_BLOCK) {
            it.setDisplayName("&aRestart");
            it.lore = Utils.colorize(listOf("Right click to return to the start of the course.", "Your elapsed time &awill &rbe reset."));
            it
        }
        val exitItem = Utils.createItemStack(Material.REDSTONE_BLOCK) {
            it.setDisplayName("&aExit");
            it.lore = Utils.colorize(listOf("Right click to exit this course.", "&cYour elapsed time will not reset."));
            it
        }
        val controls = listOf(returnItem, resetItem, exitItem)
    }
}
