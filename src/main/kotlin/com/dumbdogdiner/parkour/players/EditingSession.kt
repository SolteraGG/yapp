package com.dumbdogdiner.parkour.players

import com.dumbdogdiner.parkour.courses.Course
import com.dumbdogdiner.parkour.utils.Language
import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack

/**
 * A player's editing session.
 *
 * TODO: Check for bad people trying to create parkours across multiple worlds >:C
 */
class EditingSession(private var player: Player, private var course: Course, private var type: EditingSession.Type) {
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
     * Handle a checkpoint clicked event.
     */
    fun handleCheckpointClicked(loc: Location) {
        when (type) {
            Type.CREATE -> addCheckpoint(loc)
            Type.DELETE -> course.removeCheckpoint(loc)
            Type.MODIFY -> {}
        }
    }

    fun addCheckpoint(loc: Location) {
        val checkpoint: Location? = course.getCheckpoints()[Utils.makeShortCoords(loc)]
        if (checkpoint != null) {
            player.sendMessage(Language.checkpointExists)
            return
        }

        course.addCheckpoint(loc)
        player.sendMessage(Language.checkpointCreated)
    }

    /**
     * Get the course being modified.
     */
    fun getCourse(): Course {
        return course
    }

    /**
     * End this editing session.
     */
    fun end() {
        player.inventory.remove(editorTool)
        course.save()
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