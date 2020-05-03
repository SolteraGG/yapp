package com.dumbdogdiner.parkour

import com.dumbdogdiner.parkour.commands.ParkourCommand
import com.dumbdogdiner.parkour.courses.CourseManager
import com.dumbdogdiner.parkour.listeners.PlayerInteractListener
import com.dumbdogdiner.parkour.players.SessionManager
import org.bukkit.plugin.java.JavaPlugin
import com.dumbdogdiner.parkour.utils.Configuration

class ParkourPlugin : JavaPlugin() {

    lateinit var courseManager: CourseManager
    lateinit var sessionManager: SessionManager

    override fun onLoad() {
        instance = this
        Configuration.loadDefaultConfig()
    }

    override fun onEnable() {
        courseManager = CourseManager(this)
        sessionManager = SessionManager(this)

        server.pluginManager.registerEvents(PlayerInteractListener(this), this)

        val parkourCommand = getCommand("parkour")!!
        parkourCommand.setExecutor(ParkourCommand())
    }

    companion object {
        lateinit var instance: ParkourPlugin
    }
}