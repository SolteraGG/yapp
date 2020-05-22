package com.dumbdogdiner.parkour.editor

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.courses.Checkpoint
import com.dumbdogdiner.parkour.courses.Course
import com.dumbdogdiner.parkour.utils.Language
import com.dumbdogdiner.parkour.utils.SoundUtils
import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

/**
 * A player's editing session.
 * TODO: are all these clones really necessary?
 */
class EditingSession(val player: Player, val course: Course, private val type: Type) : Base {
    private var boundaryCorner: Location? = null

    enum class Type {
        CREATE,
        DELETE,
        MODIFY
    }

    init {
        player.inventory.addItem(checkpointTool.clone())
        player.inventory.addItem(boundaryTool.clone())

        player.sendMessage(Language.createEditingSession)
        SoundUtils.info(player)
    }

    /**
     * End this editing session.
     */
    fun end(): Course {
        val checkpointTool = player.inventory.find { it == checkpointTool.clone()  }
        val boundaryTool = player.inventory.find { it == boundaryTool.clone()  }

        if (checkpointTool != null) {
            player.inventory.remove(checkpointTool)
        }

        if (boundaryTool != null) {
            player.inventory.remove(boundaryTool)
        }

        return course
    }

    /**
     * Handle a checkpoint clicked event.
     */
    fun handleCheckpointClicked(e: PlayerInteractEvent) {
        val item = e.item ?: return
        val block = e.clickedBlock ?: return

        if (item != checkpointTool.clone()) {
            return
        }

        if (!Utils.isPressurePlate(block.type)) {
            player.sendMessage(Language.badBlock)
            SoundUtils.error(player)
            return
        }

        when (type) {
            Type.CREATE -> addCheckpoint(block.location)
            Type.DELETE -> removeCheckpoint(block.location)
            Type.MODIFY -> {}
        }
    }

    /**
     * Handle a player clicking any other block with the boundary tool.
     */
    fun handleEverythingElseGotClicked(e: PlayerInteractEvent) {
        val item = e.item ?: return
        val block = e.clickedBlock ?: return

        if (item != boundaryTool.clone()) {
            return
        }

        if (boundaryCorner == null) {
            boundaryCorner = block.location
        }
    }

    /**
     * Add a checkpoint to the course.
     */
    private fun addCheckpoint(loc: Location) {
        if (course.getCheckpoints().lastOrNull() != null && course.getCheckpoints().last().getEndCheckpoint().world != loc.world) {
            player.sendMessage(Language.badWorld)
            SoundUtils.error(player)
            return
        }

        val checkpoint: Checkpoint? = course.findCheckpointAtLocation(loc)
        if (checkpoint != null) {
            player.sendMessage(Language.checkpointExists)
            SoundUtils.error(player)
            return
        }

        course.addCheckpointAtLocation(loc)
        player.sendMessage(Language.checkpointCreated)
        SoundUtils.info(player)
    }

    /**
     * Remove a checkpoint from the course.
     */
    private fun removeCheckpoint(loc: Location) {
        val checkpoint = course.findCheckpointAtLocation(loc)

        if (checkpoint == null) {
            player.sendMessage(Language.checkpointNotFound)
            SoundUtils.error(player)
            return
        }

        course.removeCheckpoint(checkpoint)
        player.sendMessage(Language.checkpointRemoved)
        SoundUtils.info(player)
    }

    /**
     * Exit the editor when an item is dropped.
     *
     * If the player has added less than two checkpoints i.e. no start or end point, the
     * editor will drop the current progress and inform the user it is doing such.
     */
    fun handleDropEvent(e: PlayerDropItemEvent) {
        if (
            e.itemDrop.itemStack != checkpointTool ||
            e.itemDrop.itemStack != boundaryTool
        ) {
            return
        }

        var dropProgress = false

        // If there isn't a start and an endpoint, discard progress.
        if (course.getCheckpoints().size < 2) {
            player.sendMessage(Language.badLength)
            SoundUtils.error(player)
            dropProgress = true
        }

        editingSessionManager.endEditingSession(this, dropProgress)
        e.itemDrop.remove()
    }

    /**
     * Handle the editor dying. Why this would ever happen I can't say.
     */
    fun handleEditorDeath(e: PlayerDeathEvent) {
        e.itemsToKeep.add(checkpointTool.clone())
        e.drops.remove(checkpointTool.clone())
    }

    companion object {
        /**
         * Checkpoint tool - used for creating checkpoints.
         */
        private val checkpointTool = Utils.createItemStack(Material.BLAZE_ROD) {
            it.setDisplayName(Utils.colorize("&r&6Course Editor"))
            it.lore = Utils.colorize(listOf(
                "A magical glowing stick! oWO!!",
                "Use this to create parkour courses, or return them unto the void.",
                "&cDrop this tool to end the editing session."
            ))
            it
        }

        /**
         * Boundary tool - used for adding boundaries.
         */
        private val boundaryTool = Utils.createItemStack(Material.STICK) {
            it.setDisplayName(Utils.colorize("&r&cBoundary Editor"))
            it.lore = Utils.colorize(listOf(
                "Another magical glowing stick! uWU~",
                "Use this to add boundaries to your checkpoints. Players who exit these boundaries will be teleported to the previous checkpoint.",
                "&cDrop this tool to end the editing session."
            ))
            it
        }
    }
}
