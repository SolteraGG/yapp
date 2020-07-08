package com.dumbdogdiner.yapp.utils

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Server
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * Generic util methods.
 */
object Utils {
    /**
     * Is this material a pressure plate? I don't know, ask this function!~
     */
    fun isPressurePlate(mat: Material): Boolean {
        return when (mat) {
            Material.ACACIA_PRESSURE_PLATE -> true
            Material.BIRCH_PRESSURE_PLATE -> true
            Material.DARK_OAK_PRESSURE_PLATE -> true
            Material.HEAVY_WEIGHTED_PRESSURE_PLATE -> true
            Material.JUNGLE_PRESSURE_PLATE -> true
            Material.LIGHT_WEIGHTED_PRESSURE_PLATE -> true
            Material.OAK_PRESSURE_PLATE -> true
            Material.SPRUCE_PRESSURE_PLATE -> true
            Material.STONE_PRESSURE_PLATE -> true
            else -> false
        }
    }

    /**
     * Convert bukkit colour codes into their true values.
     */
    fun colorize(string: String) = ChatColor.translateAlternateColorCodes('&', string)

    /**
     * Convert a list of strings containing bukkit colour codes into their true values.
     */
    fun colorize(list: List<String>) = list.map { colorize(it); }

    private val server: Server
        get() {
            return Bukkit.getServer()
        }

    private const val consolePrefix = "&dyapp &r&8» &r"
    private const val errorPrefix = "&dyapp &eERROR &r&8» &r"

    fun log(message: String) = server.consoleSender.sendMessage(colorize("$consolePrefix$message"))
    fun log(message: Throwable) = server.consoleSender.sendMessage(colorize("$errorPrefix$message"))

    /**
     * Utility function for creating item stacks with metadata.
     */
    fun createItemStack(mat: Material, meta: (m: ItemMeta) -> ItemMeta): ItemStack {
        val item = ItemStack(mat)
        item.itemMeta = meta(item.itemMeta)
        return item
    }

    /**
     * Round a double to n decimal places.
     */
    fun round(v: Double, n: Int): Double {
        var multiplier = 1.0
        repeat(n) {
            multiplier *= 10
        }
        return kotlin.math.round(v * multiplier) / multiplier
    }

    /**
     * Serialize a location into  astring.
     */
    fun serializeLocation(loc: Location): String {
        return "${loc.world.name}:${loc.x}:${loc.y}:${loc.z}"
    }

    /**
     * Deserialize a string into a location.
     */
    fun deserializeLocation(raw: String): Location? {
        val delimited = raw.split(":")

        if (delimited.size != 4) {
            log("Failed to deserilize location '$raw' - invalid length.")
            return null
        }

        val world = Bukkit.getWorld(delimited[0])
        if (world == null) {
            log("Failed to deserilize location '$raw' - world '${delimited[0]}' does not exist.")
            return null
        }

        return Location(world, delimited[1].toDouble(), delimited[2].toDouble(), delimited[3].toDouble())
    }
}
