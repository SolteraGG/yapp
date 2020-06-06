package com.dumbdogdiner.yapp

import com.dumbdogdiner.yapp.commands.ParkourCommand
import com.dumbdogdiner.yapp.courses.CourseManager
import com.dumbdogdiner.yapp.editor.EditingSessionManager
import com.dumbdogdiner.yapp.listeners.PlayerEditorSessionListener
import com.dumbdogdiner.yapp.listeners.PlayerMiscListener
import com.dumbdogdiner.yapp.listeners.PlayerQuitListener
import com.dumbdogdiner.yapp.listeners.PlayerSessionListener
import com.dumbdogdiner.yapp.listeners.WorldListener
import com.dumbdogdiner.yapp.session.SessionManager
import com.dumbdogdiner.yapp.utils.Configuration
import com.dumbdogdiner.yapp.utils.Utils
import org.bukkit.plugin.java.JavaPlugin

class YappParkourPlugin : JavaPlugin() {

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
        lateinit var instance: YappParkourPlugin
    }
}
