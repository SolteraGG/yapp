package com.dumbdogdiner.parkour.players

import com.dumbdogdiner.parkour.courses.Course
import com.dumbdogdiner.parkour.utils.Language
import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

/**
 * A player's editing session.
 *
 * TODO: Check for bad people trying to create parkours across multiple worlds >:C
 */
class EditingSession(private val player: Player, private val course: Course, private val type: Type) {
    private val editorTool: ItemStack = ItemStack(Material.BLAZE_ROD, 1)

    enum class Type {
        CREATE,
        DELETE,
        MODIFY
    }

    init {
        val meta = editorTool.itemMeta
        meta.setDisplayName(Utils.colorize("&r&6&lCourse Editor"))
        meta.lore = mutableListOf("A magical glowing stick! oWO!!", "Use this to create parkour courses, or return them unto the void.")

        editorTool.itemMeta = meta
        player.inventory.addItem(editorTool)

        player.sendMessage(Language.createEditingSession)
    }

    /**
     * End this editing session.
     */
    fun end() {
        player.inventory.remove(editorTool)
        course.save()
        player.sendMessage(Language.courseSaved)
    }

    /**
     * Handle a checkpoint clicked event.
     */
    fun handleCheckpointClicked(e: PlayerInteractEvent) {
        val item = e.item ?: return
        val block = e.clickedBlock ?: return

        if (item.type != Material.BLAZE_ROD || item.itemMeta.displayName != Utils.colorize("&r&6&lCourse Editor")) {
            return
        }

        when (type) {
            Type.CREATE -> addCheckpoint(block.location)
            Type.DELETE -> removeCheckpoint(block.location)
            Type.MODIFY -> {}
        }
    }

    /**
     * Add a checkpoint to the course.
     */
    private fun addCheckpoint(loc: Location) {
        if (course.getCheckpoints().last().world != loc.world) {
            player.sendMessage(Language.badWorld)
            return
        }

        val checkpoint: Location? = course.findCheckpoint(loc)
        if (checkpoint != null) {
            player.sendMessage(Language.checkpointExists)
            return
        }

        course.addCheckpoint(loc)
        player.sendMessage(Language.checkpointCreated)
    }

    private fun removeCheckpoint(loc: Location) {
        val checkpoint = course.findCheckpoint(loc)

        if (checkpoint == null) {
            player.sendMessage(Language.checkpointNotFound)
            return
        }

        course.removeCheckpoint(checkpoint)
        player.sendMessage(Language.checkpointRemoved)
    }


    /**
     * Reset the editor tool.
     */
    fun handleDropEvent(e: PlayerDropItemEvent) {
        if (
            editorTool.type == e.itemDrop.itemStack.type &&
            editorTool.itemMeta.displayName == e.itemDrop.itemStack.itemMeta.displayName
        ) {
            e.isCancelled = true
        }
    }
}