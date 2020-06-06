package com.dumbdogdiner.yapp.session

import com.dumbdogdiner.yapp.Base
import com.dumbdogdiner.yapp.courses.Course
import com.dumbdogdiner.yapp.utils.Language
import com.dumbdogdiner.yapp.utils.SoundUtils
import com.dumbdogdiner.yapp.utils.TimerUtils
import com.dumbdogdiner.yapp.utils.Utils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent

class Session(val player: Player, val course: Course) : Base {
    private var previousCheckpointId = 0
    private var nextCheckpointId = 1

    private val previousCheckpoint
        get() = course.getCheckpoints()[previousCheckpointId]

    private val nextCheckpoint
        get() = course.getCheckpoints()[nextCheckpointId]

    private val timer = TimerUtils.createTimer(player)

    private var firstCheckpointSteppedOn = System.currentTimeMillis()

    init {
        /* TODO: Properly implement this.
        player.inventory.addItem(returnItem.clone())
        player.inventory.addItem(resetItem.clone())
        player.inventory.addItem(exitItem.clone())
        */
        player.sendMessage(Language.startCourse.replace("%COURSE%", course.name, ignoreCase = true))
        SoundUtils.info(player)
    }

    /**
     * Advance the player onto the next checkpoint.
     */
    private fun nextCheckpoint() {
        previousCheckpoint.playerDidFinish(player)
        nextCheckpoint.playerDidStart(player)

        previousCheckpointId++
        nextCheckpointId++

        // If last checkpoint, end session.
        if (nextCheckpointId == course.getCheckpoints().size) {
            return sessionManager.endSession(this, false)
        }

        player.sendMessage(Language.nextCheckpoint)
        SoundUtils.boop(player)
    }

    /**
     * Revert the player back to the last checkpoint.
     */
    fun revertToPreviousCheckpoint() {
        player.teleport(previousCheckpoint.getEndCheckpoint().clone().setDirection(player.location.direction))
    }

    /**
     * End this session.
     */
    fun end(returnToStart: Boolean): Long {
        if (returnToStart) {
            player.teleport(course.getCheckpoints().first().getEndCheckpoint().clone().setDirection(player.location.direction))
        }

        return timer.stop()
    }

    /**
     * Handle the player stepping on a checkpoint pressure plate.
     */
    fun handleCheckpoint(e: PlayerInteractEvent) {
        val block = e.clickedBlock ?: return

        if (course.getCheckpoints().first().getEndCheckpoint() == block.location) {
            // Add a delay to prevent people from spamming the first checkpoint.
            val steppedOn = firstCheckpointSteppedOn
            firstCheckpointSteppedOn = System.currentTimeMillis()

            if (System.currentTimeMillis() - steppedOn < 500) {
                return
            }

            player.sendMessage(Language.restartCourse.replace("%COURSE%", course.name, ignoreCase = true))
            SoundUtils.info(player)

            // Reset checkpoints to beginning.
            nextCheckpointId = 1
            previousCheckpointId = 0
            timer.reset()

            return
        }

        if (nextCheckpoint.getEndCheckpoint() != block.location) {
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
            it.setDisplayName("&aReset")
            it.lore = Utils.colorize(listOf("Right click to return to the previous checkpoint.", "&cYour elapsed time will not reset."))
            it
        }
        val resetItem = Utils.createItemStack(Material.GOLD_BLOCK) {
            it.setDisplayName("&aRestart")
            it.lore = Utils.colorize(listOf("Right click to return to the start of the course.", "Your elapsed time &awill &rbe reset."))
            it
        }
        val exitItem = Utils.createItemStack(Material.REDSTONE_BLOCK) {
            it.setDisplayName("&aExit")
            it.lore = Utils.colorize(listOf("Right click to exit this course.", "&cYour elapsed time will not reset."))
            it
        }
        val controls = listOf(returnItem, resetItem, exitItem)
    }
}
