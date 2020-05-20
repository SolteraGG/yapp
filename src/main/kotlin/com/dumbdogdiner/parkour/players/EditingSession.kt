package com.dumbdogdiner.parkour.players

import com.dumbdogdiner.parkour.Base
import com.dumbdogdiner.parkour.courses.Course
import com.dumbdogdiner.parkour.utils.Language
import com.dumbdogdiner.parkour.utils.SoundUtils
import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

/**
 * A player's editing session.
 *
 * TODO: Check for bad people trying to create parkours across multiple worlds >:C
 */
class EditingSession(private val player: Player, private val course: Course, private val type: Type) : Base {
    enum class Type {
        CREATE,
        DELETE,
        MODIFY
    }

    init {
        player.inventory.addItem(createItemTool())

        player.sendMessage(Language.createEditingSession)
        SoundUtils.info(player)
    }

    /**
     * End this editing session.
     */
    fun end() {
        val editorTool = createItemTool()

        val tool = player.inventory.find { itemStack -> itemStack == editorTool  }
        if (tool != null) {
            player.inventory.remove(tool)
        }

        course.save()

        player.sendMessage(Language.courseSaved)
        SoundUtils.success(player)
    }

    /**
     * Handle a checkpoint clicked event.
     */
    fun handleCheckpointClicked(e: PlayerInteractEvent) {
        val item = e.item ?: return
        val block = e.clickedBlock ?: return

        if (item != createItemTool()) {
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
     * Add a checkpoint to the course.
     */
    private fun addCheckpoint(loc: Location) {
        if (course.getCheckpoints().lastOrNull() != null && course.getCheckpoints().last().world != loc.world) {
            player.sendMessage(Language.badWorld)
            SoundUtils.error(player)
            return
        }

        val checkpoint: Location? = course.findCheckpoint(loc)
        if (checkpoint != null) {
            player.sendMessage(Language.checkpointExists)
            SoundUtils.error(player)
            return
        }

        course.addCheckpoint(loc)
        player.sendMessage(Language.checkpointCreated)
        SoundUtils.info(player)
    }

    /**
     * Remove a checkpoint from the course.
     */
    private fun removeCheckpoint(loc: Location) {
        val checkpoint = course.findCheckpoint(loc)

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
     * Reset the editor tool.
     */
    fun handleDropEvent(e: PlayerDropItemEvent) {
        val editorTool = createItemTool()
        if (
            e.itemDrop.itemStack != editorTool
        ) {
            return
        }

        if (course.getCheckpoints().size < 2) {
            player.sendMessage(Language.badLength)
            SoundUtils.error(player)
        }

        plugin.sessionManager.endEditingSession(player)
        e.itemDrop.remove()
    }

    companion object {
        fun createItemTool(): ItemStack {
            val editorTool = ItemStack(Material.BLAZE_ROD, 1)

            val meta = editorTool.itemMeta
            meta.setDisplayName(Utils.colorize("&r&6&lCourse Editor"))
            meta.lore = mutableListOf("A magical glowing stick! oWO!!", "Use this to create parkour courses, or return them unto the void.")

            editorTool.addUnsafeEnchantment(Enchantment.LOYALTY, 1)
            editorTool.itemMeta = meta

            return editorTool
        }
    }
}
