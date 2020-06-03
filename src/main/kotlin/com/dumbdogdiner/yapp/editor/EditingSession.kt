package com.dumbdogdiner.yapp.editor

import com.dumbdogdiner.yapp.Base
import com.dumbdogdiner.yapp.courses.Checkpoint
import com.dumbdogdiner.yapp.courses.Course
import com.dumbdogdiner.yapp.utils.Language
import com.dumbdogdiner.yapp.utils.SoundUtils
import com.dumbdogdiner.yapp.utils.Utils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory

/**
 * A player's editing session.
 * TODO: are all these clones really necessary?
 */
class EditingSession(val player: Player, val course: Course, private val type: Type) : Base {
    private var boundaryCorner: Location? = null
    // Todo: private val preSessionInventory: Inventory

    enum class Type {
        CREATE,
        DELETE,
        MODIFY
    }

    init {
        player.inventory.addItem(checkpointTool.clone())
        player.inventory.addItem(boundaryTool.clone())
        player.inventory.addItem(jumpPadTool.clone())

        player.sendMessage(Language.createEditingSession)
        SoundUtils.info(player)
    }

    /**
     * End this editing session.
     */
    fun end(): Course {
        // Todo: This is slow.
        val checkpointTool = player.inventory.find { it == checkpointTool.clone()  }
        val boundaryTool = player.inventory.find { it == boundaryTool.clone()  }
        val jumpPadTool = player.inventory.find { it == jumpPadTool.clone() }

        if (checkpointTool != null) {
            player.inventory.remove(checkpointTool)
        }

        if (boundaryTool != null) {
            player.inventory.remove(boundaryTool)
        }

        if (jumpPadTool != null) {
            player.inventory.remove(jumpPadTool)
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
            e.itemDrop.itemStack != boundaryTool ||
            e.itemDrop.itemStack != jumpPadTool
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

        e.itemDrop.remove()
        editingSessionManager.endEditingSession(this, dropProgress)
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

        /**
         * Jump Pad tool - used for adding jump pads.
         */
        private val jumpPadTool = Utils.createItemStack(Material.RABBIT_FOOT) {
            it.setDisplayName(Utils.colorize("&r&aJump Pad Editor"))
            it.lore = Utils.colorize(listOf(
                    "Not quite a magical glowing stick, but it'll do.",
                    "Use this to add and remove jump pads to your course. Players who step on these pads will be launched in a specified direction with a certain velocity.",
                    "&cDrop this tool to end the editing session."
            ))
            it
        }
    }
}
