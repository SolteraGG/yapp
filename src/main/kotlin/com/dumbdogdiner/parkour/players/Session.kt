package com.dumbdogdiner.parkour.players

import com.dumbdogdiner.parkour.courses.Course
import com.dumbdogdiner.parkour.utils.Language
import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class Session(private val manager: SessionManager, private val player: Player, private val course: Course) {
    private var previousCheckpointId = 0
    private var nextCheckpointId = 1

     val previousCheckpoint
        get() = course.getCheckpoints()[previousCheckpointId]

     val nextCheckpoint
        get() = course.getCheckpoints()[nextCheckpointId]

    private val startedAt = System.currentTimeMillis();

    private val returnItem = ItemStack(Material.EMERALD_BLOCK)
    private val resetItem = ItemStack(Material.GOLD_BLOCK)
    private val exitItem = ItemStack(Material.REDSTONE_BLOCK)

    private val controls = mutableListOf(returnItem, resetItem, exitItem)

    init {
        val returnItemMeta = returnItem.itemMeta
        returnItemMeta.setDisplayName(Utils.colorize("&aReturn to Checkpoint"))
        returnItem.itemMeta = returnItemMeta

        val resetItemMeta = resetItem.itemMeta
        resetItemMeta.setDisplayName(Utils.colorize("&eRestart Course"))
        resetItem.itemMeta = resetItemMeta

        val exitItemMeta = exitItem.itemMeta
        exitItemMeta.setDisplayName(Utils.colorize("&cExit Course"))
        exitItem.itemMeta = returnItemMeta

        player.inventory.addItem(returnItem)
        player.inventory.addItem(resetItem)
        player.inventory.addItem(exitItem)
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
}