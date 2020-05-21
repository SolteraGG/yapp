package com.dumbdogdiner.parkour

import com.dumbdogdiner.parkour.commands.ParkourCommand
import com.dumbdogdiner.parkour.courses.CourseManager
import com.dumbdogdiner.parkour.editor.EditingSessionManager
import com.dumbdogdiner.parkour.listeners.*
import com.dumbdogdiner.parkour.session.SessionManager
import com.dumbdogdiner.parkour.utils.Configuration
import com.dumbdogdiner.parkour.utils.Utils

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
        server.pluginManager.registerEvents(PlayerSessionListener(), this)
        server.pluginManager.registerEvents(PlayerEditorSessionListener(), this)

        server.pluginManager.registerEvents(PlayerMiscListener(), this)
        server.pluginManager.registerEvents(PlayerQuitListener(), this)

        server.pluginManager.registerEvents(WorldListener(), this)

        // Register PAPI expansion if available.
        if (server.pluginManager.getPlugin("PlaceholderAPI") != null) {
            Utils.log("Attached PlaceholderAPI extension.")
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
