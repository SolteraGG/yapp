package com.dumbdogdiner.parkour.players

import com.dumbdogdiner.parkour.ParkourPlugin
import com.dumbdogdiner.parkour.courses.Course
import org.bukkit.configuration.InvalidConfigurationException
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.io.File
import java.io.IOException

class SessionStorage {
    private val file: File
    private val config: FileConfiguration

    private val plugin = ParkourPlugin.instance

    init {
        file = File(plugin.dataFolder, "records.yml")

        if (!file.exists()) {
            file.parentFile.mkdirs()
            plugin.saveResource("records.yml", false)
        }

        config = YamlConfiguration()
        try {
            config.load(file)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidConfigurationException) {
            e.printStackTrace()
        }
    }

    /**
     * Fetch a player's session.
     */
    fun fetchPlayerSession(player: Player, course: Course): StoredSession? {
        val session = StoredSession()
        val section = config.getConfigurationSection("${course}.${player.uniqueId.toString()}") ?: return null

        session.time = section.getDouble("time")
        session.player = player
        session.course = plugin.courseManager.getCourses()[section.getInt("course")]

        return session
    }

    /**
     * Store a player's session.
     */
    fun storePlayerSession(session: StoredSession) {
        val section = config.getConfigurationSection("${session.course.id}.${session.player.uniqueId.toString()}")!!
        section.set("time", session.time)
    }

    /**
     * Fetch a player's best time.
     */
    fun getPlayerBest(player: Player, course: Course): Double {
        val session = fetchPlayerSession(player, course) ?: return -1.0
        return session.time
    }
}