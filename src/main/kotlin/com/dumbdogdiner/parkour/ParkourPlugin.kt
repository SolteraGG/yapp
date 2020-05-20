package com.dumbdogdiner.parkour

import com.dumbdogdiner.parkour.commands.ParkourCommand
import com.dumbdogdiner.parkour.courses.CourseManager
import com.dumbdogdiner.parkour.editor.EditingSessionManager
import com.dumbdogdiner.parkour.listeners.PlayerDropItemListener
import com.dumbdogdiner.parkour.listeners.PlayerInteractListener
import com.dumbdogdiner.parkour.listeners.PlayerQuitListener
import com.dumbdogdiner.parkour.listeners.WorldListener
import com.dumbdogdiner.parkour.session.SessionManager
import com.dumbdogdiner.parkour.utils.Configuration
import org.bukkit.plugin.java.JavaPlugin

class ParkourPlugin : JavaPlugin() {

    lateinit var courseManager: CourseManager
    lateinit var sessionManager: SessionManager
    lateinit var editingSessionManager: EditingSessionManager

    override fun onLoad() {
        instance = this
        Configuration.loadDefaultConfig()
    }

    override fun onEnable() {
        courseManager = CourseManager()
        sessionManager = SessionManager()
        editingSessionManager = EditingSessionManager()

        // events events events
        server.pluginManager.registerEvents(PlayerInteractListener(), this)
        server.pluginManager.registerEvents(PlayerDropItemListener(), this)
        server.pluginManager.registerEvents(PlayerQuitListener(), this)
        server.pluginManager.registerEvents(WorldListener(), this)

        // Register PAPI expansion if available.
        if (server.pluginManager.getPlugin("PlaceholderAPI") != null) {
            logger.info("Attached PlaceholderAPI extension.")
            PapiExpansion().register()
        }

        // Register commands owo~
        getCommand("parkour")?.setExecutor(ParkourCommand())
    }

    // TODO: Stop onDisable from erroring if something goes horribly wrong during init.
    override fun onDisable() {
        courseManager.saveCourses()
    }

    companion object {
        lateinit var instance: ParkourPlugin
    }
}
