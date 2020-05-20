package com.dumbdogdiner.parkour

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitScheduler
import java.util.logging.Logger

interface Base {
    val plugin: ParkourPlugin
        get() = ParkourPlugin.instance

    val logger: Logger
        get() = plugin.logger
}
