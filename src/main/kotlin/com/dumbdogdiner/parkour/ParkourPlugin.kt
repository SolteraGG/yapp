package com.dumbdogdiner.parkour

import com.dumbdogdiner.parkour.commands.ParkourCommand
import com.dumbdogdiner.parkour.courses.CourseManager
import com.dumbdogdiner.parkour.listeners.PlayerDropItemListener
import com.dumbdogdiner.parkour.listeners.PlayerInteractListener
import com.dumbdogdiner.parkour.listeners.PlayerQuitListener
import com.dumbdogdiner.parkour.players.SessionManager
import com.dumbdogdiner.parkour.utils.Configuration
import com.dumbdogdiner.parkour.utils.Utils
import org.bukkit.plugin.java.JavaPlugin

class ParkourPlugin : JavaPlugin() {

    lateinit var courseManager: CourseManager
    lateinit var sessionManager: SessionManager

    override fun onLoad() {
        instance = this
        Configuration.loadDefaultConfig()
    }

    override fun onEnable() {
        courseManager = CourseManager()
        sessionManager = SessionManager()

        server.pluginManager.registerEvents(PlayerInteractListener(), this)
        server.pluginManager.registerEvents(PlayerDropItemListener(), this)
        server.pluginManager.registerEvents(PlayerQuitListener(), this)

        // Register PAPI expansion if available.
        if (server.pluginManager.getPlugin("PlaceholderAPI") != null) {
            logger.info("Attached PlaceholderAPI extension.")
            PapiExpansion().register()
        }

        val parkourCommand = getCommand("parkour")!!
        parkourCommand.setExecutor(ParkourCommand())
    }

    override fun onDisable() {
        courseManager.saveCourses()
    }

    companion object {
        lateinit var instance: ParkourPlugin
    }
}
