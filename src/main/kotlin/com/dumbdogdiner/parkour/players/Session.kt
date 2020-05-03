package com.dumbdogdiner.parkour.players

import com.dumbdogdiner.parkour.courses.Course
import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack

class Session(private var player: Player, private var course: Course) {
    private var lastCheckpointId = 0
    private var nextCheckpointId = 1

    private var lastCheckpoint: Location = course.getOrderedCheckpoints()[lastCheckpointId]
    private var nextCheckpoint: Location = course.getOrderedCheckpoints()[nextCheckpointId]

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

    fun nextCheckpoint() {
        lastCheckpoint = nextCheckpoint
        lastCheckpointId++
        nextCheckpointId++
        nextCheckpoint = course.getOrderedCheckpoints()[nextCheckpointId]
    }

    fun getNextCheckpoint(): Location {
        return nextCheckpoint
    }

    fun revertToLastCheckpoint() {
        player.teleport(lastCheckpoint)
    }

    fun end(returnToStart: Boolean) {
        if (returnToStart) {
            player.teleport(course.getOrderedCheckpoints()[0])
        }
    }

    /**
     * Reset the editor tool.
     */
    fun handleDropEvent(e: PlayerDropItemEvent) {
        controls.forEach { stack ->
            run {
                if (
                    stack.type == e.itemDrop.itemStack.type &&
                    stack.itemMeta.displayName == e.itemDrop.itemStack.itemMeta.displayName
                ) {
                    e.isCancelled = true
                }
            }
        }
    }
}