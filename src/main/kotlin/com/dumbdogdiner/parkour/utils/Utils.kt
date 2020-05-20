package com.dumbdogdiner.parkour.utils

import org.bukkit.*

object Utils {
    fun makeShortCoords(world: String, x: Int, y: Int, z: Int): String {
        return String.format("%s:%d:%d:%d", world, x, y, z)
    }

    fun makeShortCoords(loc: Location): String {
        return makeShortCoords(loc.world.name, loc.blockX, loc.blockY, loc.blockZ)
    }

    /**
     * Convert a short coords back into a location
     */
    fun makeLocation(coords: String): Location {
        val parsed = coords.split(":").map { x -> x.toDouble() }
        return Location(Bukkit.getServer().getWorld("world"), parsed[0] - 0.5, parsed[1] - 0.5, parsed[2] - 0.5)
    }

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

    fun colorize(string: String) = ChatColor.translateAlternateColorCodes('&', string)

    private val server: Server
        get() {
            return Bukkit.getServer()
        }

    private const val consolePrefix = "&d[Parkour] &r"
    private const val errorPrefix = "&e[Parkour Error] &r"

    fun log(message: String) = server.consoleSender.sendMessage("$consolePrefix${colorize(message)}")
    fun log(message: Throwable) = server.consoleSender.sendMessage("$errorPrefix$message")
}
